package org.uengine.kernel;

import org.metaworks.FieldDescriptor;
import org.metaworks.Type;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.GlobalContext;
import org.uengine.webservices.nateonmessenger.NateOnMessengerHelper;

import org.uengine.kernel.ProcessInstance;
import org.uengine.kernel.Role;
import org.uengine.kernel.RoleMapping;
import org.uengine.kernel.UEngineException;
import org.uengine.processdesigner.ProcessDesigner;

/**
 * @author Jinyoung Jang
 * @author Lee Hee-byung
 */

public class LocalNateOnMessengerActivity extends DefaultActivity{
	
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

	public LocalNateOnMessengerActivity(){
		setName("Messenger Activity");
	}

	public static String EXT_PROP_KEY_NateOnMessengerId = "EXT_PROP_KEY_NATEON_ID";
	
	public void executeActivity(ProcessInstance instance) throws Exception{
		final String actualContent = evaluateContent(instance, getContents()).toString();

		if (getToRole() == null) {
			throw new UEngineException("Receiver is not set.");
		}
			
		RoleMapping roleMapping = getToRole().getMapping(instance, getTracingTag());
		
		if (roleMapping == null) {
			throw new UEngineException("Actual target receiver is not set yet.");
		}

		 String nateOnId = roleMapping.getExtendedProperty(EXT_PROP_KEY_NateOnMessengerId);
		
		if (nateOnId == null) {
			roleMapping.fill(instance);
			nateOnId = roleMapping.getExtendedProperty(EXT_PROP_KEY_NateOnMessengerId);
		}
			
		if (nateOnId == null) {
			nateOnId = roleMapping.getEmailAddress();
		}
				
		if (nateOnId == null) {
			throw new Exception("There's no account information for instance messaging (Nate-on).");
		}
		
		final String _nateOnId = nateOnId;
		
		new Thread(new Runnable() {
			public void run() {
				(new NateOnMessengerHelper()).sendMessage(_nateOnId, actualContent);		
			}
		}).start();
		
		fireComplete(instance);
	}
}