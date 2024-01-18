package org.uengine.processpublisher.bpel.exporter;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.uengine.components.serializers.BPELSerializer;
import org.uengine.kernel.ProcessDefinition;
import org.uengine.kernel.ProcessVariable;
import org.uengine.kernel.Role;
import org.uengine.processpublisher.Adapter;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.process;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.groups.activity;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tCorrelationSet;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tCorrelationSets;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tPartnerLink;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tPartnerLinks;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tProcess;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tSequence;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tVariable;
import org.xmlsoap.schemas.ws.n2003.n03.business_process.types.tVariables;

import com.commerceone.xdk.castor.types.XAnyURI;
import com.commerceone.xdk.castor.types.XNCName;
import com.commerceone.xdk.castor.types.XQName;

/**
 * @author Jinyoung Jang
 */

public class ProcessDefinitionAdapter implements org.uengine.processpublisher.Adapter{

	static Hashtable adapters = new Hashtable();
	
	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		ProcessDefinition srcProcDef = (ProcessDefinition)src;
		
		tProcess proc = new tProcess();
		String srcProcName = BPELSerializer.toSafeName(srcProcDef.getName().getText(), "Process");
		proc.setname_Attribute(new XNCName(srcProcName));
		proc.settargetNamespace_Attribute(new XAnyURI("urn:" + srcProcName));
		
		Hashtable context = new Hashtable();
		context.put("addVariables", new Vector());

		activity actGrp = new activity();
			tSequence seq = new tSequence();
			seq.setname_Attribute(new XNCName("_main_sequence"));
			
			Vector childActivities = srcProcDef.getChildActivities();
			for(Enumeration enumeration = childActivities.elements(); enumeration.hasMoreElements();){
				Object item = (Object)enumeration.nextElement();
				Adapter adpt = getAdapter(item.getClass());
				if(adpt==null){
					continue;
				}
								
				activity actGrp_ = (activity)adpt.convert(item, context);
				seq.addactivity_Group(actGrp_);
			}		
			
			actGrp.setsequenceAsChoice(seq);
		proc.setactivity_Group(actGrp);	
		
		//setting variables
		tVariables variables = new tVariables();
		
		ProcessVariable pvds[] = srcProcDef.getProcessVariables();
		for(int i=0; i<pvds.length; i++){
			ProcessVariable pvd = pvds[i];
			tVariable variable = new tVariable();
			variable.setname_Attribute(new XNCName(BPELSerializer.toSafeName(pvd.getName(), "variable"+i)));
			
			//review: should be xsi type
			variable.settype_Attribute(getXSDTypeName(pvd));		
			variables.addvariable(variable);
		}
		
		Vector addVariables = (Vector)context.get("addVariables");
		for(Enumeration enumeration = addVariables.elements(); enumeration.hasMoreElements(); ){
			Object [] varCtx = (Object[])enumeration.nextElement();
			
			String variableName = (String)varCtx[0];
			XQName variableType = (XQName)varCtx[1];
			 
			tVariable variable = new tVariable();
			variable.setname_Attribute(new XNCName(variableName) );
			
			//review: should be xsi type
			variable.setmessageType_Attribute(variableType);		
			variables.addvariable(variable);
		}
		
		proc.setvariables(variables);			
		//
		
		//setting partners
		tPartnerLinks partnerLinks = new tPartnerLinks();
		
		Role roles[] = srcProcDef.getRoles();
		for(int i=0; i<roles.length; i++){
			Role role = roles[i];
			tPartnerLink partnerLink = new tPartnerLink();
			String roleName = BPELSerializer.toSafeName(role.getName(), "role"+i);			
			partnerLink.setname_Attribute(new XNCName(roleName));
			partnerLink.setpartnerLinkType_Attribute(new XQName("sdns", "", roleName + "PartnerLinkType"));
			partnerLink.setpartnerRole_Attribute(new XNCName("partnerRole"));
			partnerLinks.addpartnerLink(partnerLink);
		}
		//add my role
		tPartnerLink partnerLink = new tPartnerLink();
		partnerLink.setname_Attribute(new XNCName("myRole") );
		partnerLink.setpartnerLinkType_Attribute(new XQName("sdns", "", srcProcName + "PartnerLinkType"));
		partnerLink.setpartnerRole_Attribute(new XNCName("myRole"));
		partnerLinks.addpartnerLink(partnerLink);
		//
		
		proc.setpartnerLinks(partnerLinks);		
		//
		
		//setting correlations		
		tCorrelationSets correlationSets = new tCorrelationSets();
		for(int i=0; i<roles.length; i++){
			Role role = roles[i];
			ProcessVariable identifier = role.getIdentifier();
			if(identifier==null) identifier = srcProcDef.getProcessVariables()[0];//continue;
			
			tCorrelationSet correlationSet = new tCorrelationSet();
			correlationSet.setname_Attribute(new XNCName(BPELSerializer.toSafeName(role.getName(), "role"+i)+"Identification"));
			//review:
			correlationSet.setproperties_Attribute(new XQName("sdns","","property_" + identifier.getName()));
			correlationSets.addcorrelationSet(correlationSet);
		}
		proc.setcorrelationSets(correlationSets);
		//		
		
		process _proc = new process();
		_proc.settProcessComplexType(proc);
		_proc.addPrefixDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
		_proc.addPrefixDeclaration("sdns", "http://uengine.org/wsdl/" + srcProcName);
		_proc.addPrefixDeclaration("bpws", "http://schemas.xmlsoap.org/ws/2003/03/business-process/");


		return _proc;
	}
	
	protected static Adapter getAdapter(Class activityType){
		if(adapters.containsKey(activityType))
			return (Adapter)adapters.get(activityType);
		
		Adapter adapter = null;
		do{	
			try{
				String activityTypeName = org.uengine.util.UEngineUtil.getClassNameOnly(activityType);
System.out.println("activityTypeName:"+"org.uengine.processpublisher.bpel.exporter." + activityTypeName + "Adapter");			
				adapter = (Adapter)Class.forName("org.uengine.processpublisher.bpel.exporter." + activityTypeName + "Adapter").newInstance();
				
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
///		ProcessDefinition def = ProcessDefinitionFactory.getDefinition(new java.io.FileInputStream(args[0]));
//		GlobalContext.serialize(def, System.out, "BPEL");
	}
	
	public static XQName getXSDTypeName(ProcessVariable pv) throws Exception{
		Class type = pv.getType();
				
		if(pv.getQName()!=null)
			return new XQName("MANUALLY", pv.getQName().getNamespaceURI(), pv.getQName().getLocalPart());		
		else{
			String xsdTypeName = "string";
			if(type == String.class)
				xsdTypeName = "string";
			else if(type == Integer.class)
				xsdTypeName = "int";
			else if(type == Boolean.class)
				xsdTypeName = "boolean";
			else if(type == Calendar.class)
				xsdTypeName = "dateTime";
				
			return new XQName("xsd", "", xsdTypeName);
		}		 
	} 

}