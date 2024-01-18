package org.uengine.processpublisher.bpel.exporter;

import org.uengine.components.serializers.BPELSerializer;
import org.uengine.kernel.ParameterContext;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.WebServiceActivity;

import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.groups.*;
import com.commerceone.xdk.castor.types.*;
import org.uengine.processpublisher.Adapter;
import javax.wsdl.*;
import java.util.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.*;

	 
/**
 * @author Jinyoung Jang
 */
    
public class WebServiceActivityAdapter implements Adapter{

	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		WebServiceActivity srcWebServiceActivity = (WebServiceActivity)src;
		
		tSequence destSeqAct = new tSequence();
		destSeqAct.setname_Attribute(new XNCName(srcWebServiceActivity.getName().getText().replace(' ', '_') + "Sequence"));
		
		String targetNS = srcWebServiceActivity.getService().getDefinition().getTargetNamespace();
		String nsPrefix = BPELSerializer.toSafeName(srcWebServiceActivity.getService().getName(), null);
		
		String variableName = srcWebServiceActivity.getOperationName() + "Request";{
			Vector addVariables = (Vector)keyedContext.get("addVariables");
			XQName xqName = new XQName(nsPrefix, targetNS, variableName);
			variableName = "var_" + variableName; 
			addVariables.add(new Object[]{variableName, xqName});			
		}
		String outVariableName = srcWebServiceActivity.getOperationName() + "Response";{
			Vector addVariables = (Vector)keyedContext.get("addVariables");
			XQName xqName = new XQName(nsPrefix, targetNS, outVariableName);
			outVariableName = "var_" + outVariableName; 
			addVariables.add(new Object[]{outVariableName, xqName});			
		}
																
		//---- build copy activity ----
		boolean assignUsed = true;
		tAssign assign = new tAssign();{
			ParameterContext params[] = new ParameterContext[srcWebServiceActivity.getParameters().length];
			System.arraycopy(srcWebServiceActivity.getParameters(), 0, params, 0, srcWebServiceActivity.getParameters().length);
			
			if(params==null || params.length==0)
				assignUsed = false;
			else
			for(int i=0; i<params.length; i++){
				ParameterContext param = params[i];
				
				tCopy copy = new tCopy();{
					tFrom tfr = new tFrom();
					tfr.setvariable_Attribute(new XNCName(BPELSerializer.toSafeName(param.getVariable().getName(), "variable" + i)));
					from fr = new from();
					fr.settFromComplexType(tfr);
					
					to tto = new to();
					tto.setvariable_Attribute(new XNCName(BPELSerializer.toSafeName(variableName, null)));
					tto.setpart_Attribute(new XNCName(BPELSerializer.toSafeName(param.getArgument().getText(), null)));
												
					copy.setfrom(fr);
					copy.setto(tto);
				}
				
				assign.addcopy(copy);
			}
		}
		activity actGrp = new activity();
		actGrp.setassignAsChoice(assign);
		//---- end of copy activity ----
		
		destSeqAct.addactivity_Group(actGrp);
		
		//---- build invoke activity ----		
		tInvoke invoke = new tInvoke();{
			invoke.setname_Attribute(new XNCName(BPELSerializer.toSafeName(srcWebServiceActivity.getName().getText(), "Invoke")));

			//set inputVariable			
			try{
/*				ServiceDefinition sd = srcWebServiceActivity.getService();
				PortType pt = (PortType)sd.getDefinition().getPortTypes().values().iterator().next();
*/				
				
				invoke.setinputVariable_Attribute(new XNCName(variableName));
			}catch(Exception e){
				//invocation with null parameter
			}

			//set outputVariable
			invoke.setoutputVariable_Attribute(new XNCName(outVariableName));
			
			//set correlation if needed				
			boolean initiateCorrelation = false;
			boolean inboundPattern = false;
			String roleName = srcWebServiceActivity.getRole().getName();
			ProcessVariable identifier = srcWebServiceActivity.getRole().getIdentifier();
			if(identifier!=null){
				ParameterContext params[] = new ParameterContext[srcWebServiceActivity.getParameters().length];
				System.arraycopy(srcWebServiceActivity.getParameters(), 0, params, 0, srcWebServiceActivity.getParameters().length);
		
				if(params!=null && params.length>0)
				for(int i=0; i<params.length; i++){
					if(params[i].equals(identifier)){
						initiateCorrelation = true;
						break;
					}
				}
			}
			if(!initiateCorrelation && srcWebServiceActivity.getOutput()!=null){	//next, test the output variable
				if(srcWebServiceActivity.getOutput().equals(identifier)){
					initiateCorrelation = true;
					inboundPattern = true;
				}				
			}
				
			if(initiateCorrelation){
				tCorrelationsWithPattern correlations = new tCorrelationsWithPattern();
				tCorrelationWithPattern correlation = new tCorrelationWithPattern();
				correlation.setinitiate_Attribute((inboundPattern ? tBoolean.YES:tBoolean.NO));
				correlation.setset_Attribute(new XNCName(roleName + "Identification"));
				correlation.setpattern_Attribute(new XString((inboundPattern ? "in":"out")));
				correlations.addcorrelation(correlation);
				invoke.setcorrelations(correlations);		
			}
			//
				
			invoke.setoperation_Attribute(new XNCName(srcWebServiceActivity.getOperationName()));
			invoke.setportType_Attribute(new XQName(nsPrefix, targetNS, srcWebServiceActivity.getPortType()));
			invoke.setpartnerLink_Attribute(new XNCName(srcWebServiceActivity.getRole().getName().replace(' ', '_')));
		}
		actGrp = new activity();
		actGrp.setinvokeAsChoice(invoke);
		//---- end of invoke activity ---- 
		
		if(assignUsed)
			destSeqAct.addactivity_Group(actGrp);
			
		if(srcWebServiceActivity.getOutput()!=null){
			String src_outVarName = srcWebServiceActivity.getOutput().getName();
			
			tAssign outAssign = new tAssign();{
				tCopy copy = new tCopy();{
					tFrom tfr = new tFrom();
					tfr.setvariable_Attribute(new XNCName(outVariableName));
					tfr.setpart_Attribute(new XNCName("in0"));
					from fr = new from();
					fr.settFromComplexType(tfr);
				
					to tto = new to();
					tto.setvariable_Attribute(new XNCName(src_outVarName));
											
					copy.setfrom(fr);
					copy.setto(tto);
				}
			
				outAssign.addcopy(copy);
			}
			
			actGrp = new activity();
			actGrp.setassignAsChoice(outAssign);
			destSeqAct.addactivity_Group(actGrp);
			
			assignUsed = true;
		}
			
		if(!assignUsed)
			return actGrp;
		
		actGrp = new activity();
		actGrp.setsequenceAsChoice(destSeqAct);
	
		return actGrp;		
	}

}