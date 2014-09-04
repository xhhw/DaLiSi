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
//			throw new RuleException("沒有"+propName+"的属性定义.");
//		}
//		return ret;
//	}
//	
//	public void setProperty(String propName, Object propValue){
//		if (this.props.containsKey(propName)){
//			throw new RuleException("重复定义"+propName+"的属性值.");
//		}
//		this.props.put(propName, propValue);
//	}
	
	//从配置表中的value反向序列化为java对象，需要结合配置的data_type
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
			case OBJECT:// 配置表中必须是JSON字符串
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
				// RuleException("实例化未加载的类"+dataClass.getCanonicalName()+".");
				// } catch (IllegalAccessException e) {
				// e.printStackTrace();
				// throw new
				// RuleException("实例化未加载的类"+dataClass.getCanonicalName()+".");
				// }
				ret = JSONObject.toBean(JSONObject.fromObject(src), dataClass,
						classMap);
				System.out.println(ret);
			}
		} catch (Exception e) {
			throw new RuleException("参数‘"+src+"’要求的数据类型为‘"+this.dataType+"’.");
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
