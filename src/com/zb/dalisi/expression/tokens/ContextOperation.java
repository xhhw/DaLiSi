package com.zb.dalisi.expression.tokens;

/**
 * �����Ĳ���
 * @author zhangbin2
 *
 */
public enum ContextOperation {
	/**
	 * if����
	 */
	IF_CONDITION,
	
	/**
	 * else����
	 */
	ELSE_CONDITION,
	
	/**
	 * �½�������
	 */
	NEW_CONTEXT,
	
	/**
	 * �����Ľ���
	 */
	END_CONTEXT,
	
	/**
	 * if������
	 */
	END_IF,
	
	/**
	 * AND���
	 */
	AND_CONDITION,
	END_AND,
	
	/**
	 * AND���
	 */
	OR_CONDITION,
	END_OR
}
