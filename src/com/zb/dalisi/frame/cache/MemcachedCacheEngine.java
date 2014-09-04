package com.zb.dalisi.frame.cache;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.meetup.memcached.MemcachedClient;
import com.meetup.memcached.SockIOPool;

public class MemcachedCacheEngine implements CacheEngine {
	private static transient Log log = LogFactory.getLog(MemcachedCacheEngine.class);

	private MemcachedClient mcc = null;

	public void init(){
		String[] servers = { "127.0.0.1:11211" };
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers( servers );
		pool.setFailover( true );
		pool.setInitConn( 10 ); 
		pool.setMinConn( 5 );
		pool.setMaxConn( 250 );
		pool.setMaintSleep( 30 );
		pool.setNagle( false );
		pool.setSocketTO( 3000 );
		pool.setAliveCheck( true );
		pool.initialize();
		this.mcc = new MemcachedClient();
	}
	
//	public void init2222() throws Exception {
//		Cache[] caches = XMLHelper.getInstance().getWebConfig().getCaches().getCache();
//		for (int i=0;i<caches.length;i++){
//			String name = caches[i].getName();
//			String version = caches[i].getVersion();
//			Cacheable cache = (Cacheable)Class.forName(name).newInstance();
//			HashMap map = cache.getAll();
//			Set sets = map.keySet();
//			synchronized(cache){
//				if(cache.isCacheLoaded()){
//					cache.setCacheLoaded(false);
//				}
//				for (Iterator iter = sets.iterator(); iter.hasNext(); ) {
//					String key = (String)iter.next();
//					Object value = map.get(key);
//					boolean success = mcc.set(key, value);
//				}
//				cache.setCacheLoaded(true);
//			}
//		}
//	}
//	
//	public Object getObject(String key) throws Exception{
//		Object result = mcc.get(key);
//		return result;
//	}
//	
//	public void refresh() throws Exception {
////		mcc.delete("");删除耗时长，最好的方法是通过key增加版本号来处理
//		init();
//	}

	@Override
	public void stop() {
	}

	@Override
	public void add(String key, Object value) {
		mcc.set(key, value);
	}

	@Override
	public void add(String fqn, String key, Object value) {
		String realKey = fqn+":"+key+":";
		mcc.set(realKey, value);
	}

	@Override
	public Object get(String fqn, String key) {
		String realKey = fqn+":"+key+":";
		return mcc.get(realKey);
	}

	@Override
	public Object get(String key) {
		return mcc.get(key);
	}

	@Override
	public Collection getValues(String fqn) {
		throw new RuntimeException("MemcachedCacheEngine: getValues unsupport.");
	}

	@Override
	public void remove(String fqn, String key) {
		throw new RuntimeException("MemcachedCacheEngine: remove unsupport.");
	}

	@Override
	public void remove(String fqn) {
		throw new RuntimeException("MemcachedCacheEngine: remove unsupport.");
	}
}
