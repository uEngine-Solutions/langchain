package org.uengine.kernel;

import org.metaworks.FieldDescriptor;
import org.metaworks.Instance;
import org.metaworks.Type;
import org.metaworks.validator.Validator;
import org.uengine.processdesigner.DataTypeInput;
import org.uengine.processdesigner.ProcessDesigner;
import org.uengine.webservices.msnmessenger.impl.MSNMessengerServiceImpl;

/**
 * @author Jinyoung Jang
 */

public class LocalMessengerActivity extends DefaultActivity{
	
	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	public static void metaworksCallback_changeMetadata(Type type){
		FieldDescriptor fd;
		
		fd = type.getFieldDescriptor("Contents");

/*		fd.setValidators(new Validator[]{new Validator(){

			public String validate(Object arg0, Instance arg1) {
				String contents = (String)arg0;
				if(contents.length() > 30)
					return "30�� �̸�8�θ� ��۰����մϴ�";
				return null;
			}
			
			
		}});
*/		
		fd = type.getFieldDescriptor("ToRole");
		fd.setDisplayName("Recipient");
	}
	
	
	String contents;
		public String getContents() {
			return contents;
		}
		public void setContents(String value) {
			contents = value;
		}

	Role toRole;
		public Role getToRole() {
			return toRole;
		}
		public void setToRole(Role value) {
			toRole = value;
		}

	public LocalMessengerActivity(){
		setName("Messenger Activity");
	}

	public void executeActivity(ProcessInstance instance) throws Exception{
		String actualContent = evaluateContent(instance, getContents()).toString();

		if(getToRole()==null)
			throw new UEngineException("Receiver is not set.");
			
		RoleMapping roleMapping = getToRole().getMapping(instance, getTracingTag());
		
		if(roleMapping==null)
			throw new UEngineException("Actual target receiver is not set yet.");
		
		
    	do{	
			
			String instanceMessengerId = roleMapping.getInstanceMessengerId();
			
			if(instanceMessengerId==null){ //1. try to fill up
				roleMapping.fill(instance);
				instanceMessengerId = roleMapping.getInstanceMessengerId();
			}

			if(instanceMessengerId==null){ //2. try to use the mail address instead
				instanceMessengerId = roleMapping.getEmailAddress();
			}

			if(instanceMessengerId==null){ //3. try to use the endpoint instead
				instanceMessengerId = roleMapping.getEndpoint();
			}
			
			if(instanceMessengerId==null)
				throw new Exception("There's no account information for instance messaging.");
			
			(new MSNMessengerServiceImpl()).sendMessage(instanceMessengerId, actualContent);	
    		
    	}while(roleMapping.next());
		
		

		fireComplete(instance);
	}


}