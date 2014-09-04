package com.zb.dalisi.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

import com.zb.dalisi.expression.utils.ValueUtil;

/**
 * ��ȡֵ����
 * @author zhangbin2
 *
 */
public abstract class ValueToken extends TerminalToken implements Valuable {
	
	/**
	 * ��������
	 */
	private DataType dataType;
	
	/**
	 * �����ݻ����е�����
	 */
	private int index;
	
	public ValueToken(TokenBuilder builder) {
		super(builder);
		dataType = builder.getDataType();
		index = builder.getIndex();
	}
	
	protected void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	protected void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
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
