package com.zb.dalisi.cache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CCombEntityAttr;
import com.zb.dalisi.app.module.CEvent;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerEvent;
import com.zb.dalisi.utils.Utils;

public class CacheEvent {
	private transient static Logger log = Logger.getLogger(CacheEvent.class);
	
	public static Map<String, CEvent> _events = Collections.synchronizedMap(new HashMap<String, CEvent>());

	public static CEvent getEvent(String eventId) {
		if (!_events.containsKey(eventId)){
			log.debug("沒有找到事件"+eventId+"的定义.");
			return null;
		}
		return _events.get(eventId);
	}
	
	public static boolean reload() {
		_events.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerEvent.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerEvent attr = (DBZrulerEvent) iter.next();
			if (!convertDB2Logic(attr)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerEvent db){
		CEvent logic = null;
		if (_events.containsKey(db.getEventId())) {
			logic = _events.get(db.getEventId());
		}else{
			logic = new CEvent(db.getEventId());
		}
		CCombEntityAttr combEntityAttr = new CCombEntityAttr();
		combEntityAttr.setAttrId(db.getAttrId());
		combEntityAttr.setStatus(db.getStatus());
		combEntityAttr.setAttrValue(db.getAttrValue());
		
//		Field[] fields = db.getClass().getDeclaredFields();
//		for(int i=0; i<fields.length; i++){
//			fields[i].setAccessible(true);
//			String name = fields[i].getName();
//			name = Utils.decodeColName(name).toUpperCase();
//			try {
//				combEntityAttr.setProperty(name, fields[i].get(db));
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//				return false;
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//				return false;
//			}
//		}
		try {
			combEntityAttr.fillProperty(db);
		} catch (RuleException e) {
			log.debug(e.getMessage());
			return false;
		}
		logic.addCombEntityAttr(combEntityAttr);
		
		if (!_events.containsKey(logic.getEventId())) {
			_events.put(logic.getEventId(), logic);
		}
		return true;
	}

	public static CEvent findEvent(Map<String, String> eventMap) {
		Map src = Utils.sortMap(eventMap);
		int size = src.size();
		
		Collection<CEvent> c = _events.values();
		Iterator<CEvent> iter = c.iterator();
		while (iter.hasNext()) {
			CEvent cfgEvent = iter.next();
			Map<String, CCombEntityAttr> attrs = cfgEvent.getAttrs();
			if (attrs.size() != size) {
				continue;
			}

			Set<Map.Entry<String, String>> set = src.entrySet();
			boolean bFlag = true;
			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				if (attrs.containsKey(entry.getKey())) {
					Object value = attrs.get(entry.getKey()).getAttrValue();
					if (value instanceof BigDecimal){
						if(((BigDecimal)value).compareTo(new BigDecimal(entry.getValue()))==0){
							continue;
						}
					}
					if (value.equals(entry.getValue())) {
						continue;
					}
				}
				bFlag = false;
			}
			if (!bFlag) {
				continue;
			}
			return cfgEvent;
		}
		
		return null;
	}

}
