package watsonservices.utils;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import org.apache.commons.lang3.StringUtils;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.SentenceAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneInput;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import com.mendix.core.Core;
import com.mendix.logging.ILogNode;
import com.mendix.systemwideinterfaces.MendixException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;

import watsonservices.proxies.Tone;
import watsonservices.proxies.ToneAnalyzerResponse;

public class ToneAnalyzerService {
	
	private static final String WATSON_TONE_ANALYZER_LOGNODE = "WatsonServices.IBM_WatsonConnector_ToneAnalyzer";
	private static final ILogNode LOGGER = Core.getLogger((Core.getConfiguration().getConstantValue(WATSON_TONE_ANALYZER_LOGNODE).toString()));
	private static final String WATSON_TONE_ANALYZER_VERSION_DATE = "2017-09-21";

	public static IMendixObject analyzeTone(IContext context, String apiKey, String url, String text) throws MendixException {
		LOGGER.debug("Executing Watson AnalyzeTone Connector...");

		IamOptions iamOptions = new IamOptions.Builder()
				.apiKey(apiKey)
				.build();
		final ToneAnalyzer service = new ToneAnalyzer(WATSON_TONE_ANALYZER_VERSION_DATE, iamOptions);
		service.setEndPoint(url);

		final ToneInput input = new ToneInput.Builder()
				.text(text)
				.build();

		final ToneOptions options = new ToneOptions.Builder()
				.toneInput(input)
				.build();

		// Call the service and get the tone
		final ToneAnalysis response;
		try
		{
			response = service.tone(options).execute();
		} catch (Exception e) {
			LOGGER.error("Watson Service connection - Failed analyzing the tone of the text " + StringUtils.abbreviate(text, 20), e);
			throw new MendixException(e);
		}

		return createDocumentTone(context, response);
	}

	private static IMendixObject createDocumentTone(IContext context, ToneAnalysis response) {
		final IMendixObject toneAnalyzerResponse = Core.instantiate(context, ToneAnalyzerResponse.entityName);

		response.getDocumentTone().getTones().forEach(toneScore -> buildDocumentTone(context, toneAnalyzerResponse, toneScore));

		if(response.getSentencesTone() != null && !response.getSentencesTone().isEmpty())
		{
			response.getSentencesTone().forEach(sentenceTone -> buildSentencesTones(context, toneAnalyzerResponse, sentenceTone));
		} 
		

		return toneAnalyzerResponse;
	}

	private static void buildDocumentTone(IContext context, final IMendixObject toneAnalyzerResponse,
			ToneScore toneScore)
	{
		final IMendixObject toneObject = buildTone(context, toneScore);
		toneObject.setValue(context, watsonservices.proxies.Tone.MemberNames.Document_Tones.toString(), toneAnalyzerResponse.getId());
	}

	private static void buildSentencesTones(IContext context, final IMendixObject toneAnalyzerResponse,
			SentenceAnalysis sentenceAnalysis)
	{
		final IMendixObject sentenceToneObject = Core.instantiate(context, watsonservices.proxies.SentenceTone.entityName);
		sentenceToneObject.setValue(context, watsonservices.proxies.SentenceTone.MemberNames.SentenceId.toString(), sentenceAnalysis.getSentenceId());
		sentenceToneObject.setValue(context, watsonservices.proxies.SentenceTone.MemberNames.Text.toString(), sentenceAnalysis.getText());

		sentenceAnalysis.getTones().forEach(toneScore -> buildSentenceTone(context, sentenceToneObject, toneScore));

		sentenceToneObject.setValue(context, watsonservices.proxies.SentenceTone.MemberNames.Sentence_Tones.toString(), toneAnalyzerResponse.getId());
	}

	private static void buildSentenceTone(IContext context, final IMendixObject sentenceToneObject,
			ToneScore toneScore) {
		final IMendixObject toneObject = buildTone(context, toneScore);
		toneObject.setValue(context, watsonservices.proxies.Tone.MemberNames.Tone_SentenceTone.toString(), sentenceToneObject.getId());
	}

	private static IMendixObject buildTone(IContext context, ToneScore toneScore)
	{
		final IMendixObject toneObject = Core.instantiate(context, Tone.entityName);
		toneObject.setValue(context, Tone.MemberNames.ToneId.toString(), toneScore.getToneId());
		toneObject.setValue(context, Tone.MemberNames.Name.toString(), toneScore.getToneName());
		toneObject.setValue(context, Tone.MemberNames.Score.toString(), toneScore.getScore().toString());
		return toneObject;
	}

}
