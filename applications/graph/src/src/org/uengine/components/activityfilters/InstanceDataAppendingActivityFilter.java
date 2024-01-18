/*
 * Created on 2004. 12. 19.
 */
package org.uengine.components.activityfilters;

import java.io.Serializable;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ActivityFilter;
import org.uengine.kernel.EJBProcessInstance;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.RoleMapping;
import org.uengine.processdesigner.SimulatorProcessInstance;



/**
 * @author Jinyoung Jang
 */
public class InstanceDataAppendingActivityFilter implements ActivityFilter, Serializable{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public void afterExecute(Activity activity, final ProcessInstance instance)
		throws Exception {
		
		if(instance instanceof SimulatorProcessInstance ||activity.STATUS_SKIPPED.equals(activity.getStatus(instance))) return;

		if(activity instanceof HumanActivity){
			try{
				RoleMapping rm = ((HumanActivity)activity).getRole().getMapping(instance);
				rm.fill(instance);
				if(rm == null) return;
				if(
						instance.isNew() 
						&& instance.getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext()).getActivity().equals(activity)
				){	
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", rm.getEndpoint());
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", rm.getResourceName());
				} else {
					StringBuffer endpoint = new StringBuffer();
					StringBuffer resourceName = new StringBuffer();
					do {
						if (endpoint.length() > 0) endpoint.append(";");
						endpoint.append(rm.getEndpoint());
						
						if (resourceName.length() > 0) resourceName.append(";");
						resourceName.append(rm.getResourceName());
					} while (rm.next());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp", endpoint.toString());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", resourceName.toString());
					//((EJBProcessInstance)instance).getProcessInstanceDAO().set("currACT", activity.getName().getText());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if ( instance.isNew() && instance.isSubProcess() && !instance.getInstanceId().equals(instance.getRootProcessInstanceId())) {
			String initEp = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initEp");
			String initRSNM = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initRSNM");
			((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", initEp);
			((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", initRSNM);
		}
	}

	public void afterComplete(Activity activity, ProcessInstance instance) throws Exception {

	}
	
	public void beforeExecute(Activity activity, ProcessInstance instance)
		throws Exception {
	}

	public void onDeploy(ProcessDefinition definition) throws Exception {
	}

	public void onPropertyChange(Activity activity, ProcessInstance instance, String propertyName, Object changedValue) throws Exception {
		if(instance instanceof SimulatorProcessInstance ||activity.STATUS_SKIPPED.equals(activity.getStatus(instance))) return;

		if(activity instanceof HumanActivity && "saveEndpoint".equals(propertyName)){
			try{
				RoleMapping rm = ((HumanActivity)activity).getRole().getMapping(instance);
				rm.fill(instance);
				if(rm == null) return;
				if(
						instance.isNew() 
						&& instance.getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext()).getActivity().equals(activity)
				){	
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", rm.getEndpoint());
					((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", rm.getResourceName());
					
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp", rm.getEndpoint());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", rm.getResourceName());
				} else {
					StringBuffer endpoint = new StringBuffer();
					StringBuffer resourceName = new StringBuffer();
					do {
						if (endpoint.length() > 0) endpoint.append(";");
						endpoint.append(rm.getEndpoint());
						
						if (resourceName.length() > 0) resourceName.append(";");
						resourceName.append(rm.getResourceName());
					} while (rm.next());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp", endpoint.toString());
					((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", resourceName.toString());
					//((EJBProcessInstance)instance).getProcessInstanceDAO().set("currACT", activity.getName().getText());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if ( instance.isNew() && instance.isSubProcess() && !instance.getInstanceId().equals(instance.getRootProcessInstanceId())) {
				String initEp = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initEp");
				String initRSNM = (String) ((EJBProcessInstance)instance.getRootProcessInstance()).getProcessInstanceDAO().get("initRSNM");
				((EJBProcessInstance) instance).getProcessInstanceDAO().set("initEp", initEp);
				((EJBProcessInstance) instance).getProcessInstanceDAO().set("initRSNM", initRSNM);
				
				((EJBProcessInstance)instance).getProcessInstanceDAO().set("currEp",initEp);
				((EJBProcessInstance)instance).getProcessInstanceDAO().set("currRSNM", initRSNM);
			}
		}
	}
}
