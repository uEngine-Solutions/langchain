/*
 * Created on 2004. 12. 19.
 */
package org.uengine.components.activityfilters;

import org.uengine.kernel.*;

import java.io.Serializable;
import java.util.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Jinyoung Jang
 */
public class AlarmActivityFilter implements ActivityFilter, Serializable{

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	final static String messageBody = GlobalContext.getPropertyString("alarmactivityfilter.message", "You've received a work to do");
	final static String host = GlobalContext.getPropertyString("alarmactivityfilter.wih.host");
	final static String port = GlobalContext.getPropertyString("alarmactivityfilter.wih.port");
	
	public void afterExecute(Activity activity, final ProcessInstance instance)
		throws Exception {
		
		if(instance.isSimulation()) return;

		if(activity instanceof HumanActivity){
			HttpServletRequest request = (HttpServletRequest) instance.getProcessTransactionContext().getServletRequest();
			if(request!=null && "yes".equals(request.getParameter("initiate"))){
				Activity firstExecuted = null;
				try{
					firstExecuted = ((ActivityInstanceContext)instance.getProcessTransactionContext().getExecutedActivityInstanceContextsInTransaction().get(0)).getActivity();
				}catch(Exception e){
				}
				
				if(firstExecuted == null)
					return;
				
				if(firstExecuted == activity)
					return;	//���� ��ũ�������� ���� �޽�¡ �� �ʿ��=.
			}
			
			try{
				if(instance.isNew() && instance.getProcessDefinition().getInitiatorHumanActivityReference(instance.getProcessTransactionContext()).getActivity().equals(activity)) return;
			}catch(Exception e){
				e.printStackTrace();
			}
			
			try{
				HumanActivity humanActivity = (HumanActivity)activity;				
				
				//TODO: hard-coded
				final LocalMessengerActivity alarm = new LocalMessengerActivity(){

					public void fireComplete(ProcessInstance instance) throws Exception {
						//disabled
					}
					
				};
			
				RoleMapping roleMapping = (RoleMapping)(humanActivity.getRole()).getMapping(instance, activity.getTracingTag());
				String[] taskIds = humanActivity.getTaskIds(instance);
				final String taskId = taskIds[0];

				alarm.setContents(
						messageBody + " \""+
						humanActivity.getName() + "\"" + 
						"   - http://" + (host!=null ?  host : request.getServerName()) + (port!=null ? (":" + port) : (request.getServerPort() != 80 ? ":" + request.getServerPort() : "")) + GlobalContext.WEB_CONTEXT_ROOT + "/processparticipant/worklist/workitemHandler.jsp?taskId=" + 
						taskId + 
						"&userId=" + roleMapping.getEndpoint() +
						"&password=" + roleMapping.getExtendedProperty("password")
						
				);
				
				alarm.setToRole(humanActivity.getRole());
				
				Thread sender = new Thread(){

					public void run() {
						try {
							alarm.executeActivity(instance);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				};
				
				sender.start();
				
				
																
			}catch(Exception e){
				System.out.println("[AlarmActivityFilter] failed to send alarm: " + e.getMessage());
			}
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
	}

}
