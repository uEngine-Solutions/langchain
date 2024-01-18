package org.uengine.kernel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.metaworks.inputter.RadioInput;
import org.metaworks.validator.NotNullValid;
import org.metaworks.validator.Validator;
import org.uengine.contexts.TextContext;
import org.uengine.processdesigner.inputters.RoleResolutionContextSelectorInput;
import org.uengine.util.UEngineUtil;
import org.uengine.util.dao.ConnectiveDAO;
import org.uengine.util.dao.IDAO;

/**
 * @author Jinyoung Jang
 */

public class Role implements java.io.Serializable, Cloneable {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public static final int DISPATCHINGOPTION_AUTO				= -1; // ����
	public static final int DISPATCHINGOPTION_RACING			= 1; // ����
	@Deprecated
	public static final int DISPATCHINGOPTION_LOADBALANCED 		= 2; // �ε� ���
	public static final int DISPATCHINGOPTION_ALL 				= 0; // ���
	@Deprecated
	public static final int DISPATCHINGOPTION_SETBYRIGHTPERSON 	= 4; // ����ڿ� ���� ��d
	public static final int DISPATCHINGOPTION_REFERENCE 		= 5; //����ڿ� ���� ��d
	public static final int DISPATCHINGOPTION_RECEIVE 			= 6; //����ڿ� ���� ��d
	
	public static final int WORKLOAD_BALANCING_OPTION_NONE = 0;
	public static final int WORKLOAD_BALANCING_OPTION_LOWEST = 1;
	public static final int WORKLOAD_BALANCING_OPTION_CUSTOM = 10;
	
	public static final int ASSIGNTYPE_USER		 	= 0;	//����� ��b��d
	public static final int ASSIGNTYPE_DEPT 		= 2;	//�μ�
	public static final int ASSIGNTYPE_GROUP 		= 3;	//�׷�


	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
		//table.removeFieldDescriptor("AskWhenInit");
		//table.removeFieldDescriptor("DispatchingOption");
		//table.removeFieldDescriptor("Identifier");
		//table.removeFieldDescriptor("HumanWorker");
		//table.removeFieldDescriptor("ServiceType");

		String advancedOptions = GlobalContext.getLocalizedMessage("role.advancedoptions.label", "Advanced Options");
		
		type.setName(GlobalContext.getLocalizedMessage("role.displayname", "Role"));
				
		fd = type.getFieldDescriptor("RoleResolutionContext");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.roleresolutioncontext.displayname", "Role"));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
//		fd = table.getFieldDescriptor("AskWhenInit");		
//		fd.setAttribute("hidden", new Boolean(true));
//		//fd.setDisplayName(GlobalContext.getLocalizedMessage("role.askwheninit.displayname"));
		
		/*
		fd = type.getFieldDescriptor("DispatchingOption");
		//fd.setAttribute("hidden", new Boolean(true));
		fd.setInputter(new RadioInput(
			new String[]{
				GlobalContext.getLocalizedMessage("role.dispatchingoption.racing.displayname", "Racing"),
				GlobalContext.getLocalizedMessage("role.dispatchingoption.loadbalanced.displayname", "Load-balanced"),
				GlobalContext.getLocalizedMessage("role.dispatchingoption.setbyrightperson.displayname", "Set by right-person"),
				GlobalContext.getLocalizedMessage("role.dispatchingoption.all.displayname", "All")
			},
			new Object[]{
				new Integer(DISPATCHINGOPTION_RACING), 
				new Integer(DISPATCHINGOPTION_LOADBALANCED), 
				new Integer(DISPATCHINGOPTION_SETBYRIGHTPERSON), 
				new Integer(DISPATCHINGOPTION_ALL)
			}
		));
		*/
		type.removeFieldDescriptor("DispatchingOption");
		
		fd = type.getFieldDescriptor("WorkloadOption");
		//fd.setAttribute("hidden", new Boolean(true));
		fd.setInputter(new RadioInput(
			new String[] {
				GlobalContext.getLocalizedMessage("role.workloadoption.none.displayname", "None"),
				GlobalContext.getLocalizedMessage("role.workloadoption.lowest.displayname", "Lowest Workload")
			},
			new Object[] {
				new Integer(WORKLOAD_BALANCING_OPTION_NONE),
				new Integer(WORKLOAD_BALANCING_OPTION_LOWEST),
			}
		));

		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.dispatchingoption.displayname", ""));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
//
//		fd = table.getFieldDescriptor("Identifier");
//		fd.setAttribute("hidden", new Boolean(true));
//
//		fd = table.getFieldDescriptor("HumanWorker");
//		fd.setAttribute("hidden", new Boolean(true));
//		
//		fd = table.getFieldDescriptor("ServiceType");
//		fd.setAttribute("hidden", new Boolean(true));

		fd = type.getFieldDescriptor("Name");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.name.displayname", "Role Name (id)"));
		fd.setValidators(new Validator[]{new NotNullValid()}); //it's mandatory

		fd = type.getFieldDescriptor("DisplayName");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.displayname.displayname", "Display Name"));

		fd = type.getFieldDescriptor("DefaultEndpoint");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.defaultendpoint.displayname", "DefaultEndpoint"));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
		fd = type.getFieldDescriptor("DontPersistResolutionResult");		
		fd.setDisplayName(GlobalContext.getLocalizedMessage("role.dontpersistresolutionresult.displayname", "Do not persist the resolution result"));
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
		fd = type.getFieldDescriptor("RoleResolutionContext");
		fd.setInputter(new RoleResolutionContextSelectorInput());
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd = type.getFieldDescriptor("ServiceType");
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd = type.getFieldDescriptor("HumanWorker");
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd = type.getFieldDescriptor("Identifier");
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");
		
		fd = type.getFieldDescriptor("AskWhenInit");		
		fd.setAttribute("group", advancedOptions);
		fd.setAttribute("hiddenInTable", "yes");

		fd.setAttribute("collapseGroup", true);
	}

	private java.lang.String name;
		public String getName() {
			return name;
		}
		public void setName(String value) {
			name = value;
		}

	private RoleResolutionContext roleResolutionContext = null;
		public RoleResolutionContext getRoleResolutionContext() {
			if (roleResolutionContext instanceof DirectRoleResolutionContext) {
				DirectRoleResolutionContext drrc = (DirectRoleResolutionContext) roleResolutionContext;
				if(!UEngineUtil.isNotEmpty(drrc.getEndpoint())) {
					roleResolutionContext = null;
				}
			}
			
			return roleResolutionContext;
		}
		public void setRoleResolutionContext(RoleResolutionContext context) {
			roleResolutionContext = context;

			if(context!=null){
				setAskWhenInit(false);
			}
		}
	
	int dispatchingOption = DISPATCHINGOPTION_AUTO;
		public int getDispatchingOption() {
			return dispatchingOption;
		}
		public void setDispatchingOption(int i) {
			dispatchingOption = i;
		}
		
	int workloadOption = WORKLOAD_BALANCING_OPTION_NONE;
		public int getWorkloadOption() {
			return workloadOption;
		}
		public void setWorkloadOption(int workloadOption) {
			this.workloadOption = workloadOption;
		}

	private ServiceDefinition serviceType;
		public ServiceDefinition getServiceType() {
			return serviceType;
		}
		public void setServiceType(ServiceDefinition definition) {
			serviceType = definition;
		}
	
	private boolean isHumanWorker;
		public boolean isHumanWorker() {
			return isHumanWorker;
		}	
		public void setHumanWorker(boolean b) {
			isHumanWorker = b;
		}
		
	private boolean askWhenInit = true;
		public boolean isAskWhenInit() {
			return askWhenInit;
		}
		public void setAskWhenInit(boolean b) {
			askWhenInit = b;
		}

	private ProcessVariable identifier;
		public ProcessVariable getIdentifier() {
			return identifier;
		}
		public void setIdentifier(ProcessVariable variable) {
			identifier = variable;
		}

	private String defaultEndpoint;
		public String getDefaultEndpoint() {
			return defaultEndpoint;
		}
		public void setDefaultEndpoint(String string) {
			defaultEndpoint = string;
		}
		
	private TextContext displayName = TextContext.createInstance();
		public TextContext getDisplayName() {
			if(displayName==null){
				displayName = TextContext.createInstance();
			}
			
			if(displayName.getText()==null)
				displayName.setText(getName());
			
			return displayName;
		}
		public void setDisplayName(TextContext string) {
			displayName = string;
		}
		public void setDisplayName(String string) {
			displayName.setText(string);
		}

	boolean dontPersistResolutionResult;
		public boolean isDontPersistResolutionResult() {
			return dontPersistResolutionResult;
		}
		public void setDontPersistResolutionResult(boolean dontPersistResolutionResult) {
			this.dontPersistResolutionResult = dontPersistResolutionResult;
		}
	
//added
	
	public Role(){
	}

	public Role(String name){
		this. name = name;
	}
	
	public RoleMapping getMapping(ProcessInstance inst) throws Exception{
		return getMapping(inst, (String)null);
	}

	public RoleMapping getMapping(ProcessInstance inst, Activity activity) throws Exception{
		return getMapping(inst, activity.getTracingTag());
	}
		
	public RoleMapping getMapping(ProcessInstance inst, String tracingTag) throws Exception{
		RoleMapping mapping = null;
		Role role = null;
		ProcessDefinition definition = null;
	
		if(inst!=null) {
			mapping = inst.getRoleMapping(getName());
			
			if (inst.getProcessDefinition()!=null) {
				definition = inst.getProcessDefinition();
				role = definition.getRole(getName());
			}
		}
		
		if(role==null)
			role = this;
			
		//clean up the existing resolution result when isDontPersistResolutionResult() option is true
		if(mapping!=null && isDontPersistResolutionResult()){
			mapping=null;
		}
		
		//TODO: is it hard-code?
		if(mapping==null) {
			//try to use role resolution context		
			Exception resolutionException = null;
			if (role.getRoleResolutionContext()!=null) {
				try{
					mapping = role.getRoleResolutionContext().getActualMapping(definition, inst, tracingTag, new java.util.Hashtable()); // danger roop with DefaultCompanyRoleResolutionContext.java (line 64)
					
					// 업무 로드 밸런스
					if (inst != null && this.getWorkloadOption() == WORKLOAD_BALANCING_OPTION_LOWEST && mapping.size() > 1) {
						List<String> mappingEndpoints = new ArrayList<String>();
						List<String> mappingResourceNames = new ArrayList<String>();
						mapping.beforeFirst();
						do {
							mappingEndpoints.add(mapping.getEndpoint());
							mappingResourceNames.add(mapping.getResourceName());
						} while (mapping.next());
						mapping.beforeFirst();
						
						/*
						Activity act = definition.getActivity(tracingTag);
						String taskId = null;
						if (act instanceof HumanActivity) {
							String[] taskIds = ((HumanActivity)act).getTaskIds(inst);
							if (taskIds != null && taskIds.length > 0) {
								taskId = taskIds[0];	
							}
						}
						*/
						
						String propertyKey = "roleHistory_" + role.getName();
						List<RoleHistory> roleHistoryList = (List<RoleHistory>) inst.getProperty("", propertyKey);
						if (roleHistoryList == null) {
							roleHistoryList = new ArrayList<RoleHistory>();
						}
						RoleHistory roleHistory = new RoleHistory(new Date(), mappingEndpoints, mappingResourceNames);
						roleHistoryList.add(roleHistory);
						inst.setProperty("", propertyKey, (Serializable) roleHistoryList);
						
						StringBuffer endpointsInQuery = new StringBuffer();
						for (String endpoint : mappingEndpoints) {
							if (endpointsInQuery.length() > 0) endpointsInQuery.append(",");
							endpointsInQuery.append("'").append(endpoint).append("'");
						}
						endpointsInQuery.append(")");
						endpointsInQuery.insert(0, "(");
						
						StringBuffer sql = new StringBuffer();
						sql.append("SELECT "); 
						sql.append("	A.* "); 
						sql.append("FROM ( "); 
						sql.append("	SELECT "); 
						sql.append("		A.ENDPOINT, "); 
						sql.append("		COUNT(A.TASKID) AS CNT "); 
						sql.append("	FROM ( "); 
						sql.append("		SELECT "); 
						sql.append("			(CASE "); 
						sql.append("				WHEN WL.ROLENAME IS NULL THEN WL.ENDPOINT "); 
						sql.append("				ELSE ROLE.ENDPOINT "); 
						sql.append("			END) AS ENDPOINT, ");
						sql.append("			WL.TASKID "); 
						sql.append("		FROM BPM_WORKLIST WL "); 
						sql.append("		JOIN BPM_PROCINST INST ON WL.INSTID = INST.INSTID AND INST.ISDELETED = 0 AND INST.STATUS = 'Running' "); 
						sql.append("		JOIN BPM_ROLEMAPPING ROLE ON INST.INSTID = ROLE.INSTID AND WL.ROLENAME = ROLE.ROLENAME "); 
						sql.append("		WHERE WL.STATUS IN ( 'NEW', 'CONFIRMED' ) "); 
						sql.append("		AND ( WL.ENDPOINT IN ").append(endpointsInQuery).append(" OR ROLE.ENDPOINT IN ").append(endpointsInQuery).append(" ) "); 
						sql.append("	) A GROUP BY A.ENDPOINT "); 
						sql.append(") A ORDER BY A.CNT ASC ");
						
						IDAO idao = ConnectiveDAO.createDAOImpl(inst.getProcessTransactionContext(), sql.toString(), IDAO.class);
						idao.select();
						
						List<String> workloadList = new ArrayList<String>();
						while (idao.next()) {
							workloadList.add(idao.getString("ENDPOINT"));
						}
						idao.releaseResource();
						
						String lowestWorkloadEndpoint = null;
						if (workloadList.size() == 0) {
							// 쿼리 결과가 없다면 mapping 중 첫번째 유저로 맵핑
							lowestWorkloadEndpoint = mapping.getEndpoint();
						} else if (workloadList.size() == mapping.size()) {
							// 사이즈가 같다면 업무 카운트가 가장 낮은 유저로 맵핑
							lowestWorkloadEndpoint = workloadList.get(0);
						} else {
							// 작다면 업무 자체가 실행되지 않은 유저가 있다는 것이기 때문에 쿼리 질의와 mapping 을 비교해서 없는 유저로 맵핑 시킨다. 
							for (String queryEndpoint : workloadList) {
								for (String mappingEndpoint : mappingEndpoints) {
									if (!queryEndpoint.equals(mappingEndpoint)) {
										lowestWorkloadEndpoint = queryEndpoint;
										break;
									}
								}
							}
						}
						
						RoleMapping newRoleMapping = RoleMapping.create();
						newRoleMapping.setEndpoint(lowestWorkloadEndpoint);
						newRoleMapping.fill(inst);
						newRoleMapping.beforeFirst();
						mapping = newRoleMapping;
					}
					
				}catch(Exception e){
					resolutionException = e;
					e.printStackTrace();
				}
				
				//set the rolemapping if resolution succeed. 
				//this should work for the case 'Racing' work-distribution option for future reference of the resolved mapping.
				if(!isDontPersistResolutionResult() && mapping!=null && inst != null ){
					inst.putRoleMapping(getName(), mapping);
				}
			}
			
			//try to use default finally
			if(mapping==null && role.getDefaultEndpoint()!=null){
				try{
					mapping = RoleMapping.create();
					mapping.setName(role.getName());
					mapping.setEndpoint(role.getDefaultEndpoint());
					
					if(role.isHumanWorker())
						mapping.fill(inst);
				}catch(Exception e){
					
					throw new UEngineException("Can't find user where the id ["+ role.getDefaultEndpoint()+ "] since: " + e.getMessage() + "\n Please contact to the process administrator.");
				}
			}else if(resolutionException!=null){
				throw resolutionException;
			}
		}
		
		if(mapping!=null){
			mapping.setCursor(0);
		}
		
		return mapping;
	}
	
	public static Role forName(String name){
		return forName(name, null);
	}
	
	public static Role forName(String name, String defaultEP){	
		//review: fly-weight pattern needed
		Role role = new Role(name);
		role.setDefaultEndpoint(defaultEP);
		
		return role;
	}
	
	public boolean equals(Object obj){
		try{
			return getName().equals(((Role)obj).getName());
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean containsMapping(ProcessInstance instance, RoleMapping testingRoleMapping) throws Exception{
		RoleMapping thisRoleMapping = getMapping(instance);
		
		//���� �� ������ �μ��� ���� �μ��� ���ٸ� ����8�� ��		
		if(thisRoleMapping.getAssignType() == ASSIGNTYPE_DEPT){
			if(thisRoleMapping.getGroupId().equals(testingRoleMapping.getGroupId()))
				return true;
			else
				return false;
		}

		do{
			if(testingRoleMapping.getEndpoint().equals(thisRoleMapping.getEndpoint())){
				return true;
			}
		}while(thisRoleMapping.next());
		
		return false;
	}
	
	

//end

	public String toString() {
		String dispName = getDisplayName().toString(); 
		
		if(dispName!=null)
			return dispName;
		
		return super.toString();
	}
	
	public Object clone(){
		//TODO [tuning point]: Object cloning with serialization. it will be called by ProcessManagerBean.getProcessDefintionXX method.
		try{
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream ow = new ObjectOutputStream(bao);
			ow.writeObject(this);
			ByteArrayInputStream bio = new ByteArrayInputStream(bao.toByteArray());			
			ObjectInputStream oi = new ObjectInputStream(bio);
			
			Role clonedOne = (Role) oi.readObject();
			clonedOne.setIdentifier(null);
			clonedOne.setServiceType(null);
			clonedOne.setRoleResolutionContext(null);
			
			return clonedOne;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
