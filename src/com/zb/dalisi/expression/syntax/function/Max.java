package com.zb.dalisi.expression.syntax.function;

import java.math.BigDecimal;

import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;

public class Max extends Function {

	@Override
	public int getArgumentNum() {
		return -1;
	}

	@Override
	public Object executeFunction(Valuable[] arguments) {
		BigDecimal result;
		if(arguments.length == 0) {
			result = new BigDecimal("0");
		} else {
			result = new BigDecimal(arguments[0].getValue().toString());
			for(int i=1; i<arguments.length; i++)
				if(result.compareTo(new BigDecimal(arguments[i].getValue().toString())) < 0)
					result = arguments[i].getNumberValue();
		}
		return result;
	}

	@Override
	public String getName() {
		return "max";
	}

	@Override
	public DataType[] getArgumentsDataType() {
		return new DataType[]{DataType.NUMBER};
	}
}
