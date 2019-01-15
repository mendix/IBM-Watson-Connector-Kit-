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
import watsonservices.utils.VisualRecognitionService;

/**
 * If Classifier is an empty string all existing classifiers will be used
 */
public class ClassifyImage extends CustomJavaAction<java.util.List<IMendixObject>>
{
	private java.lang.String apikey;
	private java.lang.String url;
	private IMendixObject __VisualRequestObject;
	private watsonservices.proxies.VisualRecognitionImage VisualRequestObject;
	private java.util.List<IMendixObject> __classifiers;
	private java.util.List<watsonservices.proxies.Classifier> classifiers;

	public ClassifyImage(IContext context, java.lang.String apikey, java.lang.String url, IMendixObject VisualRequestObject, java.util.List<IMendixObject> classifiers)
	{
		super(context);
		this.apikey = apikey;
		this.url = url;
		this.__VisualRequestObject = VisualRequestObject;
		this.__classifiers = classifiers;
	}

	@Override
	public java.util.List<IMendixObject> executeAction() throws Exception
	{
		this.VisualRequestObject = __VisualRequestObject == null ? null : watsonservices.proxies.VisualRecognitionImage.initialize(getContext(), __VisualRequestObject);

		this.classifiers = new java.util.ArrayList<watsonservices.proxies.Classifier>();
		if (__classifiers != null)
			for (IMendixObject __classifiersElement : __classifiers)
				this.classifiers.add(watsonservices.proxies.Classifier.initialize(getContext(), __classifiersElement));

		// BEGIN USER CODE
		return VisualRecognitionService.classifyImage(getContext(), apikey, url, VisualRequestObject, classifiers);
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@Override
	public java.lang.String toString()
	{
		return "ClassifyImage";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
