package com.zb.dalisi.expression.tokens;

/**
 * 涓婁笅鏂囨搷浣滅鍙�? * @author shanxuecheng
 *
 */
public class ContextOperationToken implements Token {
	
	ContextOperation contextOperation;
	
	public ContextOperationToken(TokenBuilder builder) {
		contextOperation = builder.getContextOperation();
	}
	
	public ContextOperationToken(ContextOperation contextOperation) {
		this.contextOperation = contextOperation;
	}

	public TokenType getTokenType() {
		return TokenType.CONTEXT_OPERATION;
	}
	
	public ContextOperation getContextOperation() {
		return contextOperation;
	}
}
