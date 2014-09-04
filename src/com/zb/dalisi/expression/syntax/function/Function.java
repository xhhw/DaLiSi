package com.zb.dalisi.expression.syntax.function;

import com.zb.dalisi.expression.syntax.ArgumentsMismatchException;
import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.Valuable;

/**
 * �������������
 * @author zhangbin2
 *
 */
public abstract class Function implements Executable{

	/**
	 * ������
	 */
	protected String functionName = getName();
	
	/**
	 * ������������
	 */
	protected DataType[] argumentsDataType = getArgumentsDataType() == null ? new DataType[0] : getArgumentsDataType();
	
	/**
	 * ���غ�����
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * ���غ��������������飬��������������ʱ�����в������ͱ�����ͬ�����������ṩһ����������
	 * @return
	 */
	public abstract DataType[] getArgumentsDataType();
	
	/**
	 * ���غ���������������������С��0ʱ����ʾ������������
	 */
	public abstract int getArgumentNum();
	
	/**
	 * ִ�к���
	 */
	public final Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException {
		if(getArgumentNum() < 0) {	//�ɱ����
			//�����������Ƿ�һ��
			for(Valuable argument : arguments) {
				if(argumentsDataType[0] == DataType.ANY)
					break;
				if ( argumentsDataType[0] == DataType.NUMBER 
						&& (argument.getDataType() == DataType.INT 
						  || argument.getDataType() == DataType.LONG )) {
					break;
				} 
				if(argument.getDataType() != argumentsDataType[0])
					throw new ArgumentsMismatchException(arguments, toString());
			}
		} else if(getArgumentNum() == arguments.length) {
			int argumentNum = getArgumentNum(); 
			for(int i=0; i<argumentNum; i++) {
				if(argumentsDataType[i] == DataType.ANY) {
					continue;
				} else if ( argumentsDataType[i] == DataType.NUMBER 
						&& (arguments[i].getDataType() == DataType.INT 
						  || arguments[i].getDataType() == DataType.LONG )) {
					continue;
				} else if (argumentsDataType[i] != arguments[i].getDataType()){
					throw new ArgumentsMismatchException(arguments, toString());
				}
			}
		} else {
			throw new ArgumentsMismatchException(arguments, toString());
		}
		//ִ�к���
		Object result = executeFunction(arguments);
		return TokenBuilder.buildRuntimeValue(result);
	}

	/**
	 * ����ִ���߼�
	 * @param arguments
	 * @return
	 */
	protected abstract Object executeFunction(Valuable[] arguments);
	
	/**
	 * ��麯������
	 */
	public final void checkFunctionDefinition() {
		if(functionName == null || "".equals(functionName))
			throw new RuntimeException("Function name can not be empty.");
		
		if(getArgumentNum() >= 0) {
			if(argumentsDataType.length != getArgumentNum()) {
				throw new RuntimeException("Function definition error:" + getName() + ".");
			}
		} else {
			//��������С��0������������ʱ����Ҫ�ṩһ���������ͣ����в������ͱ�����ͬ
			if(argumentsDataType.length != 1) {
				throw new RuntimeException("Function definition error:" + getName() + ".");
			}
		}
	}
	
	@Override
	public final String toString() {
		StringBuilder signature = new StringBuilder();
		signature.append(functionName).append('(');
		if(getArgumentNum() >= 0) {
			int argumentNum = getArgumentNum();
			for(int i=0; i<argumentNum; i++) {
				if(i == argumentNum-1)
					signature.append(argumentsDataType[i].name());
				else
					signature.append(argumentsDataType[i].name()).append(',');
			}
		} else {
			signature.append(argumentsDataType[0].name()).append("...");
		}
		signature.append(')');
		return signature.toString();
	}

}
