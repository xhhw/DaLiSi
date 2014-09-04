package com.zb.dalisi.expression.syntax.operator;

import java.lang.reflect.Field;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.ObjectFieldToken;
import com.zb.dalisi.expression.tokens.Valuable;

public class FieldSetOperator extends BinaryOperator {

	public FieldSetOperator() {
		super("FIELD_SET");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		// TODO Auto-generated method stub
		ObjectFieldToken fieldToken = (ObjectFieldToken) arguments[0];
		Valuable value = arguments[1];
		if (fieldToken.getIndex() < 0) {
			fieldToken.assignWith(value);
		} else if (fieldToken.getDataType() == value.getDataType()) {
			fieldToken.assignWith(value);
		} else {
			throw new ArgumentsMismatchException(arguments, ".FIELD_SET_ASSIGN");
		}
		
		if (fieldToken.getObj() != null && fieldToken.getField() != null) {
			try {
				Object obj = fieldToken.getObj();
				Field field = fieldToken.getField();
				field.setAccessible(true);
				field.set(obj, value.getValue());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			throw new ArgumentsMismatchException(arguments, ".FIELD_SET");
		}

		return fieldToken.getValue();
	}

}
