package com.zb.dalisi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class Client {
	private transient static Logger Log = Logger.getLogger(Client.class);
	
	public static void main(String[] args) {
		String s = null;
		Socket my = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		try {
			my = new Socket("127.0.0.1", 4331);
			in = new DataInputStream(my.getInputStream());
			out = new DataOutputStream(my.getOutputStream());
			for (int i=1;i<10;i=i+2){
				out.writeUTF(""+i);
				s = in.readUTF();
				System.out.println("客户端收到"+s);
//				Log.debug("客户端收到"+s);
				Thread.sleep(500);
			}
//			my.close();
		} catch (UnknownHostException e) {
			Log.error(e.getMessage(),e);
		} catch (IOException e) {
			Log.error(e.getMessage(),e);
		} catch (InterruptedException e) {
			Log.error(e.getMessage(),e);
		}
	}
}
