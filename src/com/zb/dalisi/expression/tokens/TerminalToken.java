package com.zb.dalisi.expression.tokens;

/**
 * �ս��
 * @author zhangbin2
 *
 */
public abstract class TerminalToken implements Token {

	/**
	 * �к�
	 */
	private final int line;
	
	/**
	 * �к�
	 */
	private final int column;
	
	private final String text;
	
	public TerminalToken(TokenBuilder builder) {
		line = builder.getLine();
		column = builder.getColumn();
		text = builder.getText();
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColumn() {
		return column;
	}
	
	public String getText() {
		return text;
	}
	
	/**
	 * ���ķ��еķ����Ƿ�ƥ��
	 * @param target
	 * @return
	 */
	public abstract boolean equalsInGrammar(TerminalToken target);
}
