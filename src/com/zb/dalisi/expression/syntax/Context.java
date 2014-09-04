package com.zb.dalisi.expression.syntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.zb.dalisi.expression.tokens.Valuable;

/**
 * ������
 * @author zhangbin2
 *
 */
public class Context {
	
	/**
	 * ��Ч��־����Ӧ���������ڷ�֧�ṹ��������
	 */
	private boolean effective;
	
	/**
	 * �������еı��������Ӧ��ֵ
	 */
	private Map<String, Valuable> variableTable;
	
	/**
	 * �����ĵĿ�ʼλ��
	 */
	private int startIndex = -1;
	
	public Context(boolean effective, Map<String, Valuable> variableTable, int startIndex) {
		this.effective = effective;
		this.variableTable = variableTable;
		this.startIndex = startIndex;
	}

	public boolean isEffective() {
		return effective;
	}
	
	public Map<String, Valuable> getVariableTable() {
		return this.variableTable;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public Valuable getVariableValue(String variableName) {
		return variableTable.get(variableName);
	}
	
	public void setVariableValue(String variableName, Valuable value) {
		variableTable.put(variableName, value);
	}
	
	/**
	 * ���ڱ������Ĵ�����������
	 * @param effective
	 * @param startIndex
	 * @return
	 */
	public Context constructUpon(boolean effective, int startIndex) {
		Map<String, Valuable> variableTableUpon = new HashMap<String, Valuable>();
		//���������ĵı���ȫ�����Ƶ���������
		variableTableUpon.putAll(variableTable);
		return new Context(effective, variableTableUpon, startIndex);
	}
	
	/**
	 * ���±�������
	 * @param context
	 */
	public void update(Context context) {
		Set<String> variableNames = variableTable.keySet();
		Set<String> targetVariableNames = context.getVariableTable().keySet();
		//�������context�е�ĳ�����ڱ���������Ҳ�ж��壬�򽫸ñ�����ֵ���ǵ�����������
		for(String variableName : variableNames) 
			if(targetVariableNames.contains(variableName))
				setVariableValue(variableName, context.getVariableValue(variableName));
	}
}
