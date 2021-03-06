package com.zb.dalisi.expression.lexical.dfa;

/**
 * 定义有限自动机的结束状态代码
 * @author zhangbin2
 *
 */
public enum DFAEndStateCode {
	NUMBER_END,	//数字结束状态
	NUMBER_INT_END,	//int数字结束状态
	NUMBER_LONG_END,	//long数字结束状态
	
	//标识符结束状态
	//识别出标识符时，须依次判断是否为：布尔常量、关键字、函数名、变量
	ID_END,	
	
	//单字符界符结束状态（如+、-）
	SINGLE_DELIMITER_END,
	
	//双字符界符结束状态（如>=、&&）
	DOUBLE_DELIMITER_END,
	
	//日期结束状态
	DATE_END,
	
	//字符结束状态（可使用转义字符）
	CHAR_END,
	
	//字符串结束状态（可使用转义字符）
	STRING_END,
	
	//自定义对象状态
	OBJECT_END
}
