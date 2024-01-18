/**
 * MSNMessengerServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.uengine.webservices.msnmessenger;

public class MSNMessengerServiceServiceLocator extends org.apache.axis.client.Service implements org.uengine.webservices.msnmessenger.MSNMessengerServiceService {

    // Use to get a proxy class for MSNMessengerService
    private final java.lang.String MSNMessengerService_address = "http://localhost:8086/axis/services/MSNMessengerService";

    public java.lang.String getMSNMessengerServiceAddress() {
        return MSNMessengerService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MSNMessengerServiceWSDDServiceName = "MSNMessengerService";

	/**
	 * 
	 * @uml.property name="mSNMessengerServiceWSDDServiceName"
	 */
	public java.lang.String getMSNMessengerServiceWSDDServiceName() {
		return MSNMessengerServiceWSDDServiceName;
	}

	/**
	 * 
	 * @uml.property name="mSNMessengerServiceWSDDServiceName"
	 */
	public void setMSNMessengerServiceWSDDServiceName(java.lang.String name) {
		MSNMessengerServiceWSDDServiceName = name;
	}


    public org.uengine.webservices.msnmessenger.MSNMessengerService getMSNMessengerService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MSNMessengerService_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getMSNMessengerService(endpoint);
    }

    public org.uengine.webservices.msnmessenger.MSNMessengerService getMSNMessengerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.uengine.webservices.msnmessenger.MSNMessengerServiceSoapBindingStub _stub = new org.uengine.webservices.msnmessenger.MSNMessengerServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getMSNMessengerServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.uengine.webservices.msnmessenger.MSNMessengerService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.uengine.webservices.msnmessenger.MSNMessengerServiceSoapBindingStub _stub = new org.uengine.webservices.msnmessenger.MSNMessengerServiceSoapBindingStub(new java.net.URL(MSNMessengerService_address), this);
                _stub.setPortName(getMSNMessengerServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("MSNMessengerService".equals(inputPortName)) {
            return getMSNMessengerService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://msnmessenger.webservices.uengine.org", "MSNMessengerServiceService");
    }

	/**
	 * 
	 * @uml.property name="ports"
	 * @uml.associationEnd 
	 * @uml.property name="ports" multiplicity="(0 -1)" elementType="javax.xml.namespace.QName"
	 */
	private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("MSNMessengerService"));
        }
        return ports.iterator();
    }

}
