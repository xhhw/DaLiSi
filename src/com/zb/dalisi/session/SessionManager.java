package com.zb.dalisi.session;

public class SessionManager {
	private final static ThreadLocal<Session> session = new ThreadLocal<Session>();
	
	public static Session getSession(){
		Session context = (Session) session.get();
        // 如果session没有，则新建一个session
        if (context == null) {
        	context = new Session();
        	session.set(context); //将新开的session保存到线程局部变量中
        }
        return context;
	}
	
	public static void endSession(){
        session.remove();
	}

}
