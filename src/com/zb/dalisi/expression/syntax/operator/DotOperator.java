package com.zb.dalisi.expression.syntax.operator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.ObjectFieldToken;
import com.zb.dalisi.expression.tokens.ObjectMethodToken;
import com.zb.dalisi.expression.tokens.Token;
import com.zb.dalisi.expression.tokens.TokenType;
import com.zb.dalisi.expression.tokens.Valuable;

public class DotOperator extends BinaryOperator {

	public DotOperator() {
		super("DOT");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException {
		// TODO Auto-generated method stub
		Valuable objectValuable = arguments[0];
		Token token = (Token) arguments[1];
		if (token.getTokenType() == TokenType.FIELD){
			ObjectFieldToken objectFieldToken = (ObjectFieldToken) token;
			if (objectValuable.getDataType() == DataType.ANY) {
				try {
					Object obj = objectValuable.getValue();
					String attr = objectFieldToken.getText();
					Field field = obj.getClass().getDeclaredField(attr);
					objectFieldToken.setObj(obj);
					objectFieldToken.setField(field);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		else if (token.getTokenType() == TokenType.METHOD){
			ObjectMethodToken objectFieldToken = (ObjectMethodToken) token;
			if (objectValuable.getDataType() == DataType.ANY) {
				try {
					Object obj = objectValuable.getValue();
					String methodName = objectFieldToken.getText();
					Method function = null;
					Method method[]=obj.getClass().getDeclaredMethods();//.getMethods();
					for(int i=0;i<method.length;++i){
						if (method[i].getName().equals(methodName)){
							function = method[i];
							break;
						}
					}
					if (function == null){
						throw new ArgumentsMismatchException(arguments, "method name can not find");
					}
					objectFieldToken.setObj(obj);
					objectFieldToken.setMethod(function);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			} 
		}else {
			throw new ArgumentsMismatchException(arguments, ".");
		}
		
		return token;
	}

}
