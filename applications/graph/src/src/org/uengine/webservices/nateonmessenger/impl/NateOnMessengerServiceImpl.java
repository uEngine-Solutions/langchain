/**
 * MSNMessengerServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.uengine.webservices.nateonmessenger.impl;

import java.util.*;
import java.rmi.*;

import org.uengine.kernel.GlobalContext;

import rath.msnm.entity.MsnFriend;  
import rath.msnm.event.MsnAdapter;  
import rath.msnm.msg.MimeMessage;  
import rath.msnm.UserStatus;  
import rath.msnm.MSNMessenger;  

public class NateOnMessengerServiceImpl implements org.uengine.webservices.msnmessenger.MSNMessengerService{
	final static String msnUserId = GlobalContext.getPropertyString("messengeractivity.msn.id", "uengine_82@hotmail.com");
	final static String password = GlobalContext.getPropertyString("messengeractivity.msn.password", "pongsor2");
	final static int loginRetryCnt = Integer.parseInt(GlobalContext.getPropertyString("messengeractivity.login.retry", "15"));
	final static int messageSendingRetryCnt = Integer.parseInt(GlobalContext.getPropertyString("messengeractivity.messagesending.retry", "5"));

	static MSNMessenger msn;
	static boolean loggingin = false;

    public void sendMessage(java.lang.String in0, java.lang.String in1) throws RemoteException{
    
	System.out.println("to:"+in0);
	System.out.println("message:"+in1);    
		try{
			final Vector bLogin = new Vector();
			
			if(!loggingin){
				if(msn==null || !msn.isLoggedIn()){
					msn = new MSNMessenger(msnUserId, password);
			
		        	msn.setInitialStatus( UserStatus.ONLINE );  
		        	msn.login();
		        	loggingin = true;
		        	
					msn.addMsnListener(new MsnAdapter(){
						public void loginComplete(MsnFriend friend){
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
				msn = null;
				loggingin = false;
				throw new Exception("Couldn't log in the MSN.");
			}
			
			int retry=0;
			while(!bSuccess && retry < messageSendingRetryCnt){
		
				String friendLoginName = toEMailAddress;
		
                msn.doCallWait(friendLoginName);  
                MimeMessage sendMessage = new MimeMessage(messageContent); //�޼��� ����
                sendMessage.setKind(MimeMessage.KIND_MESSAGE);
                if (msn.sendMessage(friendLoginName , sendMessage) == true )   //�޼��� �����ºκ�
                { 
                        System.out.println("Success");
                        
                        bSuccess=true;
                } 
                else  
                { 
                        System.out.println("Failed");
			
				try{
					Thread.sleep(5000);
				}catch(Exception e){}
					retry++;
                } 
                
			}
			
			if(retry == messageSendingRetryCnt)
				throw new RemoteException("MSNMessengerService: sending message failed");
				
		}catch(Exception e){
			throw new RemoteException("MSNMessengerService error", e);
		}
    }
    
    public static void main(String [] args) throws Exception{
    	
    }

}
