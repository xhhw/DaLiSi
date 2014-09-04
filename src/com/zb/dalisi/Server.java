package com.zb.dalisi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class Server {
	private transient static Logger Log = Logger.getLogger(Server.class);

	public void run() {
		ServerSocket server = null;
		Socket you = null;
		String s = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		try {
			server = new ServerSocket(4331);
		} catch (IOException e) {
			Log.error(e.getMessage(), e);
		}
		while (true) {
			try {
				Log.debug("等待客户端指令");
				you = server.accept();
				out = new DataOutputStream(you.getOutputStream());
				in = new DataInputStream(you.getInputStream());
				while (true) {
					s = in.readUTF();
					int m = Integer.parseInt(s);
					out.writeUTF("你说的是" + m + "*2=" + 2 * m);
					Log.debug("收到的是" + m);
					Thread.sleep(500);
				}
			} catch (IOException e) {
				// StringWriter sw = new StringWriter();
				// PrintWriter pw = new PrintWriter(sw);
				// e.printStackTrace(pw);
				// Log.debug(sw.toString());
				Log.error(e.getMessage(), e);
			} catch (InterruptedException e) {
				Log.error(e.getMessage(), e);
			}
		}
	}
}
