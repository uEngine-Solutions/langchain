package org.uengine.webservices.nateonmessenger;

import java.io.IOException;

import kfmes.natelib.NateonMessenger;
import kfmes.natelib.msg.InstanceMessage;

import org.uengine.kernel.GlobalContext;

public class NateOnMessengerHelper {
	final static String nateOnSenderId = GlobalContext.getPropertyString("messengeractivity.nateon.id");
	final static String nateOnSenderPass = GlobalContext.getPropertyString("messengeractivity.nateon.password");
	
	public void sendMessage(java.lang.String nateOnReceiverId, java.lang.String content) {
		NateonMessenger nate = new NateonMessenger();
		
		if (!nate.isLoggedIn()) {
			nate.login(nateOnSenderId, nateOnSenderPass);
		}
		
		String requestAddMsg = "BPMS Administrator";
		nate.getNS().requestAdd(nateOnReceiverId, requestAddMsg.replaceAll(" ", "　").replaceAll("\n", "%0D%0A"));
		
		try {
			nate.sendIMessage(new InstanceMessage(nateOnSenderId, nateOnReceiverId, content));
			Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		}
		
		if (nate.isLoggedIn()) {
			try {
				nate.logout();
			} catch (IOException e) {
			}
		}
		
	}
}
