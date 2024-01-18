package org.uengine.kernel.descriptor;


import org.uengine.kernel.Activity;
import org.uengine.kernel.LocalMessengerActivity;
import org.uengine.processdesigner.ProcessDesigner;

public class LocalMessengerActivityDescriptor extends ActivityDescriptor {

	public LocalMessengerActivityDescriptor() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void initialize(ProcessDesigner pd, Activity activity) {
		super.initialize(pd, activity);
		setFieldDisplayNames(LocalMessengerActivity.class);
	}

}
