package com.zb.dalisi.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * ��ȡֵ���Žӿ�
 * @author zhangbin2
 *
 */
public interface Valuable extends Token {
	/**
	 * ȡ����������
	 * @return
	 */
	public DataType getDataType();
	
	/**
	 * �����ݻ����е�����
	 * @return
	 */
	public int getIndex();
	
	/**
	 * ȡ��ֵ
	 * @return
	 */
	public BigDecimal getNumberValue();
	
	/**
	 * ȡ�ַ���
	 * @return
	 */
	public String getStringValue();
	
	/**
	 * ȥȡ�ַ�
	 * @return
	 */
	public Character getCharValue();
	
	/**
	 * ȡ����
	 * @return
	 */
	public Calendar getDateValue();
	
	/**
	 * ȡ����ֵ
	 * @return
	 */
	public Boolean getBooleanValue();
	
	/**
	 * ȡֵ����ʹ��instanceof�жϾ�������
	 * @return
	 */
	public Object getValue();
}
