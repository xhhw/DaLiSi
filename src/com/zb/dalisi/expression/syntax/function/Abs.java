package com.zb.dalisi.expression.syntax.function;

import java.math.BigDecimal;

import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;

public class Abs extends Function {

	@Override
	public String getName() {
		return "abs";
	}
	
	@Override
	public int getArgumentNum() {
		return 1;
	}
	
	@Override
	public DataType[] getArgumentsDataType() {
		return new DataType[]{DataType.NUMBER};
	}

	@Override
	protected Object executeFunction(Valuable[] arguments) {
		Valuable argument = arguments[0];
		return new BigDecimal(argument.getValue().toString()).abs();
	}


}
