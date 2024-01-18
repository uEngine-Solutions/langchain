package org.uengine.kernel.descriptor;


import org.uengine.kernel.Activity;
import org.uengine.kernel.LocalNateOnMessengerActivity;
import org.uengine.processdesigner.ProcessDesigner;

public class LocalNateOnMessengerActivityDescriptor extends ActivityDescriptor {

	public LocalNateOnMessengerActivityDescriptor() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void initialize(ProcessDesigner pd, Activity activity) {
		super.initialize(pd, activity);
		setFieldDisplayNames(LocalNateOnMessengerActivity.class);
	}

}
