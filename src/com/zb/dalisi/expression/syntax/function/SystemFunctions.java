package com.zb.dalisi.expression.syntax.function;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册系统函数
 * @author zhangbin2
 *
 */
public class SystemFunctions {
	
	private static Map<String, Function> systemFunctions = new HashMap<String, Function>();
	
	/**
	 * 根据函数名获取函数定义
	 * @param functionName
	 * @return
	 */
	public static Function getFunction(String functionName) {
		return systemFunctions.get(functionName);
	}
	
	/**
	 * 判断函数是否存在
	 * @param functionName
	 * @return
	 */
	public static boolean hasFunction(String functionName) {
		return systemFunctions.keySet().contains(functionName);
	}
	
	/**
	 * 添加函数
	 * @param function
	 */
	private static void registerFunction(Function function) {
		function.checkFunctionDefinition();
		systemFunctions.put(function.getName(), function);
	}
	
	static {
		registerFunction(new Max());
		registerFunction(new Abs());
		registerFunction(new Judge());
		registerFunction(new CheckRule());
	}
}

