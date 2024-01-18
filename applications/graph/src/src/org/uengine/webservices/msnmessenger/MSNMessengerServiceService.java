/**
 * MSNMessengerServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.uengine.webservices.msnmessenger;

public interface MSNMessengerServiceService extends javax.xml.rpc.Service {

	/**
	 * 
	 * @uml.property name="mSNMessengerServiceAddress"
	 */
	public java.lang.String getMSNMessengerServiceAddress();

	/**
	 * 
	 * @uml.property name="mSNMessengerService"
	 * @uml.associationEnd 
	 * @uml.property name="mSNMessengerService" multiplicity="(0 1)"
	 */
	public org
		.uengine
		.webservices
		.msnmessenger
		.MSNMessengerService getMSNMessengerService()
		throws javax.xml.rpc.ServiceException;

    public org.uengine.webservices.msnmessenger.MSNMessengerService getMSNMessengerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
