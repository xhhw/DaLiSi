package com.zb.dalisi.expression.syntax.function;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.Valuable;

public class UserFunction implements Executable {
	/**
	 * º¯ÊýÃû
	 */
	private Method method;
	private Object obj;
	
	public UserFunction(Object obj, Method method){
		this.obj = obj;
		this.method = method;
	}

	public int getArgumentNum() {
		// TODO Auto-generated method stub
		if (method == null){
			return 0;
		}
		return method.getParameterTypes().length;
	}

	public Valuable execute(Valuable[] arguments)
			throws ArgumentsMismatchException {
		// TODO Auto-generated method stub
		Object result = null;
		if (arguments.length == 0){
			result = executeInvoke();
		} else {
			Class<?> para[] = method.getParameterTypes();
			Object[] args = new Object[arguments.length];
			for(int j=0;j<arguments.length;++j){
				if ("ailk.sh.rule.expression.tokens.Valuable[]".equals(para[j].getCanonicalName())){
					args[j] = (Object)arguments;
				} else{
					args[j] = arguments[j].getValue();
				}
			}
			result = executeInvoke(args);
		}
		if (result instanceof Valuable){
			return (Valuable)result;
		}
		return TokenBuilder.buildRuntimeValue(result);
	}
	
	public Object executeInvoke(Object... args) {
		Object result = null;
		try {
			result = method.invoke(obj, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}

}
