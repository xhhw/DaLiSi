package com.zb.dalisi.frame;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionContext implements Session {
	private static transient Log log = LogFactory.getLog(SessionContext.class);
	
	private static ThreadLocal<ThreadInfo> tx = new ThreadLocal<ThreadInfo>();
	HashMap txConnections = new HashMap();
	
	public boolean isStartTransaction(){
		return (getThreadInfo() != null);
	}

	@Override
	public void startTransaction() throws Exception {
		// TODO Auto-generated method stub
		if (isStartTransaction()){
			throw new RuntimeException("com.zb.dalisi.common.SessionContext: transaction exist.");
		}
		setThreadInfo(new ThreadInfo());
	}
	
	private void setThreadInfo(ThreadInfo objThreadInfo){
		tx.set(objThreadInfo);
	}
	
	private ThreadInfo getThreadInfo(){
		return ((ThreadInfo)tx.get());
	}

	@Override
	public void commitTransaction() throws Exception {
		// TODO Auto-generated method stub
		if (!isStartTransaction()){
			throw new RuntimeException("com.zb.dalisi.common.SessionContext: transaction not exist.");
		}
		ThreadInfo objThreadInfo = getThreadInfo();
		if (objThreadInfo.txConnections.size() != 0) {
			HashMap map = objThreadInfo.txConnections;
			Set sets = map.keySet();
			for (Iterator iter = sets.iterator(); iter.hasNext(); ) {
				String ds = (String)iter.next();
				Connection conn = (Connection)map.get(ds);
				try {
					conn.commit();
				}catch (Throwable ex) {
//					conn.rollback();
				}
			}
		}
		setThreadInfo(null);
	}

	@Override
	public void rollbackTransaction() throws Exception {
		// TODO Auto-generated method stub
		if (!isStartTransaction()){
			throw new RuntimeException("com.zb.dalisi.common.SessionContext: transaction not exist.");
		}
		ThreadInfo objThreadInfo = getThreadInfo();
		if (objThreadInfo.txConnections.size() != 0) {
			HashMap map = objThreadInfo.txConnections;
			Set sets = map.keySet();
			for (Iterator iter = sets.iterator(); iter.hasNext(); ) {
				String ds = (String)iter.next();
				Connection conn = (Connection)map.get(ds);
				try {
					conn.rollback();
				}catch (Throwable ex) {
				}
			}
		}
		setThreadInfo(null);
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		Connection ret = null;
		String ds = "res";
		if (isStartTransaction()) {
			ThreadInfo threadInfo = getThreadInfo();
			if (threadInfo.txConnections.containsKey(ds)) {
				ret = new LogicConnection((Connection) threadInfo.txConnections.get(ds));
			} else {
				Connection objConnection = null;//DataSourceFactory.getDataSource().getConnectionFromDataSource(ds);
				threadInfo.txConnections.put(ds, objConnection);
				ret = new LogicConnection((Connection) threadInfo.txConnections.get(ds));
			}
		}
		return ret;
	}
	
	private static class ThreadInfo {
		HashMap txConnections = new HashMap();
		
	}

}
