package com.zb.dalisi.expression.lexical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zb.dalisi.expression.tokens.DelimiterToken;
import com.zb.dalisi.expression.tokens.TokenBuilder;

/**
 * �ʷ���������
 * @author zhangbin2
 *
 */
public class LexicalConstants {
	private LexicalConstants() {}
	
	/**
	 * ��Ԫ��
	 */
	public static final String DOLLAR_PATTERN = "\\$";//zhangbin2
	
	/**
	 * ����
	 */
	public static final String DIGITS_PATTERN = "\\d";
	public static final String LONG_PATTERN = "[L]";//zhangbin2
	
	/**
	 * ��ĸ���»���
	 */
	public static final String LETTER_UNDERLINE_PATTERN = "[a-zA-Z_]";
	
	/**
	 * ��� ; ( ) , + - * / = > < | & ! { } . %
	 */
	public static final String DELIMITER_PATTERN = "[;(),\\+\\-\\*/=><|&!{}\\.%]";
	
	/**
	 * �ǽ��
	 */
	public static final String NOT_DELIMITER_PATTERN = "[^;(),\\+\\-\\*/=><|&!{}\\.]";
	
	/**
	 * �����ţ���Ϊ���ڳ����Ŀ�ʼ�ַ�
	 */
	public static final String LEFT_SQUARE_BRACKET_PATTERN = "\\[";
	
	/**
	 * �ҷ����ţ���Ϊ���ڳ����Ľ����ַ�
	 */
	public static final String RIGHT_SQUARE_BRACKET_PATTERN = "]";
	
	/**
	 * �����ţ���Ϊ���ڳ����Ŀ�ʼ�ͽ����ַ�
	 */
	public static final String SINGLE_QUOTES_PATTERN = "'";
	
	/**
	 * ˫����, ��Ϊ�ַ��������Ŀ�ʼ�ͽ����ַ�
	 */
	public static final String DOUBLE_QUOTES_PATTERN = "\"";
	
	/**
	 * С����
	 */
	public static final String DECIMAL_POINT_PATTERN = "\\.";
	
	/**
	 * ָ������, E/e
	 */
	public static final String EXPONENT_PATTERN = "[Ee]";
	
	/**
	 * ����ĸ���»��ߡ�С���㡢����֮��������ַ�
	 */
	public static final String NOT_LETTER_UNDERLINE_POINT_DIGIT_PATTERN = "[^a-zA-Z_\\.\\d]";
	
	/**
	 * ����ĸ���»��ߡ�����֮��������ַ�
	 */
	public static final String NOT_LETTER_UNDERLINE_DIGIT_PATTERN = "[^a-zA-Z_\\d]";
	
	/**
	 * ������
	 */
	public static final String POSITIVE_NEGATIVE_PATTERN = "[\\+\\-]";
	
	/**
	 * �����ַ�
	 */
	public static final String ANY_CHAR_PATTERN = ".";
	
	/**
	 * ���֡����ߡ�ð�źͿհ��ַ������ڳ����е��ַ���
	 */
	public static final String DATE_FORMAT_PATTERN = "[\\d\\-\\s:]";
	
	/**
	 * ��б�ܣ����ڱ�ʶת���ַ�
	 */
	public static final String BACKSLASH_PATTERN = "\\\\";
	
	/**
	 * �ǵ����š��Ƿ�б��
	 */
	public static final String NOT_SINGLEQUOTES_BACKSLASH_PATTERN = "[^'\\\\]";
	
	/**
	 * ��Ч��ת���ַ�: \\ \b \t \n \f \r \' \"
	 */
	public static final String ESCAPE_PATTERN = "[\\\\btnfr\'\"]";
	
	/**
	 * ��˫���š��Ƿ�б��
	 */
	public static final String NOT_DOUBLEQUOTES_BACKSLASH_PATTERN = "[^\"\\\\]";
	
	/**
	 * �հ��ַ�
	 */
	public static final String BLANK_PATTERN = "\\s";
	
	/**
	 * �ǿհ��ַ�
	 */
	public static final String NOT_BLANK_PATTERN =  "\\S";
	
	/**
	 * ���ڸ�ʽ, [yyyy-MM-dd]
	 */
	public static final String DATE_PATTERN = "\\[\\d{4}-\\d{1,2}-\\d{1,2}\\]";
	
	/**
	 * ���ڸ�ʽ����ȷ����, [yyyy-MM-dd HH:mm:ss]
	 */
	public static final String ACCURATE_DATE_PATTERN = "\\[\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}\\]";
	
	/**
	 * ע�ͣ�ע����##��ʼ����ĩ���������ܿ��У�
	 */
	public static final String COMMENT_PATTERN = "##.*";
	
	/**
	 * ��ֵ����
	 */
	public static final DelimiterToken ASSIGN_TOKEN = TokenBuilder.getBuilder().text("=").buildDelimiter();
	
	/**
	 * �ؼ���
	 */
	public static final List<String> KEY_WORDS;
	
	/**
	 * ���ַ����
	 */
	public static final List<String> SINGLE_DELIMITERS;
	
	/**
	 * ˫�ַ����
	 */
	public static final List<String> DOUBLE_DELIMITERS;
	
	public static final List<String> OPERATORS;
	
	static {
		List<String> keys = new ArrayList<String>();
		keys.add("if");
		keys.add("else");
		keys.add("then");
		keys.add("endif");
		keys.add("var");
		keys.add("while");
		keys.add("for");
		keys.add("do");
		KEY_WORDS = Collections.unmodifiableList(keys);
		
		List<String> doubleDelimiters = new ArrayList<String>();
		doubleDelimiters.add(">=");
		doubleDelimiters.add("<=");
		doubleDelimiters.add("==");
		doubleDelimiters.add("!=");
		doubleDelimiters.add("&&");
		doubleDelimiters.add("||");
		DOUBLE_DELIMITERS = Collections.unmodifiableList(doubleDelimiters);
		
		List<String> singleDelimiters = new ArrayList<String>();
		singleDelimiters.add("+");
		singleDelimiters.add("-");
		singleDelimiters.add("*");
		singleDelimiters.add("/");
		singleDelimiters.add("%");
		singleDelimiters.add("=");
		singleDelimiters.add(">");
		singleDelimiters.add("<");
		singleDelimiters.add("!");
		singleDelimiters.add(";");
		singleDelimiters.add(",");
		singleDelimiters.add("(");
		singleDelimiters.add(")");
		singleDelimiters.add("{");
		singleDelimiters.add("}");
		singleDelimiters.add(".");
//		singleDelimiters.add("$");//zhangbin2
		SINGLE_DELIMITERS = Collections.unmodifiableList(singleDelimiters);
		
		List<String> operators = new ArrayList<String>();
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		operators.add("%");
		operators.add(">");
		operators.add("<");
		operators.add(">=");
		operators.add("<=");
		operators.add("==");
		operators.add("!=");
		operators.add("&&");
		operators.add("||");
		operators.add("!");
		operators.add("=");
//		operators.add("$");//zhangbin2
		operators.add(".");//zhangbin2
		OPERATORS = Collections.unmodifiableList(operators);
	}
}
