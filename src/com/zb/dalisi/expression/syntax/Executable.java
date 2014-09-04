package com.zb.dalisi.expression.syntax;

import com.zb.dalisi.expression.tokens.Valuable;

/**
 * ��ִ�ж����ӿ�
 * @author zhangbin2
 *
 */
public interface Executable {
	/**
	 * ��������
	 * @return
	 */
	public int getArgumentNum();
	
	/**
	 * ִ�����㣬���ؽ��
	 * @param arguments
	 * @return
	 * @throws ArgumentsMismatchException
	 */
	public Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException;
	
}

