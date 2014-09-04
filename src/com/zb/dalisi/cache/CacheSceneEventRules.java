package com.zb.dalisi.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CSceneEventKey;
import com.zb.dalisi.app.module.CSceneEventRule;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerActionruleapp;

public class CacheSceneEventRules {
	private transient static Logger log = Logger.getLogger(CacheSceneEventRules.class);
	
	public static Map<CSceneEventKey, ArrayList<CSceneEventRule>> _senceEventRules = Collections.synchronizedMap(new HashMap<CSceneEventKey, ArrayList<CSceneEventRule>>());
	
	public static ArrayList<CSceneEventRule> getSceneEventRules(CSceneEventKey key) {
		if (!_senceEventRules.containsKey(key)){
			log.debug("根据场景" + key.getSceneId() + "事件"
					+ key.getEventId() + "找不到对应的规则!");
			return new ArrayList<CSceneEventRule>();
		}
		return _senceEventRules.get(key);
	}
	
	public static boolean reload() {
		_senceEventRules.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerActionruleapp.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerActionruleapp data = (DBZrulerActionruleapp) iter.next();
			if (!convertDB2Logic(data)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerActionruleapp db){
		CSceneEventKey key = new CSceneEventKey();
		key.setAppId(db.getAppId());
		key.setSceneId(db.getSceneId());
		key.setEventId(db.getEventId());
		
		CSceneEventRule logic = new CSceneEventRule();
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
		
		if (_senceEventRules.containsKey(key)) {
			_senceEventRules.get(key).add(logic);
		} else {
			ArrayList<CSceneEventRule> list = new ArrayList<CSceneEventRule>();
			list.add(logic);
			_senceEventRules.put(logic.getKey(), list);
		}
		
		return true;
	}
}
