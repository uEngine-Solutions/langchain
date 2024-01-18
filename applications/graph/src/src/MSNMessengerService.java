public class MSNMessengerService extends org.uengine.webservice.AbstractServiceProvider{

	public Object getStub(String endpoint) throws Exception{
		return (new org.uengine.webservices.msnmessenger.MSNMessengerServiceServiceLocator()).getMSNMessengerService(new java.net.URL(endpoint));

//		return null;
	}
}
