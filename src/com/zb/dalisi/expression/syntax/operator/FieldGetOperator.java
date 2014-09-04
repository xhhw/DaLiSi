package com.zb.dalisi.expression.syntax.operator;

import java.lang.reflect.Field;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.ObjectFieldToken;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.Valuable;

public class FieldGetOperator extends UnaryOperator {

	public FieldGetOperator() {
		super("FIELD_GET");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		// TODO Auto-generated method stub
		ObjectFieldToken fieldToken = (ObjectFieldToken) arguments[0];
		
		if (fieldToken.getIndex() >= 0) {
			return fieldToken.getValue();
		}

		Object result = null;
		if (fieldToken.getObj() != null && fieldToken.getField() != null) {
			try {
				Object obj = fieldToken.getObj();
				Field field = fieldToken.getField();
				field.setAccessible(true);
				result = field.get(obj);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			throw new ArgumentsMismatchException(arguments, ".FIELD_GET");
		}
		
		if (fieldToken.getIndex() < 0) {
			fieldToken.assignWith(TokenBuilder.buildRuntimeValue(result));
		}

		return result;
	}

}
