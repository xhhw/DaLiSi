package com.zb.dalisi.expression.syntax.function;

import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;

public class Judge extends Function {

	@Override
	public int getArgumentNum() {
		return 3;
	}

	@Override
	protected Object executeFunction(Valuable[] arguments) {
		boolean condition = arguments[0].getBooleanValue();
		if(condition)
			return arguments[1].getValue();
		else
			return arguments[2].getValue();
	}

	@Override
	public String getName() {
		return "judge";
	}

	@Override
	public DataType[] getArgumentsDataType() {
		return new DataType[]{DataType.BOOLEAN, DataType.ANY, DataType.ANY};
	}
}
