package org.uengine.processpublisher.bpel.exporter;
	
import org.uengine.kernel.Condition;
import org.uengine.kernel.Evaluate;
import org.uengine.kernel.Otherwise;
import org.uengine.kernel.SwitchActivity;

import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.groups.*;
import com.commerceone.xdk.castor.types.*;
import org.uengine.processpublisher.Adapter;
import java.util.*;
 
/**
 * @author Jinyoung Jang
 */
   
public class SwitchActivityAdapter implements Adapter{
	
	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		SwitchActivity srcAct = (SwitchActivity)src;

		tSwitch destAct = new tSwitch();
		destAct.setname_Attribute(new XNCName(srcAct.getName().getText().replace(' ', '_')));
		
		Vector childActivities = srcAct.getChildActivities();
		int i=0;
		for(Enumeration enumeration = childActivities.elements(); enumeration.hasMoreElements();){
			Object item = (Object)enumeration.nextElement();
			Adapter adpt = ProcessDefinitionAdapter.getAdapter(item.getClass());
			if(adpt==null){
				continue;
			}
							
			activity actGrp_ = (activity)adpt.convert(item, keyedContext);
			
			Condition condition = srcAct.getConditions()[i];
			if(condition instanceof Otherwise){
				tActivityContainer actContainer = new tActivityContainer();
				actContainer.setactivity_Group(actGrp_);
				destAct.setotherwise(actContainer);
			}else{
				case_ case__ = new case_();
				case__.setactivity_Group(actGrp_);			
				//review:
				tBoolean_expr booleanExp = new tBoolean_expr();
				
				if(condition instanceof Evaluate){
					Evaluate eval = ((Evaluate)condition);
					String key = eval.getKey();
					Object value = eval.getValue();
					String cond = eval.getCondition();
					
					booleanExp.set("bpws:getVariableData("+key+") " + cond + " " + value);
				}else
					booleanExp.set("MANUALLY");
				
				case__.setcondition_Attribute(booleanExp);
				destAct.addcase_(case__);
			}

			i++;
		}
				
		activity resultAct = new activity();
		resultAct.setswitch_AsChoice(destAct);
		
		return resultAct;
	}
}
