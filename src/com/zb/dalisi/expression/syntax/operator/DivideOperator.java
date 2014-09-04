package com.zb.dalisi.expression.syntax.operator;

import java.math.BigDecimal;

import com.zb.dalisi.expression.Expression;
import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;


public class DivideOperator extends BinaryOperator {

	public DivideOperator() {
		super("DIVIDE");
	}

	@Override
	public Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = null;
		Valuable a1 = arguments[0];
		Valuable a2 = arguments[1];
		if (a1.getDataType() == DataType.NUMBER
				&& a2.getDataType() == DataType.NUMBER) {
			//除数不能为零
			if (a2.getNumberValue().compareTo(new BigDecimal("0")) == 0)
				throw new ArithmeticException("Divided by zero.");
			
			//使用Expression中提供的scale和舍入方式
			result = a1.getNumberValue().divide(a2.getNumberValue(), 
					Expression.DEFAULT_DIVISION_SCALE, Expression.DEFAULT_DIVISION_ROUNDING_MODE);
		} else if ( a1.getDataType() == DataType.INT || a1.getDataType() == DataType.LONG
				|| a2.getDataType() == DataType.INT || a2.getDataType() == DataType.LONG) {
			//除数不能为零
			if (new BigDecimal(a2.getValue().toString()).compareTo(new BigDecimal("0")) == 0)
				throw new ArithmeticException("Divided by zero.");
			
			//使用Expression中提供的scale和舍入方式
			result = new BigDecimal(a1.getValue().toString()).divide(new BigDecimal(a2.getValue().toString()), 
					Expression.DEFAULT_DIVISION_SCALE, Expression.DEFAULT_DIVISION_ROUNDING_MODE);
		} else {
			throw new ArgumentsMismatchException(arguments, "/");
		}
		return result;
	}
}
