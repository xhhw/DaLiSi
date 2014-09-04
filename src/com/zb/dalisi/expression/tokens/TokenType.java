package com.zb.dalisi.expression.tokens;

/**
 * Token类型
 * @author zhangbin2
 *
 */
public enum TokenType {
	/**
	 * 非终结符
	 */
	NT,
	
	/**
	 * 动作
	 */
	EXECUTION,
	
	/**
	 * 上下文操作
	 */
	CONTEXT_OPERATION,
	
	/**
	 * 关键字
	 */
	KEY,
	
	/**
	 * 界符
	 */
	DELIMITER,
	
	/**
	 * 函数
	 */
	FUNCTION,
	
	/**
	 * 常量
	 */
	CONST,
	
	/**
	 * 变量
	 */
	VARIABLE,
	
	/**
	 * 对象属性
	 */
	FIELD,
	
	/**
	 * 对象函数
	 */
	METHOD,
	
	/**
	 * 运行时结果
	 */
	RUNTIME_VALUE
}
