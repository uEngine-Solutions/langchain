/**
 * MSNMessengerServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.uengine.webservices.msnmessenger.impl;

import java.util.*;
import java.rmi.*;

import org.uengine.kernel.GlobalContext;

import rath.msnm.msg.MimeMessage;

import net.sf.jml.Email;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnList;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.impl.MsnMessengerFactory;


public class MSNMessengerServiceImpl implements org.uengine.webservices.msnmessenger.MSNMessengerService{
	final static String msnUserId = GlobalContext.getPropertyString("messengeractivity.msn.id", "uengine_82@hotmail.com");
	final static String password = GlobalContext.getPropertyString("messengeractivity.msn.password", "pongsor2");
	final static int loginRetryCnt = Integer.parseInt(GlobalContext.getPropertyString("messengeractivity.login.retry", "15"));
	final static int messageSendingRetryCnt = Integer.parseInt(GlobalContext.getPropertyString("messengeractivity.messagesending.retry", "5"));


	static boolean loggingin = false;

    public void sendMessage(java.lang.String in0, java.lang.String in1) throws RemoteException{
    
	System.out.println("to:"+in0);
	System.out.println("message:"+in1);    
	
		try{
			final Vector bLogin = new Vector();
			MsnMessenger messenger = null;
			
			if(!loggingin){
				if(messenger==null || !messenger.isLogIncoming()){
					messenger = MsnMessengerFactory.createMsnMessenger(msnUserId, password);
			        messenger.login();			        
			        loggingin = true;		        	
			        messenger.addListener(new MsnAdapter(){
						public void loginCompleted(MsnMessenger messenger) {
							bLogin.add("");
							loggingin = false;
						}
					});
				}else
					bLogin.add("");
			}
		         
			final String toEMailAddress = in0;
			final String messageContent = in1;
			
			boolean bSuccess = false;			
			
		
			for(int i=0; i<loginRetryCnt && bLogin.size()==0; i++){ //wait for msn login
				System.out.println("trying logging in...");
				Thread.sleep(5000);
			}	

			if(bLogin.size()==0){
				
				loggingin = false;
				throw new Exception("Couldn't log in the MSN.");
			}
			
			int retry=0;
			
			while(!bSuccess && retry < messageSendingRetryCnt){
				String friendLoginName = toEMailAddress;
				Email friendMail=Email.parseStr(friendLoginName);
				messenger.sendText(friendMail, messageContent);                
               
                System.out.println("Success");                
                bSuccess=true;
               
				try{
					Thread.sleep(5000);
				}catch(Exception e){}
					retry++;
                
			}
			
			if(retry == messageSendingRetryCnt)
				throw new RemoteException("MSNMessengerService: sending message failed");
				
		}catch(Exception e){
			throw new RemoteException("MSNMessengerService error", e);
		}
    }
    
   

}
