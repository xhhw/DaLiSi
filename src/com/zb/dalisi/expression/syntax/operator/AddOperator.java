package com.zb.dalisi.expression.syntax.operator;

import java.math.BigDecimal;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;

public class AddOperator extends BinaryOperator {

	public AddOperator() {
		super("ADD");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			result = a1.getNumberValue().add(a2.getNumberValue());
		} else if ( a1.getDataType() == DataType.INT || a1.getDataType() == DataType.LONG
				|| a2.getDataType() == DataType.INT || a2.getDataType() == DataType.LONG) {
			result = new BigDecimal(a1.getValue().toString()).add(new BigDecimal(a2.getValue().toString()));
				
		} else if (a1.getDataType() == DataType.STRING
				&& a2.getDataType() == DataType.STRING) {
			result = a1.getStringValue() + a2.getStringValue();
		} else if (a1.getDataType() == DataType.STRING
				&& a2.getDataType() == DataType.CHARACTER) {
			result = a1.getStringValue() + a2.getCharValue();
		} else if (a1.getDataType() == DataType.CHARACTER
				&& a2.getDataType() == DataType.STRING) {
			result = a1.getCharValue() + a2.getStringValue();
		} else {
			throw new ArgumentsMismatchException(arguments, "+");
		}
		return result;
	}

}
