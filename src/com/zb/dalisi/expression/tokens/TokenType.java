package com.zb.dalisi.expression.tokens;

/**
 * Token����
 * @author zhangbin2
 *
 */
public enum TokenType {
	/**
	 * ���ս��
	 */
	NT,
	
	/**
	 * ����
	 */
	EXECUTION,
	
	/**
	 * �����Ĳ���
	 */
	CONTEXT_OPERATION,
	
	/**
	 * �ؼ���
	 */
	KEY,
	
	/**
	 * ���
	 */
	DELIMITER,
	
	/**
	 * ����
	 */
	FUNCTION,
	
	/**
	 * ����
	 */
	CONST,
	
	/**
	 * ����
	 */
	VARIABLE,
	
	/**
	 * ��������
	 */
	FIELD,
	
	/**
	 * ������
	 */
	METHOD,
	
	/**
	 * ����ʱ���
	 */
	RUNTIME_VALUE
}
