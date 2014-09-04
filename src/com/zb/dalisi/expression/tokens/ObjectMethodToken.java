package com.zb.dalisi.expression.tokens;

import java.lang.reflect.Method;

import com.zb.dalisi.expression.syntax.function.UserFunction;

public final class ObjectMethodToken extends ValueToken {
	private Method method;
	private Object obj;

	public ObjectMethodToken(TokenBuilder builder) {
		super(builder);
		// TODO Auto-generated constructor stub
	}

	public TokenType getTokenType() {
		// TODO Auto-generated method stub
		return TokenType.METHOD;
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		// TODO Auto-generated method stub
		return target instanceof ObjectMethodToken;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public UserFunction getFunction() {
		return new UserFunction(obj,method);
	}
	
}
