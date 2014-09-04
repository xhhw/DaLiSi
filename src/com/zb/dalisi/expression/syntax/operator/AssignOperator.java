package com.zb.dalisi.expression.syntax.operator;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;
import com.zb.dalisi.expression.tokens.VariableToken;

public class AssignOperator extends BinaryOperator {

	public AssignOperator() {
		super("ASSIGN");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		VariableToken variable = (VariableToken) arguments[0];
		Valuable value = arguments[1];
		//������δ���壬ֱ�Ӹ�ֵ�����Ѷ��壬�����ж����������Ƿ�ƥ���ٸ�ֵ
		if (variable.getIndex() < 0) {
			variable.assignWith(value);
		} else if (variable.getDataType() == value.getDataType() || value.getDataType() == DataType.NUMBER) {
			variable.assignWith(value);
		} else
			throw new ArgumentsMismatchException(
					"Type mismatch in assignment: cannot convert from "
							+ value.getDataType().name() + " to "
							+ variable.getDataType().name() + ".");
		return variable.getValue();
	}

}
