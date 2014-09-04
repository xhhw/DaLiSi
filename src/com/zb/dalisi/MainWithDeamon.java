package com.zb.dalisi;

import java.util.TimerTask;

import org.apache.log4j.Logger;


public class MainWithDeamon {
	private transient static Logger Log = Logger.getLogger(MainWithDeamon.class);
	
	public void reload(){
		Log.debug("ThreadId="+Thread.currentThread().getId());
		Log.debug("***********##reload is called**************************");
	}
	
	public void reloadCommand(){
		Log.debug("ThreadId="+Thread.currentThread().getId());
		Log.debug("***********##reloadCommand is called**************************");
		final TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Log.debug("ThreadId="+Thread.currentThread().getId());
//				reload();
				new Server().run();
			}
        };
        TaskEngine.getInstance().schedule(task, 100);
	}
	
	public void testPeriodTask(){
		final TimerTask task = new TimerTask(){
			@Override
			public void run() {
				Log.debug("==================ThreadId="+Thread.currentThread().getId());
			}
		};
//		TaskEngine.getInstance().schedule(task, 300, 300);
		TaskEngine.getInstance().schedule(task, 300);
	}
	
	public void deamonThreadCreate(){
		Thread deamonThread = new Thread(new Runnable() {
			public void run() {
				while(true){
					Log.debug("*****ThreadId="+Thread.currentThread().getId());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						Log.error(e.getMessage(),e);
					}
				}
			}
		});
		deamonThread.setDaemon(true);
		deamonThread.start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		new MainWithDeamon().deamonThreadCreate();
		new MainWithDeamon().reloadCommand();
//		new Server().run();
		
//		new MainWithDeamon().testPeriodTask();
		while(true){
			Log.debug("**主线程执行***ThreadId="+Thread.currentThread().getId());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				Log.error(e.getMessage(),e);
			}
		}
	}

}
