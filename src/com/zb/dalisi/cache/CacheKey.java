package com.zb.dalisi.cache;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CDefKey;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerKey;

public class CacheKey {
	private transient static Logger log = Logger.getLogger(CacheKey.class);
	
	public static Map<String, CDefKey> _keys = Collections.synchronizedMap(new HashMap<String, CDefKey>());

	public static CDefKey getCDefKey(String keyId) {
		log.debug("查找KEY=" + keyId + "的数值!");
		if (!_keys.containsKey(keyId)){
			log.debug("找不到KEY=" + keyId + "的数值!");
			return null;
		}
		return _keys.get(keyId);
	}
	
	public static boolean reload() {
		_keys.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		log.debug("load function call");
		ArrayList list = DBEngine.instance(DBZrulerKey.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerKey data = (DBZrulerKey) iter.next();
			if (!convertDB2Logic(data)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerKey db){
//		if (!(db.getDriverIntfType().equals("01") || db.getDriverIntfType().equals("08"))) {
//			return true;
//		}
		if (!db.getDriverIntfType().equals("01")){
			return true;
		}
		CDefKey logic = new CDefKey();
		logic.setKeySymbol(db.getKeySymbol());
		logic.setDependsKey(db.getParentKey());
		logic.setStatus(db.getStatus());
		logic.setDriverIntfType(db.getDriverIntfType());
		
		try {
			Class<?> driverClass = Class.forName(db.getDriverClass());
			logic.setDriverClass(driverClass);
			String strParamlist = db.getDriverParamlist();
			String findPara[] = null;
			if (strParamlist != null && !strParamlist.trim().equals("")){
				findPara = strParamlist.split(",");
			}else{
				findPara = new String[0];
			}
			
			Method methods[] = driverClass.getDeclaredMethods();// .getMethods();
			Method method = null;
			Type returnType = null;
			for (int i = 0; i < methods.length; ++i) {
				if (methods[i].getName().equals(db.getDriverMethod())) {
					Class<?> para[] = methods[i].getParameterTypes();
					if (para.length != findPara.length){
						break;
					}
					boolean compareEachParamType = true;
					for (int j = 0; j < para.length; ++j) {
						//getCanonicalName ==> "com.test.Apple[]"
						//getSimpleName ==> "Apple[]"
						//getName ==> "[Lcom.test.Apple;"
						String paramClassName = para[j].getCanonicalName();
						if (! paramClassName.equals(findPara[j])){
							compareEachParamType = false;
							break;
						}
					}
					if (compareEachParamType) {
						method = methods[i];
						returnType = methods[i].getGenericReturnType();
						break;
					}
				}
			}
			
			if (method != null) {
				logic.setDriverMethod(method);
				logic.setDriverReturn(returnType);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
//		Field[] fields = db.getClass().getDeclaredFields();
//		for(int i=0; i<fields.length; i++){
//			fields[i].setAccessible(true);
//			String name = fields[i].getName();
//			name = Utils.decodeColName(name).toUpperCase();
//			try {
//				logic.setProperty(name, fields[i].get(db));
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//				return false;
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
		try {
			logic.fillProperty(db);
		} catch (RuleException e) {
			log.debug(e.getMessage());
			return false;
		}

		_keys.put(logic.getKeySymbol(), logic);
		return true;
	}
}
