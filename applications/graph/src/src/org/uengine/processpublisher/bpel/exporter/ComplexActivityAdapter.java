package org.uengine.processpublisher.bpel.exporter;
	
import org.uengine.components.serializers.BPELSerializer;
import org.uengine.kernel.ComplexActivity;

import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.groups.*;
import com.commerceone.xdk.castor.types.*;
import org.uengine.processpublisher.Adapter;
import java.util.*;
 
/**
 * @author Jinyoung Jang
 */
   
public class ComplexActivityAdapter implements Adapter{
	
	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		ComplexActivity srcAct = (ComplexActivity)src;
		
		tSequence destAct = new tSequence();
		destAct.setname_Attribute(new XNCName(BPELSerializer.toSafeName(srcAct.getName().getText(), "UnnamedBlock")));
		
		Vector childActivities = srcAct.getChildActivities();
		for(Enumeration enumeration = childActivities.elements(); enumeration.hasMoreElements();){
			Object item = (Object)enumeration.nextElement();
			Adapter adpt = ProcessDefinitionAdapter.getAdapter(item.getClass());
			if(adpt==null){
				continue;
			}
							
			activity actGrp_ = (activity)adpt.convert(item, keyedContext);
			destAct.addactivity_Group(actGrp_);
		}
				
		activity resultAct = new activity();
		resultAct.setsequenceAsChoice(destAct);
		
		return resultAct;
	}
}
