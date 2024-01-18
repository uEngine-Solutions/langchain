package org.uengine.ui.jsp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.uengine.kernel.Activity;
import org.uengine.kernel.HumanActivity;
import org.uengine.kernel.KeyedParameter;
import org.uengine.kernel.ResultPayload;
import org.uengine.kernel.Role;
import org.uengine.processmanager.ProcessDefinitionRemote;
import org.uengine.processmanager.ProcessManagerFactoryBean;
import org.uengine.ui.list.util.StringUtils;

/**
 * <pre>
 * �� �� �� : WorkItemHandlerSubmitJSPBase.java
 * ��    �� : Workitem Handler Submit SuperClass
 * ���� �ۼ��� : 2007/03/28
 * Comments :
 * �������� :
 * </pre>
 * 
 * @version : 1.0
 * @author : �ŵ���
 */
public abstract class WorkItemHandlerSubmitJSPBase extends WorkItemHandlerJSPBase{
	
	/**
	 * Submit ���������� ���� ������ ����� ��ü.
	 */
	public ResultPayload resultPayload;
	
	/**
	 * WorkItemHandlerSubmitJSPBase Constructor
	 */
    public WorkItemHandlerSubmitJSPBase()
    {
    	super();
	}
    
    /**
     * ���μ����ν��Ͻ� �ʱ�ȭ
     * 
     * @param request
	 * @throws Exception
     */
    public void setInitializeProc(HttpServletRequest request) throws Exception {
    	ProcessDefinitionRemote pdr = null;

		// useBean
		ProcessManagerFactoryBean processManagerFactory = new ProcessManagerFactoryBean();

		pm = processManagerFactory.getProcessManager();

		instanceId = decode(request.getParameter("instanceId"));
		
		processDefinition = decode(request.getParameter("processDefinition"));
		tracingTag = request.getParameter("tracingTag");

		initiate = "yes".equals(request.getParameter("initiate"));	// Submit������  isEventHandler ����.
		
		isViewMode = "yes".equals(request.getParameter("isViewMode"));
		isMine = initiate || "yes".equals(request.getParameter("isMine"));
		taskId = request.getParameter("taskId");

		dispatchingOption = request
				.getParameter(KeyedParameter.DISPATCHINGOPTION);

		isRacing = ("" + Role.DISPATCHINGOPTION_RACING)
				.equals(dispatchingOption);

		if (piRemote != null)
			pdRemote = piRemote.getProcessDefinition();
		
		// --------------------------------------------------------------------
    	isEventHandler = "yes".equals(request.getParameter("isEventHandler"));
    	
    	// added by bhhan 2007-05-31
    	// ������ ó������ �߰���.
    	// WorkItemHandlerJugmSubmitBase before()�� �ű�.
    	//isFlowControl = "yes".equals(request.getParameter("isFlowControl"));
    	
    	// ����-�������� öȸó��,öȸ���,�ݼ���� �� ����ϱ� ���� �߰���.
    	// WorkItemHandlerJugmSubmitBase before()�� �ű�.
    	//isChangeStaus = "yes".equals(request.getParameter("isChangeStaus"));
    	
    	resultPayload = new ResultPayload();
    	resultPayload.setExtendedValues(new KeyedParameter[]{new KeyedParameter("TASKID", taskId)});
    	
    	if(initiate){//The case that this workitem handler should initiate the process
			if(instanceId!=null && (instanceId.trim().equals("null") || instanceId.trim().length()==0))
				instanceId = null;
			instanceId = pm.initializeProcess(processDefinition, instanceId);
		}
    	
		piRemote = pm.getProcessInstance(instanceId);
    	
		Map genericContext = new HashMap();
		genericContext.put(HumanActivity.GENERICCONTEXT_CURR_LOGGED_ROLEMAPPING, loggedRoleMapping);
		genericContext.put("request", request);
		pm.setGenericContext(genericContext);
    }
    
    /**
     * Scriptlet �� ó���� �� ó���� �۾� ����.
     * 
     * @param request
	 * @throws Exception
     */
	public void doAfter(HttpServletRequest request) throws Exception {

		super.doAfter(request);

		// ������ ó������ �߰���.
		System.out.println("setActivityInstanceProperty().........................");
		if(isFlowControl){
//			// öȸ ��� �� ���(06)
//			if("06".equals(request.getParameter("cr_jugm_reslt"))){
//			// ���� tracingTag�� ���� ���� ��� ��ȸ�Ͽ� ���Ѵ�.
//			int crnt_jugm_cnt = 0;										// �ɻ�����
//			if(request.getParameter("crnt_jugm_cnt") != null || !"".equals(request.getParameter("crnt_jugm_cnt")))
//				crnt_jugm_cnt= Integer.parseInt(request.getParameter("crnt_jugm_cnt"));
//			
//			if(crnt_jugm_cnt == 1){
//				tracingTag = "106";
//			}else if(crnt_jugm_cnt == 2){
//				tracingTag = "107";
//			}else{
//				tracingTag = "108";
//			}
//			System.out.println("* öȸ���  tracingTag ID= " + tracingTag);
//							
//			}else{
//				System.out.println("* ������  tracingTag ID= " + tracingTag);
//				int crnt_jugm_cnt = 0;										// �ɻ�����
//				if(request.getParameter("crnt_jugm_cnt") != null || !"".equals(request.getParameter("crnt_jugm_cnt")))
//					crnt_jugm_cnt= Integer.parseInt(request.getParameter("crnt_jugm_cnt"));
//							
//				if(crnt_jugm_cnt == 2){
//					tracingTag = "106";
//				}else{
//					tracingTag = "107";
//				}
//				System.out.println("* ���� tracingTag ID= " + tracingTag);
//			}
			pm.flowControl("compensateTo", instanceId, tracingTag);
		}else if(isChangeStaus){
				//pm.setActivityInstanceProperty(String instanceId, String tracingTag, String propertyName, Serializable value, Class valueType)
			System.out.println("setActivityInstanceProperty().........................");
			// ���� ���»���
			pm.setActivityInstanceProperty(instanceId, beforeTracingTag, "Status", Activity.STATUS_CANCELLED, String.class);
			pm.setActivityInstanceProperty(instanceId, tracingTag, "Status", Activity.STATUS_COMPLETED, String.class);
		}else if(isEventHandler){
			//String mainInstanceId = getParameter(parameterMap, "mainInstanceId");
			System.out.println("executeEventByWorkitem().........................");
			String eventName = request.getParameter("eventName");
			pm.executeEventByWorkitem(instanceId, eventName, resultPayload);
		}else if(initiate){
			pm.executeProcessByWorkitem(instanceId, resultPayload);
		}else{
			System.out.println("completeWorkitem().........................");
			pm.completeWorkitem(instanceId, tracingTag, taskId, resultPayload);
		}
	}
	
	/**
	 * ���μ����ν��Ͻ� ��ü�� ������ �����Ѵ�.
	 * Submit �ÿ��� ResultPayload ��ü�� �̿��Ͽ� �����Ѵ�.
	 *
	 * @param String key
	 * @param Serializable value
	 * @throws Exception 
	 */
	public void setProcVar(String key, Serializable value) 
		throws Exception
	{
	/*	if( resultPayload != null && !StringUtils.isEmpty(key) && value != null){
			resultPayload.setProcessVariableChange(new KeyedParameter(key, value));
		}*/
		
		super.setProcVar(key, value);
	}
}
