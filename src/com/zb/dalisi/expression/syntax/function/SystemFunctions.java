package com.zb.dalisi.expression.syntax.function;

import java.util.HashMap;
import java.util.Map;

/**
 * ע��ϵͳ����
 * @author zhangbin2
 *
 */
public class SystemFunctions {
	
	private static Map<String, Function> systemFunctions = new HashMap<String, Function>();
	
	/**
	 * ���ݺ�������ȡ��������
	 * @param functionName
	 * @return
	 */
	public static Function getFunction(String functionName) {
		return systemFunctions.get(functionName);
	}
	
	/**
	 * �жϺ����Ƿ����
	 * @param functionName
	 * @return
	 */
	public static boolean hasFunction(String functionName) {
		return systemFunctions.keySet().contains(functionName);
	}
	
	/**
	 * ��Ӻ���
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

