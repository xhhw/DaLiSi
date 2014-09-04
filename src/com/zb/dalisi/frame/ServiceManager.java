package com.zb.dalisi.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceManager {
	private static transient Log log = LogFactory.getLog(ServiceManager.class);
	private static ThreadLocal<Session> s_session = new ThreadLocal<Session>();
	
	public static Session getSession(){
		Session sessionContext = (Session) s_session.get();
        // ���sessionû�У����½�һ��session
        if (sessionContext == null) {
        	sessionContext = new SessionContext();
        	s_session.set(sessionContext); //���¿���session���浽�ֲ߳̾�������
        }
        return sessionContext;
	}

}
