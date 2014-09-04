package com.zb.dalisi.expression.syntax.operator;

import java.math.BigDecimal;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;

public class MinusOperator extends BinaryOperator {

	public MinusOperator() {
		super("MINUS");
	}

	public static final String name = "MINUS";

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			result = a1.getNumberValue().subtract(a2.getNumberValue());
		} else if ( a1.getDataType() == DataType.INT || a1.getDataType() == DataType.LONG
				|| a2.getDataType() == DataType.INT || a2.getDataType() == DataType.LONG) {
			result = new BigDecimal(a1.getValue().toString()).subtract(new BigDecimal(a2.getValue().toString()));
				
		} else {
			throw new ArgumentsMismatchException(arguments, "-");
		}
		return result;
	}

}
