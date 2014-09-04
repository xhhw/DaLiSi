package com.zb.dalisi.expression.tokens;

import java.lang.reflect.Field;


public final class ObjectFieldToken extends ValueToken {
	private boolean toBeAssigned;
	private Field field;
	private Object obj;

	public ObjectFieldToken(TokenBuilder builder) {
		super(builder);
		toBeAssigned = builder.isToBeAssigned();
	}

	public TokenType getTokenType() {
		// TODO Auto-generated method stub
		return TokenType.FIELD;
	}
	
	public void assignWith(Valuable value) {
		setDataType(value.getDataType());
		setIndex(value.getIndex());
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		// TODO Auto-generated method stub
		if(!(target instanceof ObjectFieldToken))
			return false;
		return this.isToBeAssigned() == ((ObjectFieldToken)target).isToBeAssigned();
	}

	public boolean isToBeAssigned() {
		return toBeAssigned;
	}

	public void setToBeAssigned(boolean toBeAssigned) {
		this.toBeAssigned = toBeAssigned;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
