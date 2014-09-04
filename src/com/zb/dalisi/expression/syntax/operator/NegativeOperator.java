package com.zb.dalisi.expression.syntax.operator;

import java.math.BigDecimal;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;


public class NegativeOperator extends UnaryOperator {

	public NegativeOperator() {
		super("NEGATIVE");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable argument = arguments[0];
		if (argument.getDataType() == DataType.NUMBER) {
			result = new BigDecimal("0").subtract(argument.getNumberValue());
		} else if ( argument.getDataType() == DataType.INT || argument.getDataType() == DataType.LONG) {
			result = new BigDecimal(0).subtract(new BigDecimal(argument.getValue().toString()));
				
		} else {
			throw new ArgumentsMismatchException(arguments, "-");
		}
		return result;
	}

}
