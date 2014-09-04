package com.zb.dalisi.xml.cfg;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
//	private List list = new ArrayList();
	private String autocommit = null;
	private String flag = null;
	private SessionClass sessionClass = null;
//	public void addSessionClass(SessionClass sessionClass) {
//		list.add(sessionClass);
//	}
//	public SessionClass[] getSessionClasses() {
//		return ((SessionClass[])(SessionClass[])this.list.toArray(new SessionClass[0]));
//	}
	public String getAutocommit() {
		return autocommit;
	}
	public void setAutocommit(String autocommit) {
		this.autocommit = autocommit;
	}
	public SessionClass getSessionClass() {
		return sessionClass;
	}
	public void setSessionClass(SessionClass sessionClass) {
		this.sessionClass = sessionClass;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
