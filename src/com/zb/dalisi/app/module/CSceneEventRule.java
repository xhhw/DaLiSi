package com.zb.dalisi.app.module;



public class CSceneEventRule extends CLogicBase {
//	private Map<String, Object> props = new HashMap<String, Object>();
	private CSceneEventKey key;
	private String ruleId;
	private String msgCode;
	private boolean isValid;
	
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
	
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public CSceneEventKey getKey() {
		return key;
	}

	public void setKey(CSceneEventKey key) {
		this.key = key;
	}
	
}
