package com.zb.dalisi.expression.tokens;

import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.syntax.function.Function;

/**
 * 函数符号
 * @author zhangbin2
 *
 */
public final class FunctionToken extends TerminalToken {

	/**
	 * 函数定义
	 */
	private final Executable function;
	
	public FunctionToken(TokenBuilder builder) {
		super(builder);
		function = builder.getExecutable();//.getFunction();
	}
	
	public Executable getFunction() {
		return function;
	}
	
	public TokenType getTokenType() {
		return TokenType.FUNCTION;
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		return target instanceof FunctionToken;
	}

}
