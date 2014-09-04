package com.zb.dalisi.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CDefRule;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerRule;

public class CacheRule {
	private transient static Logger log = Logger.getLogger(CacheRule.class);
	
	public static Map<String, CDefRule> _rules = Collections.synchronizedMap(new HashMap<String, CDefRule>());
	
	public static CDefRule getCDefRule(String ruleId){
		if (!_rules.containsKey(ruleId)){
			log.debug("没有找到规则"+ruleId+"的定义");
			return null;
		}
		return _rules.get(ruleId);
	}
	
	public static boolean reload() {
		_rules.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerRule.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerRule data = (DBZrulerRule) iter.next();
			if (!convertDB2Logic(data)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerRule db){
		CDefRule logic = new CDefRule();
		logic.setRuleId(db.getRuleId());
		logic.setExpression(db.getExpression());
		logic.setMsgCode(db.getMsgCode());
		logic.setRuleAttentionType(db.getRuleAttentionType());
		logic.setValid(db.getStatus().equals("1"));
		logic.setDriverType(db.getExt1());
		
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
		
		_rules.put(logic.getRuleId(), logic);
		return true;
	}

}
