package com.zb.dalisi.expression;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zb.dalisi.expression.lexical.LexicalAnalyzer;
import com.zb.dalisi.expression.lexical.LexicalException;
import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.syntax.SyntaxAnalyzer;
import com.zb.dalisi.expression.syntax.SyntaxException;
import com.zb.dalisi.expression.syntax.function.Function;
import com.zb.dalisi.expression.tokens.RuntimeValue;
import com.zb.dalisi.expression.tokens.TerminalToken;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.TokenType;
import com.zb.dalisi.expression.tokens.Valuable;

/**
 * ���ʽ
 * @author zhangbin2
 *
 */
public class Expression {
	private String expression;
	/**
	 * Token����
	 */
	private List<TerminalToken> tokens;
	
	/**
	 * �����������Ӧ��ֵ
	 */
	private Map<String, Valuable> variableTable = new HashMap<String, Valuable>();
	
	/**
	 * �����������Ӧ�ĺ�������
	 */
	private Map<String, Executable> functionTable = new HashMap<String, Executable>();
	
	/**
	 * ���ʽ���ս��
	 */
	private Valuable finalResult;
	
	/**
	 * �ʷ�������
	 */
	private LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
	
	/**
	 * �﷨������
	 */
	private SyntaxAnalyzer syntaxAnalyzer= new SyntaxAnalyzer();
	
	/**
	 * ��������Ĭ�ϲ��õ�scale
	 */
	public static int DEFAULT_DIVISION_SCALE = 16;
	
	/**
	 * ��������Ĭ��ʹ�õ����뷽ʽ
	 */
	public static RoundingMode DEFAULT_DIVISION_ROUNDING_MODE = RoundingMode.HALF_UP;
	
	public Expression() {}
	
	public Expression(String expression) {
		setExpression(expression);
	}
	
	public Expression(InputStream source) throws IOException {
		setExpression(source);
	}
	
	public Expression(Reader source) throws IOException {
		setExpression(source);
	}
	
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public void setExpression(InputStream source) throws IOException {
		StringBuilder sb = new StringBuilder();
	    try {
	      int c;
	      while ((c = source.read()) != -1)
	        sb.append((char)c);
	      setExpression(sb.toString());
	    } finally {
	    	source.close();
	    }
	}
	
	public void setExpression(Reader source) throws IOException {
		StringBuilder sb = new StringBuilder();
	    try {
	      int c;
	      while ((c = source.read()) != -1)
	        sb.append((char)c);
	      setExpression(sb.toString());
	    } finally {
	    	source.close();
	    }
	}
	
	public String getExpression() {
		return expression;
	}
	
	public List<TerminalToken> getTokens() {
		return tokens;
	}
	
	/**
	 * ���б����������Ƚ��дʷ�������Ȼ�󷵻����б�����
	 * @return
	 * @throws LexicalException
	 */
	public Set<String> getVariableNames() throws LexicalException {
		//�ʷ����������Token����
		tokens = lexicalAnalyzer.analysis(expression, functionTable);
		Set<String> variableNames = new HashSet<String>();
		for(TerminalToken terminalToken : tokens)
			if(terminalToken.getTokenType() == TokenType.VARIABLE)
				variableNames.add(terminalToken.getText());
		return variableNames;
	}
	
	/**
	 * ���ñ���ֵ
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setVariableValue(String name, Object value) {
		RuntimeValue runtimeValue = TokenBuilder.buildRuntimeValue(value);
		variableTable.put(name, runtimeValue);
	}
	
	/**
	 * ��ȡ����ֵ
	 * @param name ������
	 * @return
	 */
	public Valuable getVariableValue(String name) {
		return variableTable.get(name);
	}
	
	public Map<String, Valuable> getVariableTable() {
		return variableTable;
	}
	
	public void removeVariable(String name) {
		variableTable.remove(name);
	}
	
	/**
	 * ��������
	 * @param function
	 */
	public void addFunction(Function function) {
		function.checkFunctionDefinition();
		functionTable.put(function.getName(), function);
	}
	
	public Executable getFunction(String functionName) {
		return functionTable.get(functionName);
	}
	
	public Map<String, Executable> getFunctionTable() {
		return functionTable;
	}
	
	public void removeFunction(String functionName) {
		functionTable.remove(functionName);
	}
	
	public Valuable getFinalResult() {
		return finalResult;
	}
	
	/**
	 * �������ʽ
	 * @return �������
	 * @throws LexicalException �ʷ������쳣
	 * @throws SyntaxException �﷨�����쳣
	 */
	public Valuable evaluate() throws LexicalException, SyntaxException {
		//�ʷ����������Token����
		tokens = lexicalAnalyzer.analysis(expression, functionTable);
		
		//�﷨�������������ս��
		finalResult = syntaxAnalyzer.analysis(tokens, variableTable);
		//���±���ֵ
		variableTable = syntaxAnalyzer.getVariableTable();
		return finalResult;
	}
	
	public void clear() {
		tokens.clear();
		finalResult = null;
		variableTable.clear();
		functionTable.clear();
	}
	
	public void clearTokens() {
		tokens.clear();
	}
	
	public void clearVariableTable() {
		variableTable.clear();
	}
	
	public void clearFunctionTable() {
		functionTable.clear();
	}
}