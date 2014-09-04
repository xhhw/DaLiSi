package com.zb.dalisi.expression.syntax.operator;

/**
 * 涓�厓鎿嶄綔绗�
 * @author shanxuecheng
 *
 */
public abstract class UnaryOperator extends Operator {

	public UnaryOperator(String operatorName) {
		super(operatorName);
	}

	/**
	 * 鎿嶄綔鏁颁釜鏁颁�?
	 */
	public final int getArgumentNum() {
		return 1;
	}

}
