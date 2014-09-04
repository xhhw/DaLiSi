package com.zb.dalisi.app.module;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.zb.dalisi.utils.Utils;

public abstract class CLogicBase {
	private Map<String, Object> props = new HashMap<String, Object>();
	
	public Object getProperty(String propName){
		Object ret = this.props.get(propName);
		if (ret == null){
			throw new RuleException("�]��"+propName+"�����Զ���.");
		}
		return ret;
	}
	
	public void setProperty(String propName, Object propValue){
		if (this.props.containsKey(propName)){
			throw new RuleException("�ظ�����"+propName+"������ֵ.");
		}
		this.props.put(propName, propValue);
	}
	
	public void fillProperty(Object db) {
		Field[] fields = db.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String name = fields[i].getName();
			name = Utils.decodeColName(name).toUpperCase();
			try {
				setProperty(name, fields[i].get(db));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RuleException("�������ֵʧ��1.");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuleException("�������ֵʧ��2.");
			}
		}
	}
	
}
