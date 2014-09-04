package com.zb.dalisi.expression.tokens;

/**
 * 数据类型
 * @author zhangbin2
 *
 */
public enum DataType {
	NUMBER,		//数字	BigDecimal
	INT,		//数字	Integer
	LONG,		//数字	Long
	BOOLEAN,	//布尔	Boolean
	STRING,		//字符串	String
	CHARACTER,	//字符	Character
	DATE,		//日期	Calendar
	ANY			//任意类型	Object
}
