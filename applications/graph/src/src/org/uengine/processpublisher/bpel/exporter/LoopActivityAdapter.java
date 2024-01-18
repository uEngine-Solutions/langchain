package org.uengine.processpublisher.bpel.exporter;
	
import org.uengine.kernel.LoopActivity;

import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.groups.*;
import com.commerceone.xdk.castor.types.*;
import org.uengine.processpublisher.Adapter;
import java.util.*;
 
/**
 * @author Jinyoung Jang
 */
   
public class LoopActivityAdapter implements Adapter{
	
	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		LoopActivity srcAct = (LoopActivity)src;
		
		tWhile destAct = new tWhile();
		destAct.setname_Attribute(new XNCName(srcAct.getName().getText().replace(' ', '_')));
		
		//review:
		tBoolean_expr booleanExp = new tBoolean_expr();
		booleanExp.set("boolean exp");
		destAct.setcondition_Attribute(booleanExp);
		
		tSequence sequence_ = new tSequence();
		
		Vector childActivities = srcAct.getChildActivities();
		for(Enumeration enumeration = childActivities.elements(); enumeration.hasMoreElements();){
			Object item = (Object)enumeration.nextElement();
			Adapter adpt = ProcessDefinitionAdapter.getAdapter(item.getClass());
			if(adpt==null){
				continue;
			}
							
			activity actGrp_ = (activity)adpt.convert(item, null);
			sequence_.addactivity_Group(actGrp_);
		}
		
		activity sequence = new activity();
		sequence.setsequenceAsChoice(sequence_);
				
		destAct.setactivity_Group(sequence);
				
		activity resultAct = new activity();
		resultAct.setwhile_AsChoice(destAct);
		
		return resultAct;
	}
}
