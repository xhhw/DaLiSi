package com.zb.dalisi.app.module;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;


public class CDefAttr extends CLogicBase {
//	private Map<String, Object> props = new HashMap<String, Object>();
	private String attrId;
	private EDataType dataType;
	private Class<?> dataClass;
	
//	public Object getProperty(String propName){
//		Object ret = this.props.get(propName);
//		if (ret == null){
//			throw new RuleException("�]��"+propName+"�����Զ���.");
//		}
//		return ret;
//	}
//	
//	public void setProperty(String propName, Object propValue){
//		if (this.props.containsKey(propName)){
//			throw new RuleException("�ظ�����"+propName+"������ֵ.");
//		}
//		this.props.put(propName, propValue);
//	}
	
	//�����ñ��е�value�������л�Ϊjava������Ҫ������õ�data_type
	public Object getDataFormString(String src) {
		Object ret = null;
		try {
			switch (this.dataType) {
			case STRING:
				ret = src;
				break;
			case NUMBER:
				ret = new BigDecimal(src);
				break;
			case DATETIME:
				Calendar date = null;
				ret = date;
				break;
			case OBJECT:// ���ñ��б�����JSON�ַ���
				// JSONArray ja = JSONArray.fromObject(jsonString);
				// Map<String, Class<pictures>> classMap = new HashMap<String,
				// Class<pictures>>();
				// classMap.put("pictures", pictures.class);
				// List<Content> list = JSONArray.toList(ja, Content.class,
				// classMap);
				Map<String, Object> classMap = new HashMap<String, Object>();
				// try {
				// ret = dataClass.newInstance();
				// } catch (InstantiationException e) {
				// e.printStackTrace();
				// throw new
				// RuleException("ʵ����δ���ص���"+dataClass.getCanonicalName()+".");
				// } catch (IllegalAccessException e) {
				// e.printStackTrace();
				// throw new
				// RuleException("ʵ����δ���ص���"+dataClass.getCanonicalName()+".");
				// }
				ret = JSONObject.toBean(JSONObject.fromObject(src), dataClass,
						classMap);
				System.out.println(ret);
			}
		} catch (Exception e) {
			throw new RuleException("������"+src+"��Ҫ�����������Ϊ��"+this.dataType+"��.");
		}
		return ret;
	}

	public String getAttrId() {
		return attrId;
	}
	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}
	public EDataType getDataType() {
		return dataType;
	}
	public void setDataType(EDataType dataType) {
		this.dataType = dataType;
	}
	public void setDataClass(Class<?> dataClass) {
		this.dataClass = dataClass;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("##classname : "+CDefAttr.class.getName()+"\n");
		buf.append("     attrId : "+attrId+"\n");
		buf.append("displayType : "+dataType+"\n");
		return buf.toString();
	}
}
