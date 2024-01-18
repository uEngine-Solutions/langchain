package org.uengine.kernel.descriptor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.metaworks.FieldDescriptor;
import org.metaworks.inputter.Inputter;
import org.uengine.kernel.Activity;
import org.uengine.kernel.DRoolsActivity;
import org.uengine.kernel.FTPActivity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.processdesigner.ProcessDesigner;
import org.uengine.ui.XMLValueInput;
import org.uengine.util.ClientProxy;

/**
 * 
 * @author Jinyoung Jang
 * @author <a href="mailto:ghbpark@hanwha.co.kr">Sungsoo Park</a>
 * @version $Id: SubProcessActivityDescriptor.java,v 1.3 2006/11/06 04:06:47
 *          pongsor Exp $
 */
public class DRoolsActivityDescriptor extends ActivityDescriptor {

	Inputter bindingVariablesInput;

	Inputter bindingRolesInput;

	Inputter definitionIdInput;

	public DRoolsActivityDescriptor() throws Exception {
		super();
	}

	public void initialize(final ProcessDesigner pd, Activity activity) {
		super.initialize(pd, activity);

		FieldDescriptor fd;
		fd = getFieldDescriptor("VariableBindings");
		bindingVariablesInput = fd.getInputter();

		fd = getFieldDescriptor("DefinitionId");

		XMLValueInput inputter = new XMLValueInput("/processmanager/processDefinitionListXML.jsp?omitVersion=false&objectType=rule") {
			public void onValueChanged() { 
				changeBindingArguments((String) getValue());
			}
		};

		fd.setInputter(inputter);
		definitionIdInput = inputter;
		
		setFieldDisplayNames(DRoolsActivity.class);
	}

	protected void changeBindingArguments(String pvId) {
		System.out.println(pvId);
		
		if (pvId == null)
			return;

		try {
			//ProcessDefinition pd = null;
			ArrayList array = null;
			if (pvId.indexOf("@") > -1) {
				String[] defIdAndVersionId = pvId.split("@");
				//pd = loadDesign(defIdAndVersionId[1], false); // load with
				array = loadDesign(defIdAndVersionId[1], false); // load with

				// version id;
			} else {
				array = loadDesign(pvId, false);
				//pd = loadDesign(array = loadDesign(defIdAndVersionId[1], false);, true); // load with definition id;
				//definitionIdInput.setValue(null); // clear picker's remained
				// selection
				//definitionIdInput.setValue(pvId + "@" + pd.getId()); // reload
				// version
				//((XMLValueInput) definitionIdInput).setDisplayValue(pd.getName()+ " Version_" + pd.getVersion());

				// return;
			}
			System.out.println("load binding information...");

			/*
			 * RoleParameterContext[] oldRPCs =
			 * (RoleParameterContext[])bindingRolesInput.getValue(); HashMap
			 * oldRPCHM = new HashMap(); if(oldRPCs!=null) for(int i=0; i<oldRPCs.length;
			 * i++) oldRPCHM.put(oldRPCs[i].getArgument(), oldRPCs[i]);
			 * 
			 * Role[] roles = pd.getRoles(); RoleParameterContext[] rolePCs =
			 * new RoleParameterContext[roles.length];
			 */
			/*
			 * for(int i=0; i<roles.length; i++){ rolePCs[i] = new
			 * RoleParameterContext();
			 * rolePCs[i].setArgument(roles[i].getName());
			 * //rolePCs[i].setRole(roles[i]);
			 * //rolePCs[i].setDirection(rolePCs[i].getDirection());
			 * 
			 * if(oldRPCs!=null &&
			 * oldRPCHM.containsKey(rolePCs[i].getArgument())){
			 * RoleParameterContext theRPC =
			 * (RoleParameterContext)oldRPCHM.get(rolePCs[i].getArgument());
			 * rolePCs[i].setRole(theRPC.getRole());
			 * rolePCs[i].setDirection(theRPC.getDirection()); } }
			 * bindingRolesInput.setValue(rolePCs);
			 */
			//
			ParameterContext[] oldpvPCs = (ParameterContext[]) bindingVariablesInput.getValue();

			HashMap oldpvPCsM = new HashMap();
			if (oldpvPCs != null) {
				for (int i = 0; i < oldpvPCs.length; i++)
					oldpvPCsM.put(oldpvPCs[i].getArgument(), oldpvPCs[i]);
			}

			//ProcessVariable[] processVariables = pd.getProcessVariables();			
			//ParameterContext[] pvPCs = new ParameterContext[processVariables.length];
			ParameterContext[] pvPCs = new ParameterContext[array.size()];
			
			//for (int i = 0; i < processVariables.length; i++) {
			for(int i=0;i<array.size();i++){
				pvPCs[i] = new ParameterContext();
				//pvPCs[i].getArgument().setText(processVariables[i].getName());
				pvPCs[i].getArgument().setText((String)array.get(i));

				if (oldpvPCs != null
						&& oldpvPCsM.containsKey(pvPCs[i].getArgument())) {
					ParameterContext thepvPCs = (ParameterContext) oldpvPCsM
							.get(pvPCs[i].getArgument());
					pvPCs[i].setVariable(thepvPCs.getVariable());
					pvPCs[i].setDirection(thepvPCs.getDirection());
				}
			}
			bindingVariablesInput.setValue(pvPCs);
            System.out.println(pvPCs);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected ArrayList loadDesign(String id, boolean withDefinitionId)
			throws Exception {

		System.out.println("loadDesign(String " + id + ", boolean "
				+ withDefinitionId + ")");
		ClientProxy proxy = ProcessDesigner.getClientProxy();
		InputStream is;

		is = proxy.showRuleDefinitionWithDefinitionId(id);

		// InputStream is = new URL(UEngineUtil.getWebServerBaseURL() +
		// ProcessDesigner.URL_showProcessDefinitionJSPWithDefinitionId +
		// definitionId).openStream();
		ArrayList array = (ArrayList) GlobalContext.deserialize(
				is, ArrayList.class);

		return array;
	}

}