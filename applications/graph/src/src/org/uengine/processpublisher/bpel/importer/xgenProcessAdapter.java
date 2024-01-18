package org.uengine.processpublisher.bpel.importer;

import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;

import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.groups.*;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.*;

import org.uengine.processpublisher.Adapter;
import java.util.*;

/**
 * @author Jinyoung Jang
 */

public class xgenProcessAdapter implements org.uengine.processpublisher.Adapter{

	static Hashtable adapters = new Hashtable();
	
	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		process srcProcDef = (process)src;				
		tProcess srcProc = srcProcDef.gettProcessComplexType();
		
		ProcessDefinition proc = new ProcessDefinition();
		
		proc.setName(srcProc.getname_Attribute().get());
		
		//proc.settargetNamespace_Attribute(new XAnyURI("urn:" + srcProcDef.getName().replace(' ', '_')));

		Vector childActivities = new Vector();
		activity srcChildActivity = srcProc.getactivity_Group();
		
		//getting variables
		{
			Vector pvdVector = new Vector();
		
			tVariables variables = srcProc.getvariables();
			for(Enumeration enumeration = variables.enumeratevariable(); enumeration.hasMoreElements();){
				ProcessVariable pvd = new ProcessVariable();
				tVariable var = (tVariable)enumeration.nextElement();
				pvd.setName(var.getname_Attribute().get());
				//pvd.setQName(var.gettype_Attribute().get());
				pvdVector.add(pvd);
			}
		
			ProcessVariable[] pvds = new ProcessVariable[pvdVector.size()];
			pvdVector.toArray(pvds);
			proc.setProcessVariables(pvds);			
		}
		//
		
		//setting roles
		{
			Vector roleVector = new Vector();

			tPartnerLinks partnerLinks = srcProc.getpartnerLinks();

			for(Enumeration enumeration = partnerLinks.enumeratepartnerLink(); enumeration.hasMoreElements();){
				Role role = new Role();
				tPartnerLink partnerLink = (tPartnerLink)enumeration.nextElement();
				role.setName(partnerLink.getname_Attribute().get());
				//pvd.setQName(var.gettype_Attribute().get());
				roleVector.add(role);
			}
		
			Role[] roles = new Role[roleVector.size()];
			roleVector.toArray(roles);
			proc.setRoles(roles);			
		}
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
		GlobalContext.deserialize(new java.io.FileInputStream(args[0]), "BPEL");
	}

}