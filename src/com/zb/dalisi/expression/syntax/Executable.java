package com.zb.dalisi.expression.syntax;

import com.zb.dalisi.expression.tokens.Valuable;

/**
 * 可执行动作接口
 * @author zhangbin2
 *
 */
public interface Executable {
	/**
	 * 参数个数
	 * @return
	 */
	public int getArgumentNum();
	
	/**
	 * 执行运算，返回结果
	 * @param arguments
	 * @return
	 * @throws ArgumentsMismatchException
	 */
	public Valuable execute(Valuable[] arguments) throws ArgumentsMismatchException;
	
}

