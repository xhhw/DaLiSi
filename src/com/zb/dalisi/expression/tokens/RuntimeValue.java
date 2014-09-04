package com.zb.dalisi.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

import com.zb.dalisi.expression.utils.ValueUtil;

/**
 * ����ʱ���м�ֵ
 * @author zhangbin2
 *
 */
public final class RuntimeValue implements Valuable {

	/**
	 * ��������
	 */
	private DataType dataType;
	
	/**
	 * �����ݻ����е�����
	 */
	private int index;
	
	public RuntimeValue(TokenBuilder builder) {
		dataType = builder.getDataType();
		index = builder.getIndex();
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public int getIndex() {
		return index;
	}
	
	public TokenType getTokenType() {
		return TokenType.RUNTIME_VALUE;
	}
	
	public BigDecimal getNumberValue() {
		return ValueUtil.getNumberValue(this);
	}
	
	public String getStringValue() {
		return ValueUtil.getStringValue(this);
	}
	
	public Character getCharValue() {
		return ValueUtil.getCharValue(this);
	}
	
	public Calendar getDateValue() {
		return ValueUtil.getDateValue(this);
	}
	
	public Boolean getBooleanValue() {
		return ValueUtil.getBooleanValue(this);
	}
	
	public Object getValue() {
		return ValueUtil.getValue(this);
	}

}
