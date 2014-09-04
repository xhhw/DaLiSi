package com.zb.dalisi.app.module;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.Valuable;

public class CDefFunction extends CLogicBase implements Executable {
//	private Map<String, Object> props = new HashMap<String, Object>();
	private String funcId;
	private String funcName;
	private String status;
	/* driverIntfType
	 * 01：JAVA
	 * 02：JS
	 * 03：WEBSERVICE
	 * 04：CORBA
	 * 05：PROC
	 * 06：SQL
	 */
	private String driverIntfType;
	
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

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDriverIntfType() {
		return driverIntfType;
	}

	public void setDriverIntfType(String driverIntfType) {
		this.driverIntfType = driverIntfType;
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

	@Override
	public int getArgumentNum() {
		// TODO Auto-generated method stub
		if (driverMethod == null){
			return 0;
		}
		Class<?> para[] = driverMethod.getParameterTypes();
		int num = para.length;
		return num;
	}

	@Override
	public Valuable execute(Valuable[] arguments)
			throws ArgumentsMismatchException {
		// TODO Auto-generated method stub
		try {
			Object[] args = new Object[arguments.length];
			if (getArgumentNum() != arguments.length){
				throw new IllegalArgumentException();
			}
//			Class<?> para[]=driverMethod.getParameterTypes();
//			for(int j=0;j<para.length;++j){
//				String paramType = para[j].getSimpleName();
//				if (paramType.equals("int")){
//					args[j] = arguments[j].getNumberValue().intValue();
//				}
//				else if (paramType.equals("long")){
//					args[j] = arguments[j].getNumberValue().longValue();
//				}
//				else if (paramType.equals("double")){
//					args[j] = arguments[j].getNumberValue().doubleValue();
//				}
//				else if (paramType.equals("float")){
//					args[j] = arguments[j].getNumberValue().floatValue();
//				}
//				else if (paramType.equals("String")){
//					args[j] = arguments[j].getStringValue();
//				}
//				else if (paramType.equals("boolean")){
//					args[j] = arguments[j].getBooleanValue();
//				}
//				else if (paramType.equals("char")){
//					args[j] = arguments[j].getCharValue();
//				}
//				else if (paramType.equals("Calendar")){
//					args[j] = arguments[j].getDateValue();
//				}
//				else if (paramType.equals("Valuable")){
//					args[j] = arguments[j];
//				}
//				else if (paramType.equals("Valuable[]")){
//					args[j] = arguments;
//				}
//				else {
//					args[j] = arguments[j].getValue();
//				}
//			}
//			for (int i=0;i<arguments.length;i++){
//				args[i]=arguments[i].getNumberValue().longValue();
//			}
//			if (arguments instanceof Valuable[]){
//				System.out.println("arguments instanceof Valuable[]");
//			}
//			if (arguments instanceof Object[]){
//				System.out.println("arguments instanceof Object[]");
//			}
//			Object result = function.invoke(dirverClass.newInstance(), (Object)arguments);
			
			Class<?> para[] = driverMethod.getParameterTypes();
			for(int j=0;j<arguments.length;++j){
				if ("ailk.sh.rule.expression.tokens.Valuable[]".equals(para[j].getCanonicalName())){
					args[j] = (Object)arguments;
				} else{
					args[j] = arguments[j].getValue();
				}
			}
			
			Object result = driverMethod.invoke(driverClass.newInstance(), args);
			return TokenBuilder.buildRuntimeValue(result);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
