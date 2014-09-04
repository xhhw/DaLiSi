package com.zb.dalisi.session;

public class SessionManager {
	private final static ThreadLocal<Session> session = new ThreadLocal<Session>();
	
	public static Session getSession(){
		Session context = (Session) session.get();
        // ���sessionû�У����½�һ��session
        if (context == null) {
        	context = new Session();
        	session.set(context); //���¿���session���浽�ֲ߳̾�������
        }
        return context;
	}
	
	public static void endSession(){
        session.remove();
	}

}
