package com.zb.dalisi.expression.lexical;

import static com.zb.dalisi.expression.lexical.LexicalConstants.ACCURATE_DATE_PATTERN;
import static com.zb.dalisi.expression.lexical.LexicalConstants.ASSIGN_TOKEN;
import static com.zb.dalisi.expression.lexical.LexicalConstants.BLANK_PATTERN;
import static com.zb.dalisi.expression.lexical.LexicalConstants.DATE_PATTERN;
import static com.zb.dalisi.expression.lexical.LexicalConstants.DOUBLE_DELIMITERS;
import static com.zb.dalisi.expression.lexical.LexicalConstants.KEY_WORDS;
import static com.zb.dalisi.expression.lexical.LexicalConstants.SINGLE_DELIMITERS;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zb.dalisi.cache.CacheFunction;
import com.zb.dalisi.expression.lexical.dfa.DFADefinition;
import com.zb.dalisi.expression.lexical.dfa.DFAEndStateCode;
import com.zb.dalisi.expression.lexical.dfa.DFAMidState;
import com.zb.dalisi.expression.syntax.Executable;
import com.zb.dalisi.expression.syntax.function.SystemFunctions;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.ObjectFieldToken;
import com.zb.dalisi.expression.tokens.TerminalToken;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.TokenType;
import com.zb.dalisi.expression.tokens.VariableToken;
import com.zb.dalisi.expression.utils.DataCache;
import com.zb.dalisi.expression.utils.ExpressionUtil;

/**
 * �ʷ�����
 * @author zhangbin2
 *
 */
public class LexicalAnalyzer {
	
	/**
	 * �����Զ���
	 */
	private DFADefinition DFA = DFADefinition.getDFA();
	
	/**
	 * ��ǰ����λ���к�
	 */
	private int curLine = 0;
	
	/**
	 * �´ζ�ȡ�к�
	 */
	private int nextScanColumn = 0;
	
	/**
	 * ������ڽ�����Token����������
	 */
	private StringBuilder curWord = new StringBuilder();
	
	/**
	 * ���ʽ���漰�ĺ���
	 */
//	private Map<String, Executable> functionTable;
	
	public LexicalAnalyzer() {}
	
	/**
	 * �ʷ�����
	 * @param expression ���ʽ
	 * @return
	 * @throws LexicalException
	 */
	public List<TerminalToken> analysis(String expression) throws LexicalException {
		return analysis(expression, null);
	}
	
	/**
	 * �ʷ�����
	 * @param expression ���ʽ
	 * @param functionTable ���ʽ�漰�ĺ���
	 * @return
	 * @throws LexicalException
	 */
	public List<TerminalToken> analysis(String expression, Map<String, Executable> functionTable) throws LexicalException {
		if(expression == null || expression.length() == 0)
			throw new LexicalException("Invalid empty expression.");
		
		Scanner scanner = new Scanner(expression);
//		this.functionTable = functionTable;
		
		try {
			List<TerminalToken> tokens = doAnalysis(scanner);
			return tokens;
		} catch(LexicalException e) {
			throw e;
		} finally {
			scanner.close();
		}
	}
	
	/**
	 * ִ�дʷ�����
	 * @return
	 * @throws LexicalException 
	 */
	private List<TerminalToken> doAnalysis(Scanner scanner) throws LexicalException {
		//�ʷ������Ľ����������ʶ�����Token
		List<TerminalToken> tokens = new ArrayList<TerminalToken>();
		
		char[] curLineCharArray;	//���ڴ�ŵ�ǰ�е��ַ�����
		char inputChar;	//��ǰ��ȡ���ַ�
		
		DFAMidState curMidState = null;		//��ǰ������м�״̬
		DFAMidState nextMidsState = null;	//curMidState����inputChar���ܵ�����м�״̬
		DFAEndStateCode endStateCode = null;	//����״̬����
		TerminalToken curToken = null;	//ʶ�����Token
		TerminalToken preToken = null;	//ǰһ��Token
		curLine = 0;
		while(scanner.hasNextLine()) {
			curLineCharArray = nextLine(scanner).toCharArray();//��ȡ��һ��
			curLine++;
			nextScanColumn = 0;
			
			while(escapeBlank(curLineCharArray) < curLineCharArray.length) {
				curMidState = DFA.getDFAStartState(); //���õ�ǰ״̬����ʼ״̬��׼��ʶ����һ��Token
				curWord = curWord.delete(0, curWord.length());
				curToken = null;
				
				while(curToken == null) {
					if(nextScanColumn < curLineCharArray.length) {
						inputChar = curLineCharArray[nextScanColumn]; //ȡ��һ�ַ�
						nextMidsState = curMidState.getNextMidState(inputChar);
						if(nextMidsState != null) {	//��һ�м�״̬���գ�׷�Ӹ��ַ�����ǰToken
							curMidState = nextMidsState;
							curWord.append(inputChar);
							nextScanColumn++;
						} else {
							endStateCode = curMidState.goToEndStateWithInput(inputChar);
							if(endStateCode != null)
								//�������״̬��һ��tokenʶ���������ǰ������ַ���׷�ӵ�curWord��
								curToken = actAtEndState(endStateCode, preToken);
							else 	//�����ʷ�����
								throw new LexicalException(curMidState, curLine, nextScanColumn + 1);
						}
					} else if(curMidState.hasRouteToEndState()) {
						//����β���curMidState���ڵ�����״̬��·�ɣ�˵����ǰToken��ȷ������������ڴʷ�����
						curToken = actAtEndState(curMidState.getNextEndStateCode(), preToken);
					} else
						throw new LexicalException(curMidState, curLine, nextScanColumn + 1);
				}
				tokens.add(curToken);
				preToken = curToken;
				checkVariableToBeAssigned(tokens);
				checkObjectMethod(tokens);
			}
		}
		return tokens;
	}
	
	private void checkObjectMethod(List<TerminalToken> tokens) {
		// TODO Auto-generated method stub
		int size = tokens.size();
		if(size < 2)
			return;
		TerminalToken first = tokens.get(size - 2);
		TerminalToken second = tokens.get(size - 1);
		if (first.getTokenType() == TokenType.FIELD
				&& second.getTokenType() == TokenType.DELIMITER
				&& "(".equals(second.getText())) {
			TerminalToken objMethodToken = TokenBuilder.getBuilder()
					.line(first.getLine()).column(first.getColumn())
					.text(first.getText()).buildObjectMethod();
			tokens.set(size - 2, objMethodToken);
		}
	}

	/**
	 * �ڽ���״ִ̬�ж�����ʶ���һ��Token
	 * @param endStateCode
	 * @param preToken 
	 * @throws LexicalException 
	 */
	private TerminalToken actAtEndState(DFAEndStateCode endStateCode, TerminalToken preToken) throws LexicalException {
		TerminalToken curToken = null;	//��ǰʶ�����Token
		String curWordText = curWord.toString();	//��ǰToken����������
		int wordStartColumn = nextScanColumn - curWordText.length() + 1;	//��ǰToken��ʼ����λ��
		
		switch(endStateCode) {
		case NUMBER_END:
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex(new BigDecimal(curWordText)))
							.buildConst();
			break;
		case NUMBER_INT_END:
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.INT)
							.index(DataCache.getObjIndex(new Integer(curWordText)))
							.buildConst();
			break;
		case NUMBER_LONG_END:
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.LONG)
							.index(DataCache.getObjIndex(new Long(curWordText.substring(0, curWordText.length()-1))))
							.buildConst();
			break;
		case ID_END:
			//�����ж��Ƿ�Ϊ�����������ؼ��֡�����������������ǣ����ж�Ϊ����
			if("true".equals(curWordText) || "TRUE".equals(curWordText)
					|| "false".equals(curWordText) || "FALSE".equals(curWordText)) {
				//ʶ�𲼶�����
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).dataType(DataType.BOOLEAN)
								.index(DataCache.getBooleanIndex(Boolean.valueOf(curWordText)))
								.buildConst();
			} else if(KEY_WORDS.contains(curWordText)) { //ʶ��ؼ���
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildKey();
			} else if(preTokenIsDotToken(preToken)) { //��������
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
						.text(curWordText).buildObjectField();
			} else if(hasFunction(curWordText)) { //����
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).executable(findFunction(curWordText)).buildFunction();
			} else {//����
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildVariable();
				if (curWordText.startsWith("$")){
					((VariableToken)curToken).setKeepInContext(true);
				}
			}
			break;
		case SINGLE_DELIMITER_END:
			//�ж��Ƿ�Ϊ�Ϸ��ĵ��ַ����������ʷ�����
			if(SINGLE_DELIMITERS.contains(curWordText))
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).buildDelimiter();
			else
				throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			break;
		case DOUBLE_DELIMITER_END:
			if(DOUBLE_DELIMITERS.contains(curWordText)) {	//�ж��Ƿ�Ϊ�Ϸ���˫�ַ����
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildDelimiter();
			} else {
				//ȡ��һ���ַ�������ǺϷ��ĵ��ַ��������ǰ��ɨ��λ�ü�1���´�ɨ��ӵڶ��������ʼ
				String firstDelimiter = curWordText.substring(0, 1);
				if(SINGLE_DELIMITERS.contains(firstDelimiter)) {
					curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(firstDelimiter).buildDelimiter();
					nextScanColumn--;
				} else
					throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			}
			break;
		case DATE_END:
			Calendar date = null;
			DateFormat dateFormate;
			try {
				if(curWordText.matches(DATE_PATTERN)) {
					//���ڸ�ʽyyyy-MM-dd
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(curWordText));
				} else if(curWordText.matches(ACCURATE_DATE_PATTERN)) {
					//���ڸ�ʽyyyy-MM-dd HH:mm:ss
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(curWordText));
				} else {
					throw new LexicalException("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].",
										curLine, wordStartColumn);
				}
				if(date != null)
					curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).dataType(DataType.DATE)
									.index(DataCache.getDateIndex(date)).buildConst();
			} catch (ParseException e) {
				throw new LexicalException("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].",
						curLine, wordStartColumn);
			}
			break;
		case CHAR_END:
			char ch;
			if(curWordText.length() == 3)
				ch = curWordText.toCharArray()[1];
			else	//ʶ��ת���ַ�
				ch = ExpressionUtil.getEscapedChar(curWordText.toCharArray()[2]);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.CHARACTER)
							.index(DataCache.getCharIndex(ch)).buildConst();
			break;
		case STRING_END:
			String str = curWordText.substring(1, curWordText.length()-1);
			str = ExpressionUtil.transformEscapesInString(str);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.STRING)
							.index(DataCache.getStringIndex(str)).buildConst();
			break;
		default:
			throw new LexicalException("endStateCode is undefined.", curLine, wordStartColumn);
		}
		return curToken;
	}
	
	private boolean preTokenIsDotToken(TerminalToken preToken) {
		// TODO Auto-generated method stub
		if (preToken != null && preToken.getTokenType() == TokenType.DELIMITER
				&& ".".equals(preToken.getText())) {
			return true;
		}
		return false;
	}

	/**
	 * �ж���ʶ�����token�Ƿ��Ǳ���ֵ�ı���
	 */
	private void checkVariableToBeAssigned(List<TerminalToken> tokens) {
		int size = tokens.size();
		if(size < 2)
			return;
		TerminalToken first = tokens.get(size-2);
		TerminalToken second = tokens.get(size - 1);
		if(!second.equalsInGrammar(ASSIGN_TOKEN))
			return;
		if(first instanceof VariableToken)
			((VariableToken)first).setToBeAssigned(true);
		if(first instanceof ObjectFieldToken)
			((ObjectFieldToken)first).setToBeAssigned(true);
	}
	
	private int escapeBlank(char[] curLineCharArray) {
		while(nextScanColumn < curLineCharArray.length 
				&& ((Character)curLineCharArray[nextScanColumn]).toString()
				.matches(BLANK_PATTERN) ) 
			nextScanColumn++;
			
		return nextScanColumn;
	}
	
	/**
	 * @return ��һ�У�ȥ��ע��
	 */
	private String nextLine(Scanner scanner){
		String nextLine = scanner.nextLine();
		//��֧����ע�ͣ�ע����##��ͷ
		if(nextLine.indexOf("##") < 0)
			return nextLine;
		Pattern commentPattern = Pattern.compile("##.*");
		Matcher matcher = commentPattern.matcher(nextLine);
		return matcher.replaceFirst("");
	}
	
	/**
	 * �жϺ����Ƿ����
	 * @param functionName
	 * @return
	 */
	private boolean hasFunction(String functionName) {
		return hasCustomizedFunction(functionName)
				|| SystemFunctions.hasFunction(functionName);
	}
	
	/**
	 * ���Һ��������жϺ����Ƿ�Ϊ�Զ��庯��������������ж��Ƿ�Ϊϵͳ����
	 * @param functionName
	 * @return
	 */
	private Executable findFunction(String functionName) {
		if(hasCustomizedFunction(functionName))
//			return functionTable.get(functionName);
			return CacheFunction.getDefFunction(functionName);
		else
			return SystemFunctions.getFunction(functionName);
	}
	
	/**
	 * �жϺ����Ƿ�Ϊ�Զ��庯��
	 * @param functionName
	 * @return
	 */
	private boolean hasCustomizedFunction(String functionName) {
		return CacheFunction.hasFunction(functionName);
	}
}
