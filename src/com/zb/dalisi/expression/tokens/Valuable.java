package com.zb.dalisi.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * 可取值符号接口
 * @author zhangbin2
 *
 */
public interface Valuable extends Token {
	/**
	 * 取得数据类型
	 * @return
	 */
	public DataType getDataType();
	
	/**
	 * 在数据缓存中的索引
	 * @return
	 */
	public int getIndex();
	
	/**
	 * 取数值
	 * @return
	 */
	public BigDecimal getNumberValue();
	
	/**
	 * 取字符串
	 * @return
	 */
	public String getStringValue();
	
	/**
	 * 去取字符
	 * @return
	 */
	public Character getCharValue();
	
	/**
	 * 取日期
	 * @return
	 */
	public Calendar getDateValue();
	
	/**
	 * 取布尔值
	 * @return
	 */
	public Boolean getBooleanValue();
	
	/**
	 * 取值，可使用instanceof判断具体类型
	 * @return
	 */
	public Object getValue();
}
