package com.zb.dalisi.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceManager {
	private static transient Log log = LogFactory.getLog(ServiceManager.class);
	private static ThreadLocal<Session> s_session = new ThreadLocal<Session>();
	
	public static Session getSession(){
		Session sessionContext = (Session) s_session.get();
        // 如果session没有，则新建一个session
        if (sessionContext == null) {
        	sessionContext = new SessionContext();
        	s_session.set(sessionContext); //将新开的session保存到线程局部变量中
        }
        return sessionContext;
	}

}
