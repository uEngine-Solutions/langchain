package org.uengine.util.ftp;

import sun.net.ftp.*; 
import sun.net.*; 
import java.io.*; 

/**
 * @author Jinyoung Jang
 */

public class Uploader extends FtpClient{ 
	public Uploader(){
		super();
	}
	
	public void connect(String server, String user, String pass) throws Exception{
      	openServer(server, 21);
		login(user, pass); 
	}	
	
	public void uploadFile(String fileName, String directory) throws Exception{
		uploadFile(new File(fileName), directory);
	}
	
	public void uploadFile(File file, String directory) throws Exception{
		uploadFile(new FileInputStream(file), directory);
	}

	public void uploadFile(InputStream is, String directory) throws Exception{
		binary();

		byte[] bytes = new byte[1024]; 
		int c; 
		int total_bytes=0; 
			
		TelnetOutputStream tos = put(directory); 
			
		while((c=is.read(bytes)) !=-1) 
		{ 
				total_bytes +=c; 
				tos.write(bytes,0,c); 
		} 
			
		tos.close(); 
		is.close();
	} 
	
	public static void main(String args[]) throws Exception{ 
		Uploader uploader = new Uploader();
		
		uploader.connect("192.168.0.7", "seoyd", "s09043");
//		uploader.cd("..");
		uploader.uploadFile("c:/ISLOG.TXT", "abc.txt"); 
	} 
} 

