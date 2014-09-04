package com.zb.dalisi.frame;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zb.dalisi.xml.cfg.WebConfig;


public class XMLHelper {
	private static transient Log log = LogFactory.getLog(XMLHelper.class);
	
	private static XMLHelper instance = null;
	private static Boolean isInit = Boolean.FALSE;
	private static WebConfig webConfig = null;
	
	public static XMLHelper getInstance() throws Exception {
		if(isInit.equals(Boolean.FALSE)){
			synchronized (isInit) {
				if(isInit.equals(Boolean.FALSE)){
					webConfig = createWebConfig();
					isInit = Boolean.TRUE;
				}
			}
			instance = new XMLHelper();
		}
		return instance;
	}
	
	private static WebConfig createWebConfig() throws Exception {
		WebConfig rtn = null;
		String fileName = "dalisi-webconfig.xml";
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if (input == null){
			throw new RuntimeException("com.zb.dalisi.common.XMLHelper: input file null.");
		}
		
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("web-config", "com.zb.dalisi.xml.cfg.WebConfig");
		digester.addSetProperties("web-config");
		digester.addObjectCreate("web-config/transaction", "com.zb.dalisi.xml.cfg.Transaction");
		digester.addSetProperties("web-config/transaction");
		digester.addObjectCreate("web-config/transaction/session-class", "com.zb.dalisi.xml.cfg.SessionClass");
		digester.addSetProperties("web-config/transaction/session-class");
		digester.addObjectCreate("web-config/datasource", "com.zb.dalisi.xml.cfg.Datasource");
		digester.addSetProperties("web-config/datasource");
		digester.addObjectCreate("web-config/datasource/pool", "com.zb.dalisi.xml.cfg.Pool");
		digester.addSetProperties("web-config/datasource/pool");
		digester.addObjectCreate("web-config/datasource/pool/property", "com.zb.dalisi.xml.cfg.Property");
		digester.addSetProperties("web-config/datasource/pool/property");
		digester.addObjectCreate("web-config/caches", "com.zb.dalisi.xml.cfg.Caches");
		digester.addSetProperties("web-config/caches");
		digester.addObjectCreate("web-config/caches/cache", "com.zb.dalisi.xml.cfg.Cache");
		digester.addSetProperties("web-config/caches/cache");
		
		digester.addSetNext("web-config/transaction", "setTransaction", "com.zb.dalisi.xml.cfg.Transaction");
		digester.addSetNext("web-config/transaction/session-class", "setSessionClass", "com.zb.dalisi.xml.cfg.SessionClass");
		digester.addSetNext("web-config/datasource", "setDatasource", "com.zb.dalisi.xml.cfg.Datasource");
		digester.addSetNext("web-config/datasource/pool", "setPool", "com.zb.dalisi.xml.cfg.Pool");
		digester.addSetNext("web-config/datasource/pool/property", "addProperty", "com.zb.dalisi.xml.cfg.Property");
		digester.addSetNext("web-config/caches", "setCaches", "com.zb.dalisi.xml.cfg.Caches");
		digester.addSetNext("web-config/caches/cache", "addCache", "com.zb.dalisi.xml.cfg.Cache");
//		digester.addBeanPropertySetter("web-config/transaction");
//		digester.addBeanPropertySetter("web-config/transaction/session-class");
//		digester.addSetProperties("web-config/transaction", "session-class", "sessionClass");
//		digester.addSetProperty("web-config/transaction", "autocommit", "flag");


		rtn = (WebConfig) digester.parse(input);
		
		return rtn;
	}

	public WebConfig getWebConfig() {
		return webConfig;
	}
	
	public static void main(String[] args) {
		try {
			String name = XMLHelper.getInstance().getWebConfig().getTransaction().getSessionClass().getName();
			String autocommit = XMLHelper.getInstance().getWebConfig().getTransaction().getAutocommit();
			System.out.println(autocommit);
			System.out.println(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
