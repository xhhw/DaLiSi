package com.zb.dalisi.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CSceneEventObjKey;
import com.zb.dalisi.app.module.CSceneEventObjRule;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerDataruleapp;

public class CacheSceneEventObjectRules {
	private transient static Logger log = Logger.getLogger(CacheSceneEventObjectRules.class);
	
	public static Map<CSceneEventObjKey, ArrayList<CSceneEventObjRule>> _eventObjRules = Collections.synchronizedMap(new HashMap<CSceneEventObjKey, ArrayList<CSceneEventObjRule>>());
	
	public static ArrayList<CSceneEventObjRule> getSceneEventRules(CSceneEventObjKey key) {
		if (!_eventObjRules.containsKey(key)) {
			log.debug("根据场景" + key.getSceneId() + "事件"
					+ key.getEventId() + "驱动对象" + key.getEventObjId()
					+ "找不到对应的规则!");
			return new ArrayList<CSceneEventObjRule>();
		}
		return _eventObjRules.get(key);
	}
	
	public static boolean reload() {
		_eventObjRules.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerDataruleapp.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerDataruleapp data = (DBZrulerDataruleapp) iter.next();
			if (!convertDB2Logic(data)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerDataruleapp db){
		CSceneEventObjKey key = new CSceneEventObjKey();
		key.setAppId(db.getAppId());
		key.setSceneId(db.getSceneId());
		key.setEventId(db.getEventId());
		key.setEventObjId(db.getEventObjectId());
		
		CSceneEventObjRule logic = new CSceneEventObjRule();
		logic.setKey(key);
		logic.setRuleId(db.getRuleId());
		logic.setMsgCode(db.getMsgCode());
		logic.setValid(db.getStatus().equals("1"));
		
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
		
		if (_eventObjRules.containsKey(key)) {
			_eventObjRules.get(key).add(logic);
		} else {
			ArrayList<CSceneEventObjRule> list = new ArrayList<CSceneEventObjRule>();
			list.add(logic);
			_eventObjRules.put(logic.getKey(), list);
		}
		
		return true;
	}
	
}
