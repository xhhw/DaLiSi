package com.zb.dalisi.expression.syntax.operator;

/**
 * 浜屽厓鎿嶄綔绗�
 * @author shanxuecheng
 *
 */
public abstract class BinaryOperator extends Operator {

	public BinaryOperator(String operator) {
		super(operator);
	}

	/**
	 * 鎿嶄綔鏁颁釜鏁颁�?
	 */
	public final int getArgumentNum() {
		return 2;
	}

}
