package com.zb.dalisi.expression.tokens;


/**
 * VariableToken
 * @author zhangbin2
 *
 */
public final class VariableToken extends ValueToken {
	/**
	 * 是否是被赋值的变量，即变量在表达式中处于赋值操作符的左边
	 */
	private boolean toBeAssigned;
	private boolean keepInContext;

	public VariableToken(TokenBuilder builder) {
		super(builder);
		toBeAssigned = builder.isToBeAssigned();
	}
	
	public boolean isToBeAssigned() {
		return toBeAssigned;
	}
	
	public void setToBeAssigned(boolean toBeAssigned) {
		this.toBeAssigned = toBeAssigned;
	}
	
	public TokenType getTokenType() {
		return TokenType.VARIABLE;
	}
	
	public void assignWith(Valuable value) {
		setDataType(value.getDataType());
		setIndex(value.getIndex());
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		if(!(target instanceof VariableToken))
			return false;
		return this.isToBeAssigned() == ((VariableToken)target).isToBeAssigned();
	}

	public boolean isKeepInContext() {
		return keepInContext;
	}

	public void setKeepInContext(boolean keepInContext) {
		this.keepInContext = keepInContext;
	}

}
