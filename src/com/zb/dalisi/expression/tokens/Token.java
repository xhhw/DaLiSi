package com.zb.dalisi.expression.tokens;

/**
 * 符号接口
 * @author zhangbin2
 *
 */
public interface Token {
	/**
	 * 返回符号类型
	 * @return
	 */
	public abstract TokenType getTokenType();
}
