package com.zb.dalisi.expression.syntax.operator;

import java.math.BigDecimal;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;


public class ModOperator extends BinaryOperator {

	public ModOperator() {
		super("MOD");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			if (a2.getNumberValue().compareTo(new BigDecimal("0")) == 0)
				throw new ArithmeticException("Divided by zero.");
			result = a1.getNumberValue().divideAndRemainder(a2.getNumberValue())[1];
		} else if ( a1.getDataType() == DataType.INT || a1.getDataType() == DataType.LONG
				|| a2.getDataType() == DataType.INT || a2.getDataType() == DataType.LONG) {
			if (new BigDecimal(a2.getValue().toString()).compareTo(new BigDecimal("0")) == 0)
				throw new ArithmeticException("Divided by zero.");
			
			result = new BigDecimal(a1.getValue().toString()).divideAndRemainder(new BigDecimal(a2.getValue().toString()));
				
		} else {
			throw new ArgumentsMismatchException(arguments, "%");
		}
		return result;
	}
}
