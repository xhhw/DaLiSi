package com.zb.dalisi.expression.lexical;

import com.zb.dalisi.expression.lexical.dfa.DFAMidState;

/**
 * ¥ ∑®“Ï≥£
 * @author zhangbin2
 *
 */
@SuppressWarnings("serial")
public class LexicalException extends RuntimeException{
	
	public LexicalException() {
		super();
	}

	public LexicalException(String message) {
		super(message);
	}
	
	public LexicalException(String message, int line, int column) {
		this(message + " At line:" + line + ", column:" + column + ".");
	}
	
	public LexicalException(DFAMidState state, int line, int column) {
		this(state.getErrorMessage(), line, column);
	}
}
