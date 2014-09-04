package com.zb.dalisi.expression.tokens;

/**
 * 鍏抽敭�?楃鍙�
 * @author shanxuecheng
 *
 */
public final class KeyToken extends TerminalToken {

	public KeyToken(TokenBuilder builder) {
		super(builder);
	}
	
	public TokenType getTokenType() {
		return TokenType.KEY;
	}

	@Override
	public boolean equalsInGrammar(TerminalToken target) {
		if(!(target instanceof KeyToken))
			return false;
		return this.getText().equals(target.getText());
	}

}
