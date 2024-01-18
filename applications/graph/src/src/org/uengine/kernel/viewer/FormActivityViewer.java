package org.uengine.kernel.viewer;

import java.io.Serializable;
import java.util.Map;

import org.uengine.contexts.HtmlFormContext;
import org.uengine.kernel.Activity;
import org.uengine.kernel.FormActivity;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.ProcessVariable;

public class FormActivityViewer extends DefaultActivityViewer {

	public StringBuffer getActivityPropertyString(Activity activity, ProcessInstance instance, Map options) throws Exception {
		FormActivity formActivity = (FormActivity) activity;

		StringBuffer formActivityPropertyString = super.getActivityPropertyString(activity, instance, options);
		String formDefinitionId = ((HtmlFormContext) formActivity.getVariableForHtmlFormContext().getDefaultValue()).getFormDefId();

		formActivityPropertyString.append("formDefinitionId=").append(formDefinitionId).append(",");

		ProcessVariable variableForHtmlFormContext = formActivity.getVariableForHtmlFormContext();
		if (formActivity.getVariableForHtmlFormContext() != null) {
			formActivityPropertyString.append("formVariableName=").append(formActivity.getVariableForHtmlFormContext().getName()).append(",");

			Serializable data = instance.get("", variableForHtmlFormContext.getName());
			if (data != null && data instanceof HtmlFormContext) {
				formActivityPropertyString.append("formVariableFilePath=").append(((HtmlFormContext) data).getFilePath()).append(",");
			}
		}

		String[] taskId = formActivity.getTaskIds(instance);
		if (taskId != null)
			formActivityPropertyString.append("taskId=").append(taskId[0]).append(",");

		return formActivityPropertyString;
	}
}
