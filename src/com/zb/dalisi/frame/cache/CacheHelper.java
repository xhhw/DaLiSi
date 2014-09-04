package com.zb.dalisi.frame.cache;

import java.util.HashMap;

import com.zb.dalisi.frame.XMLHelper;
import com.zb.dalisi.xml.cfg.Cache;


public class CacheHelper {
	private static CacheHelper instance = null;
	private static Boolean isInit = Boolean.FALSE;
	private HashMap<Class<?>, Cacheable> caches = new HashMap<Class<?>, Cacheable>();
	
	private CacheHelper(){}
	
	public static CacheHelper getInstance() {
		if(isInit.equals(Boolean.FALSE)){
			synchronized (isInit) {
				if(isInit.equals(Boolean.FALSE)){
					isInit = Boolean.TRUE;
				}
			}
			instance = new CacheHelper();
		}
		return instance;
	}
	
	public void initFromXML() throws Exception {
		Cache[] caches = XMLHelper.getInstance().getWebConfig().getCaches().getCache();
		for(int i=0;i<caches.length;i++){
			Cacheable cacheable = (Cacheable)Class.forName(caches[i].getName()).newInstance();
			cacheable.load();
			this.caches.put(cacheable.getClass(), cacheable);
		}
	}
	
	public Cacheable getCache(Class<?> clazz){
		return this.caches.get(clazz);
	}
}
