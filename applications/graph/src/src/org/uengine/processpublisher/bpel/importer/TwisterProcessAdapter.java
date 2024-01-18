/*
 * Created on 2004-05-08
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.uengine.processpublisher.bpel.importer;

import org.uengine.processpublisher.Adapter;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;

import org.uengine.smcp.twister.TwisterBPELToolkit;
import org.uengine.smcp.twister.engine.priv.core.definition.*;
import java.util.*;


/**
 * @author Jinyoung Jang
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TwisterProcessAdapter implements Adapter{

	static Hashtable adapters = new Hashtable();
	
	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		TwisterProcess srcProcDef = (TwisterProcess)src;				
		ProcessDefinition proc = new ProcessDefinition();
		
		proc.setName(srcProcDef.getName());
		
		Vector childActivities = new Vector();
		org.uengine.smcp.twister.engine.priv.core.definition.Activity activity = srcProcDef.getActivity();
		if(activity instanceof Sequence){
			Sequence seq = (Sequence)activity;
			List childList = seq.getActivities();
			
			for(Iterator iter = childList.iterator(); iter.hasNext(); ){
				activity = (org.uengine.smcp.twister.engine.priv.core.definition.Activity)iter.next();
				
				try{
					org.uengine.kernel.Activity destAct = 
						(org.uengine.kernel.Activity)getAdapter(activity.getClass()).convert(activity, null);
					proc.addChildActivity(destAct);	
				}catch(Exception e){
					
				}				
			}			
		}else{
			org.uengine.kernel.Activity destAct = 
								(org.uengine.kernel.Activity)getAdapter(activity.getClass()).convert(activity, null);
			proc.addChildActivity(destAct);
		}	
				
		//getting variables
		{
			Vector pvdVector = new Vector();
		
			for(Iterator iter = srcProcDef.getVariables().iterator(); iter.hasNext();){
				Variable var = (Variable)iter.next();
				
				ProcessVariable pvd = new ProcessVariable();
				
				pvd.setName(var.getName());
				//pvd.setQName(var.gettype_Attribute().get());
				pvdVector.add(pvd);
			}
		
			ProcessVariable[] pvds = new ProcessVariable[pvdVector.size()];
			pvdVector.toArray(pvds);
			proc.setProcessVariables(pvds);			
		}
		//
		
		//setting roles
		/*{
			Vector roleVector = new Vector();

			tPartnerLinks partnerLinks = srcProc.getpartnerLinks();

			for(Enumeration enum = partnerLinks.enumeratepartnerLink(); enum.hasMoreElements();){
				Role role = new Role();
				tPartnerLink partnerLink = (tPartnerLink)enum.nextElement();
				role.setName(partnerLink.getname_Attribute().get());
				//pvd.setQName(var.gettype_Attribute().get());
				roleVector.add(role);
			}
		
			Role[] roles = new Role[roleVector.size()];
			roleVector.toArray(roles);
			proc.setRoles(roles);			
		}*/
		//
				
		return proc;
	}
	
	protected static Adapter getAdapter(Class activityType){
		if(adapters.containsKey(activityType))
			return (Adapter)adapters.get(activityType);
		
		Adapter adapter = null;
		do{	
			try{
				String activityTypeName = org.uengine.util.UEngineUtil.getClassNameOnly(activityType);
System.out.println("activityTypeName:"+"org.uengine.processpublisher.bpel.importer." + activityTypeName + "Adapter");			
				adapter = (Adapter)Class.forName("org.uengine.processpublisher.bpel.importer." + activityTypeName + "Adapter").newInstance();
				
				adapters.put(activityType, adapter);
			}catch(Exception e){
				activityType = activityType.getSuperclass();
			}
System.out.println("activityType:"+activityType);
		}while(adapter==null && activityType!=Object.class);

		if(adapter==null)			
			System.out.println("ProcessDefinitionAdapter::getAdapter : can't find adapter for " + activityType);
			
		return adapter;
	}
	
	public static void main(String [] args) throws Exception{
		org.smartcomps.twister.common.configuration.XMLConfigurationReader.loadConfiguration();
		TwisterBPELToolkit ttk = new TwisterBPELToolkit();
		TwisterProcess tp = ttk.read(new java.io.FileInputStream(args[0]), new java.io.FileInputStream(args[1]));
		
		System.out.println(tp);
		
	}

}
