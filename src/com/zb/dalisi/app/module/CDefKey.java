package com.zb.dalisi.app.module;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class CDefKey extends CLogicBase {
//	private Map<String, Object> props = new HashMap<String, Object>();
	private String keySymbol;
	private String driverIntfType;
	private String dependsKey;
	private String status;
	
	private Class<?> driverClass;
	private Method driverMethod;
	private Type driverReturn;
	
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

	public String getKeySymbol() {
		return keySymbol;
	}

	public void setKeySymbol(String keySymbol) {
		this.keySymbol = keySymbol;
	}

	public String getDriverIntfType() {
		return driverIntfType;
	}

	public void setDriverIntfType(String driverIntfType) {
		this.driverIntfType = driverIntfType;
	}

	public String getDependsKey() {
		return dependsKey;
	}

	public void setDependsKey(String dependsKey) {
		this.dependsKey = dependsKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Class<?> getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(Class<?> driverClass) {
		this.driverClass = driverClass;
	}

	public Method getDriverMethod() {
		return driverMethod;
	}

	public void setDriverMethod(Method driverMethod) {
		this.driverMethod = driverMethod;
	}

	public Type getDriverReturn() {
		return driverReturn;
	}

	public void setDriverReturn(Type driverReturn) {
		this.driverReturn = driverReturn;
	}
}
