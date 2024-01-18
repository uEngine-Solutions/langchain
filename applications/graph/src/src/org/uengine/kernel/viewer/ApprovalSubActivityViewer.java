package org.uengine.kernel.viewer;

import java.util.Map;

import org.uengine.kernel.Activity;
import org.uengine.kernel.ProcessInstance;

public class ApprovalSubActivityViewer extends DefaultActivityViewer{

	protected String getDetails(Activity activity, ProcessInstance instance, Map options) {
		// TODO Auto-generated method stub
		StringBuilder detail = new StringBuilder();
		detail.append(super.getDetails(activity, instance, options)).append("<a href=....>");
		
		return detail.toString();
	}

	
}
