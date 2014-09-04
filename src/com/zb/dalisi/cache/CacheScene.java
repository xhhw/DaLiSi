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
import com.zb.dalisi.app.module.CScene;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerScene;
import com.zb.dalisi.utils.Utils;

public class CacheScene {
	private transient static Logger log = Logger.getLogger(CacheScene.class);
	
	private static Map<String, CScene> _scenes = Collections.synchronizedMap(new HashMap<String, CScene>());

	public static CScene getScene(String sceneId) {
		if (!_scenes.containsKey(sceneId)){
			log.debug("没有找到场景"+sceneId+"的定义");
			return null;
		}
		return _scenes.get(sceneId);
	}
	
	public static boolean reload() {
		_scenes.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerScene.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerScene attr = (DBZrulerScene) iter.next();
			if (!convertDB2Logic(attr)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerScene db){
		CScene logic = null;
		if (_scenes.containsKey(db.getSceneId())) {
			logic = _scenes.get(db.getSceneId());
		}else{
			logic = new CScene(db.getSceneId());
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
		
		if (!_scenes.containsKey(logic.getSceneId())) {
			_scenes.put(logic.getSceneId(), logic);
		}
		
		return true;
	}
	
	public static CScene findScene(Map<String, String> map){
		Map src = Utils.sortMap(map);
		int size = src.size();
		
		Collection<CScene> c = _scenes.values();
		Iterator<CScene> iter = c.iterator();
		while (iter.hasNext()) {
			CScene cfgScene = iter.next();
			Map<String, CCombEntityAttr> attrs = cfgScene.getAttrs();
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
			return cfgScene;
		}
		
		return null;
	}
	
}
