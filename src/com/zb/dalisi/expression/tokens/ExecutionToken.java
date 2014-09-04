package com.zb.dalisi.expression.tokens;

import com.zb.dalisi.expression.syntax.Executable;

/**
 * ��ִ�ж���
 * @author zhangbin2
 *
 */
public final class ExecutionToken implements Token {
	
	private Executable executable;
	
	public ExecutionToken(TokenBuilder builder) {
		this.executable = builder.getExecutable();
	}
	
	public ExecutionToken(Executable executable) {
		this.executable = executable;
	}
	
	public Executable getExecutable() {
		return executable;
	}

	public final TokenType getTokenType() {
		return TokenType.EXECUTION;
	}

}
