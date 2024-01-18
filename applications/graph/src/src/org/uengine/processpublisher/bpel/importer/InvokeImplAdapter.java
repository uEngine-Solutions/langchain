/*
 * Created on 2004-05-08
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.uengine.processpublisher.bpel.importer;

import org.uengine.processpublisher.Adapter;
import org.uengine.kernel.WebServiceActivity;

import org.uengine.smcp.twister.engine.priv.core.definition.impl.*;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InvokeImplAdapter implements Adapter{

	public Object convert(Object src, java.util.Hashtable keyedContext) throws Exception{
		InvokeImpl srcAct = (InvokeImpl)src;
		
		WebServiceActivity destAct = new WebServiceActivity();
		destAct.setName(srcAct.getName());
		
		return destAct;
	}	
}
