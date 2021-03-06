package com.zb.dalisi.expression.tokens;

import java.math.BigDecimal;
import java.util.Calendar;

import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.utils.DataCache;

/**
 * 创建token
 * @author zhangbin2
 *
 */
public class TokenBuilder {
	/**
	 * 行号
	 */
	private int line = -1;
	
	/**
	 * 列号
	 */
	private int column = -1;
	
	/**
	 * 字面内容
	 */
	private String text;
	
	/**
	 * 数据类型
	 */
	private DataType dataType;
	
	/**
	 * 在数据缓存中的索引
	 */
	private int index = -1;
	
	/**
	 * 函数定义
	 */
	private Executable executable;
	
	/**
	 * 上下文操作
	 */
	private ContextOperation contextOperation;
	
	/**
	 * 是否为被赋值的变量
	 */
	private boolean toBeAssigned = false;
	
	public TokenBuilder() {}
	
	public static TokenBuilder getBuilder() {
		return new TokenBuilder();
	}
	
	public TokenBuilder line(int val) {
		line = val;
		return this;
	}
	
	public int getLine() {
		return line;
	}
	
	public TokenBuilder column(int val) {
		column = val;
		return this;
	}
	
	public int getColumn() {
		return column;
	}
	
	public TokenBuilder text(String val) {
		text = val;
		return this;
	}
	
	public String getText() {
		return text;
	}
	
	public TokenBuilder dataType(DataType val) {
		dataType = val;
		return this;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public TokenBuilder index(int val) {
		index = val;
		return this;
	}
	
	public int getIndex() {
		return index;
	}
	
	public TokenBuilder contextOperation(ContextOperation val) {
		contextOperation = val;
		return this;
	}
	
	public ContextOperation getContextOperation() {
		return contextOperation;
	}
	
	public TokenBuilder executable(Executable val) {
		executable = val;
		return this;
	}
	
	public Executable getExecutable() {
		return executable;
	}
	
	public TokenBuilder toBeAssigned(boolean val) {
		toBeAssigned = val;
		return this;
	}
	
	public boolean isToBeAssigned() {
		return toBeAssigned;
	}
	
	public NonterminalToken buildNT() {
		return new NonterminalToken();
	}
	
	public ExecutionToken buildExecution() {
		return new ExecutionToken(this);
	}
	
	public ContextOperationToken buildContextOperation() {
		return new ContextOperationToken(this);
	}
	
	public DelimiterToken buildDelimiter() {
		return new DelimiterToken(this);
	}
	
	public KeyToken buildKey() {
		return new KeyToken(this);
	}
	
	public FunctionToken buildFunction() {
		return new FunctionToken(this);
	}

	public ObjectMethodToken buildObjectMethod() {//zhangbin2
		return new ObjectMethodToken(this);
	}
	public ObjectFieldToken buildObjectField() {//zhangbin2
		return new ObjectFieldToken(this);
	}
	
	public ConstToken buildConst() {
		return new ConstToken(this);
	}
	
	public VariableToken buildVariable() {
		return new VariableToken(this);
	}
	
	public RuntimeValue buildRuntimeValue() {
		return new RuntimeValue(this);
	}
	
	public static RuntimeValue buildRuntimeValue(Object value) {
		RuntimeValue runtimeValue = null;
		if(value == null) {
			throw new RuntimeException("Ilegal value : null");
		} else if(value instanceof Integer) {
			runtimeValue = getBuilder().dataType(DataType.INT)
							.index(DataCache.getObjIndex(value) )
							.buildRuntimeValue();
		} else if (value instanceof Double) {
			runtimeValue = getBuilder().dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex( BigDecimal.valueOf((Double)value)) )
							.buildRuntimeValue();
		} else if (value instanceof Long) {
			runtimeValue = getBuilder().dataType(DataType.LONG)
					.index(DataCache.getObjIndex(value) )
					.buildRuntimeValue();
		} else if (value instanceof BigDecimal) {
			runtimeValue = getBuilder().dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex((BigDecimal)value))
							.buildRuntimeValue();
		} else if (value instanceof String) {
			runtimeValue = getBuilder().dataType(DataType.STRING)
							.index(DataCache.getStringIndex((String)value))
							.buildRuntimeValue();
		} else if (value instanceof Character) {
			runtimeValue = getBuilder().dataType(DataType.CHARACTER)
							.index(DataCache.getCharIndex((Character)value))
							.buildRuntimeValue();
		} else if(value instanceof Boolean) {
			runtimeValue = getBuilder().dataType(DataType.BOOLEAN)
							.index(DataCache.getBooleanIndex((Boolean)value))
							.buildRuntimeValue();
		} else if (value instanceof Calendar) {
			runtimeValue = getBuilder().dataType(DataType.DATE)
							.index(DataCache.getDateIndex((Calendar)value))
							.buildRuntimeValue();
		} else if(value instanceof RuntimeValue) {
			runtimeValue = (RuntimeValue) value;
		} else if(value instanceof Object) {
			runtimeValue = getBuilder().dataType(DataType.ANY)
					.index(DataCache.getObjIndex(value))
					.buildRuntimeValue();
		} else
			throw new RuntimeException("Ilegal value : " + value);
		return runtimeValue;
	}

}
