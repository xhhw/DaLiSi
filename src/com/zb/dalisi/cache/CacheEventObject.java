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
import com.zb.dalisi.app.module.CEventObject;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerEventObject;
import com.zb.dalisi.utils.Utils;

public class CacheEventObject {
	private transient static Logger log = Logger.getLogger(CacheEventObject.class);
	
	public static Map<String, CEventObject> _eventobjs = Collections.synchronizedMap(new HashMap<String, CEventObject>());

	public static CEventObject getEventObject(String eventObjectId) {
		if (!_eventobjs.containsKey(eventObjectId)){
			log.debug("没有找到驱动对象"+eventObjectId+"的定义");
			return null;
		}
		return _eventobjs.get(eventObjectId);
	}
	
	public static boolean reload() {
		_eventobjs.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerEventObject.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerEventObject attr = (DBZrulerEventObject) iter.next();
			if (!convertDB2Logic(attr)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerEventObject db){
		CEventObject logic = null;
		if (_eventobjs.containsKey(db.getEventObjectId())) {
			logic = _eventobjs.get(db.getEventObjectId());
		}else{
			logic = new CEventObject(db.getEventObjectId());
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
		
		if (!_eventobjs.containsKey(logic.getEventObjectId())) {
			_eventobjs.put(logic.getEventObjectId(), logic);
		}
		return true;
	}

	public static ArrayList<CEventObject> findObjects(
			ArrayList<Map<String, String>> eventObjList) {
		ArrayList<CEventObject> list = new ArrayList<CEventObject>();
		for (int i = 0; i < eventObjList.size(); i++) {
			CEventObject obj = findEventObj(eventObjList.get(i));
			if (obj != null){
				list.add(obj);
			}
		}
		return list;
	}
	
	public static CEventObject findEventObj(Map<String, String> objMap) {
		Map src = Utils.sortMap(objMap);
		int size = src.size();
		
		Collection<CEventObject> c = _eventobjs.values();
		Iterator<CEventObject> iter = c.iterator();
		while (iter.hasNext()) {
			CEventObject cfgObj = iter.next();
			Map<String, CCombEntityAttr> attrs = cfgObj.getAttrs();
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
			return cfgObj;
		}
		
		return null;
	}
	
}
