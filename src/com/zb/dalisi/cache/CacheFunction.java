package com.zb.dalisi.cache;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CDefFunction;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerFunction;

public class CacheFunction {
	private transient static Logger log = Logger.getLogger(CacheFunction.class);
	
	public static Map<String, CDefFunction> _funcs = Collections.synchronizedMap(new HashMap<String, CDefFunction>());

	public static CDefFunction getDefFunction(String funcName) {
		if (!_funcs.containsKey(funcName)){
			log.debug("没有找到函数"+funcName+"的定义");
			return null;
		}
		return _funcs.get(funcName);
	}
	
	public static boolean hasFunction(String funcName) {
		if(_funcs == null || _funcs.size() == 0)
			return false;
		return _funcs.containsKey(funcName);
	}
	
	public static boolean reload() {
		_funcs.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerFunction.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerFunction data = (DBZrulerFunction) iter.next();
			if (!convertDB2Logic(data)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerFunction db){
		if (!db.getDriverIntfType().equals("01")){
			return true;
		}
		CDefFunction logic = new CDefFunction();
		logic.setFuncId(db.getFuncId());
		logic.setStatus(db.getStatus());
		logic.setFuncName(db.getFuncName());
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
				if (methods[i].getName().equals(db.getFuncName())) {
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

		_funcs.put(logic.getFuncName(), logic);
		return true;
	}
}
