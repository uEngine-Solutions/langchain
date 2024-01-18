package org.uengine.processpublisher.invert.exporter;
	
import org.uengine.kernel.Activity;
import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.Role;
import org.uengine.kernel.SequenceActivity;

import org.uengine.processpublisher.Adapter;
import java.util.*;
 
/**
 * @author Jinyoung Jang
 */
   
public class ComplexActivityAdapter implements Adapter{
	
	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		ComplexActivity srcAct = (ComplexActivity)src;
		ComplexActivity dstAct = createDestinationActivity();
		Role role = (Role)keyedContext.get("role");		
						
		Vector childActivities = srcAct.getChildActivities();
		int i=0;
		for(Enumeration enumeration = childActivities.elements(); enumeration.hasMoreElements();){					
			Object item = (Object)enumeration.nextElement();
			Adapter adpt = ProcessDefinitionAdapter.getAdapter(item.getClass());
			if(adpt==null){
				continue;
			}
			
			Activity convertedAct = (Activity)adpt.convert(item, keyedContext);
			if(convertedAct!=null){
				dstAct.addChildActivity(convertedAct);
				i++;							
			}
		}		
		
		if(i==0) return null;
		if(i==1) return dstAct.getChildActivities().elementAt(0);
		
		return dstAct;
	}
	
	protected ComplexActivity createDestinationActivity(){
		return new SequenceActivity();
	}
}
