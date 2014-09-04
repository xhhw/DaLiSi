package com.zb.dalisi.cache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zb.dalisi.app.module.CDefAttr;
import com.zb.dalisi.app.module.EDataType;
import com.zb.dalisi.app.module.RuleException;
import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerAttribute;
import com.zb.dalisi.frame.cache.CacheEngine;
import com.zb.dalisi.frame.cache.Cacheable;

public class CacheAttr implements Cacheable {
	private static transient Log log = LogFactory.getLog(CacheAttr.class);
	private static final String FQN = "ATTR";
	private CacheEngine cache;

	public CDefAttr getCDefAttr(String attrId){
		return (CDefAttr) cache.get(FQN, attrId);
	}
	
	@SuppressWarnings("rawtypes")
	public void load() {
		ArrayList list = DBEngine.instance(DBZrulerAttribute.class).selectAll();
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			DBZrulerAttribute attr = (DBZrulerAttribute) iter.next();
			if (!convertDB2Logic(attr)){
			}
		}
	}
	
	public boolean convertDB2Logic(DBZrulerAttribute db){
		CDefAttr logic = new CDefAttr();
		logic.setAttrId(db.getAttrId());
//		logic.setDataType(db.getDataType());
//		01：字符型
//		02：数字型
//		03:   时间
//		04:   自定义对象
		if (db.getDataType().equals("01")) {
			logic.setDataType(EDataType.STRING);
			logic.setDataClass(String.class);
		} else if (db.getDataType().equals("02")) {
			logic.setDataType(EDataType.NUMBER);
			logic.setDataClass(BigDecimal.class);
		} else if (db.getDataType().equals("03")) {
			logic.setDataType(EDataType.DATETIME);
			logic.setDataClass(Calendar.class);
		} else /*if (db.getDataType().equals("04"))*/ {
			logic.setDataType(EDataType.OBJECT);
			try {
				logic.setDataClass(Class.forName(db.getDataType()));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				log.debug("数据类型"+db.getDataType()+"未定义");
				return false;
			}
		} /*else {
			return false;
		}*/
		
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
		
		cache.add(FQN, logic.getAttrId(), logic);
		return true;
	}

	@Override
	public void setCacheEngine(CacheEngine engine) {
		// TODO Auto-generated method stub
		this.cache = engine;
	}
	
}
