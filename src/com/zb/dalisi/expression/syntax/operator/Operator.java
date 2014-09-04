package com.zb.dalisi.expression.syntax.operator;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.Valuable;

/**
 * ������
 * @author zhangbin2
 *
 */
public abstract class Operator implements Executable {

	/**
	 * ��������
	 */
	private final String operatorName;

	public Operator(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * ִ�в�����
	 */
	public Valuable execute(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = operate(arguments);
		if (result instanceof Valuable){
			return (Valuable) result;
		}
		return TokenBuilder.buildRuntimeValue(result);
	}

	/**
	 * �ṩ������ִ���߼�
	 * @param arguments
	 * @return
	 * @throws ArgumentsMismatchException
	 */
	protected abstract Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException;

}

