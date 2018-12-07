// This file was generated by Mendix Modeler.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package watsonservices.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import watsonservices.utils.LanguageTranslationService;

/**
 * FromLang could be empty. Language will be detected if possible. If not possible an exception will be thrown
 * 
 */
public class Translate extends CustomJavaAction<IMendixObject>
{
	private IMendixObject __translation;
	private watsonservices.proxies.Translation translation;
	private java.lang.String apikey;
	private java.lang.String url;

	public Translate(IContext context, IMendixObject translation, java.lang.String apikey, java.lang.String url)
	{
		super(context);
		this.__translation = translation;
		this.apikey = apikey;
		this.url = url;
	}

	@Override
	public IMendixObject executeAction() throws Exception
	{
		this.translation = __translation == null ? null : watsonservices.proxies.Translation.initialize(getContext(), __translation);

		// BEGIN USER CODE
		return LanguageTranslationService.translate(getContext(), translation, apikey, url);
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@Override
	public java.lang.String toString()
	{
		return "Translate";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
