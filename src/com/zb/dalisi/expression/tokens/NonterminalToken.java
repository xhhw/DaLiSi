package com.zb.dalisi.expression.tokens;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ���ս��
 * @author zhangbin2
 *
 */
public final class NonterminalToken implements Token {
	
	private Map<TerminalToken[], Token[]> productions = new HashMap<TerminalToken[], Token[]>();
	
	/**
	 * ��Ӳ���ʽ
	 * @param selectCollection ѡ��
	 * @param production ����ʽ
	 */
	public void addProduction(TerminalToken[] selectCollection, Token[] production) {
		productions.put(selectCollection, production);
	}
	
	/**
	 * ����������ս�����Ҳ���ʽ
	 * @param target ������ս��
	 * @return
	 */
	public Token[] getProduction(TerminalToken target) {
		//�����ս�����в���ʽ��ѡ��
		Set<TerminalToken[]> selectCollections = productions.keySet();
		for(TerminalToken[] selectCollection : selectCollections) {
			//���������ս��������ĳһѡ�񼯣��򷵻ظ�ѡ�񼯶�Ӧ�Ĳ���ʽ
			for(TerminalToken token : selectCollection)
				if(token.equalsInGrammar(target))
					return productions.get(selectCollection);
		}
		return null;
	}
	
	public TokenType getTokenType() {
		return TokenType.NT;
	}
	
}

