package com.zb.dalisi.expression.tokens;

/**
 * 上下文操作
 * @author zhangbin2
 *
 */
public enum ContextOperation {
	/**
	 * if条件
	 */
	IF_CONDITION,
	
	/**
	 * else条件
	 */
	ELSE_CONDITION,
	
	/**
	 * 新建上下文
	 */
	NEW_CONTEXT,
	
	/**
	 * 上下文结束
	 */
	END_CONTEXT,
	
	/**
	 * if语句结束
	 */
	END_IF,
	
	/**
	 * AND语句
	 */
	AND_CONDITION,
	END_AND,
	
	/**
	 * AND语句
	 */
	OR_CONDITION,
	END_OR
}
