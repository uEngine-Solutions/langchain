package org.uengine.processpublisher.bpel.exporter;

import org.uengine.kernel.MessageDefinition;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.ReceiveActivity;

import org.xmlsoap.schemas.ws.n2003.n03.business_process.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.groups.*;
import com.commerceone.xdk.castor.types.*;
import org.uengine.processpublisher.Adapter;
import java.util.Vector;
	 
/**
 * @author Jinyoung Jang
 */
             
public class ReceiveActivityAdapter implements Adapter{

	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		ReceiveActivity srcAct = (ReceiveActivity)src;
		
		tSequence destSeqAct = new tSequence();
		destSeqAct.setname_Attribute(new XNCName(srcAct.getName().getText().replace(' ', '_').replace('?', '_') + "Sequence"));
		
		//---- build receive activity ----
		String message = "on"+srcAct.getMessage().replace(' ', '_');
		String variableName = message + "Request";{
			Vector addVariables = (Vector)keyedContext.get("addVariables");
			XQName xqName = new XQName("sdns", "", variableName);
			variableName = "var_" + variableName; 
			addVariables.add(new Object[]{variableName, xqName});			
		}
		
		MessageDefinition md = srcAct.getMessageDefinition();
		tReceive destAct = new tReceive();{
			destAct.setname_Attribute(new XNCName(srcAct.getName().getText().replace(' ', '_').replace('?', '_')));
			
			String procName = srcAct.getProcessDefinition().getName().getText().replace(' ','_').replace('?', '_');
			destAct.setportType_Attribute(new XQName("sdns", "", procName));
			try{
				//destAct.setvariable_Attribute(new XNCName(srcAct.getOutput().getName()));
			}catch(Exception e){
				//invocation with null parameter
			}
			
			//review:
			destAct.setoperation_Attribute(new XNCName(message));
			
			String fromRole;
			if(srcAct.getFromRole()!=null){
				fromRole = srcAct.getFromRole().getName().replace(' ', '_');
				
				//correlation setting
				boolean initiateCorrelation = false;
				ProcessVariable identifier = srcAct.getFromRole().getIdentifier();
				if(identifier!=null){
					ParameterContext params[] = srcAct.getParameters();
		
					if(params!=null && params.length>0)
					for(int i=0; i<params.length; i++){
						if(params[i].equals(identifier)){
							initiateCorrelation = true;
							break;
						}
					}
				}
				
				if(initiateCorrelation){
					tCorrelations correlations = new tCorrelations();
					tCorrelation correlation = new tCorrelation();
					correlation.setinitiate_Attribute(tBoolean.YES);
					correlation.setset_Attribute(new XNCName(fromRole + "Identification"));
					correlations.addcorrelation(correlation);
					destAct.setcorrelations(correlations);		
				}
				//
				
			}else{
				fromRole = "myRole";
			}
						
			destAct.setpartnerLink_Attribute(new XNCName(fromRole));
			
			destAct.setvariable_Attribute(new XNCName(variableName));
		}
		//---- end of receive activity ----

		activity actGrp = new activity();
		actGrp.setreceiveAsChoice(destAct);
		destSeqAct.addactivity_Group(actGrp);

		
		//---- build copy activity ----
		boolean assignUsed = true;
		tAssign assign = new tAssign();{
			ParameterContext params[] = srcAct.getParameters();
			
			if(params==null || params.length==0)
				assignUsed = false;
			else
			for(int i=0; i<params.length; i++){
				ParameterContext param = params[i];
				
				tCopy copy = new tCopy();{
					tFrom tfr = new tFrom();
					tfr.setvariable_Attribute(new XNCName(variableName));
					tfr.setpart_Attribute(new XNCName(/*param.getArgument()*/"in" + i));
					from fr = new from();
					fr.settFromComplexType(tfr);
					
					to tto = new to();
					tto.setvariable_Attribute(new XNCName(param.getVariable().getName()));
												
					copy.setfrom(fr);
					copy.setto(tto);
				}
				
				assign.addcopy(copy);
			}
		}
		//---- end of copy activity ----
		
		if(!assignUsed)
			return actGrp;

		actGrp = new activity();
		actGrp.setassignAsChoice(assign);
		
		destSeqAct.addactivity_Group(actGrp);

		activity resultAct = new activity();
		resultAct.setsequenceAsChoice(destSeqAct);

		return resultAct;
	}

}