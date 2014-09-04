package com.zb.dalisi.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zb.dalisi.app.module.CDefMessage;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerMsgcode;

public class CacheMessage {
	private transient static Logger log = Logger.getLogger(CacheMessage.class);
	
	public static Map<String, CDefMessage> _msgs = Collections.synchronizedMap(new HashMap<String, CDefMessage>());
	
	public static CDefMessage getCDefMessage(String code){
		if (!_msgs.containsKey(code)){
			log.debug("没有找到消息"+code+"的定义");
			return null;
		}
		return _msgs.get(code);
	}
	
	public static boolean reload() {
		_msgs.clear();
		return load();
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean load() {
		ArrayList list = DBEngine.instance(DBZrulerMsgcode.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerMsgcode attr = (DBZrulerMsgcode) iter.next();
			if (!convertDB2Logic(attr)){
				return false;
			}
		}
		return true;
	}
	
	public static boolean convertDB2Logic(DBZrulerMsgcode db){
		CDefMessage logic = new CDefMessage();
		logic.setCode(db.getMsgCode());
		logic.setMsg(db.getMsgDesc());
		
		_msgs.put(logic.getCode(), logic);
		return true;
	}
}
