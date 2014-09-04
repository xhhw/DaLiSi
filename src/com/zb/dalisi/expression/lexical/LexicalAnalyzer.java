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
 * 词法分析
 * @author zhangbin2
 *
 */
public class LexicalAnalyzer {
	
	/**
	 * 有限自动机
	 */
	private DFADefinition DFA = DFADefinition.getDFA();
	
	/**
	 * 当前解析位置行号
	 */
	private int curLine = 0;
	
	/**
	 * 下次读取列号
	 */
	private int nextScanColumn = 0;
	
	/**
	 * 存放正在解析的Token的字面内容
	 */
	private StringBuilder curWord = new StringBuilder();
	
	/**
	 * 表达式中涉及的函数
	 */
//	private Map<String, Executable> functionTable;
	
	public LexicalAnalyzer() {}
	
	/**
	 * 词法分析
	 * @param expression 表达式
	 * @return
	 * @throws LexicalException
	 */
	public List<TerminalToken> analysis(String expression) throws LexicalException {
		return analysis(expression, null);
	}
	
	/**
	 * 词法分析
	 * @param expression 表达式
	 * @param functionTable 表达式涉及的函数
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
	 * 执行词法分析
	 * @return
	 * @throws LexicalException 
	 */
	private List<TerminalToken> doAnalysis(Scanner scanner) throws LexicalException {
		//词法分析的结果，按序存放识别出的Token
		List<TerminalToken> tokens = new ArrayList<TerminalToken>();
		
		char[] curLineCharArray;	//用于存放当前行的字符数组
		char inputChar;	//当前读取的字符
		
		DFAMidState curMidState = null;		//当前到达的中间状态
		DFAMidState nextMidsState = null;	//curMidState根据inputChar所能到达的中间状态
		DFAEndStateCode endStateCode = null;	//结束状态代码
		TerminalToken curToken = null;	//识别出的Token
		TerminalToken preToken = null;	//前一个Token
		curLine = 0;
		while(scanner.hasNextLine()) {
			curLineCharArray = nextLine(scanner).toCharArray();//读取下一行
			curLine++;
			nextScanColumn = 0;
			
			while(escapeBlank(curLineCharArray) < curLineCharArray.length) {
				curMidState = DFA.getDFAStartState(); //设置当前状态到开始状态，准备识别下一个Token
				curWord = curWord.delete(0, curWord.length());
				curToken = null;
				
				while(curToken == null) {
					if(nextScanColumn < curLineCharArray.length) {
						inputChar = curLineCharArray[nextScanColumn]; //取下一字符
						nextMidsState = curMidState.getNextMidState(inputChar);
						if(nextMidsState != null) {	//下一中间状态不空，追加该字符到当前Token
							curMidState = nextMidsState;
							curWord.append(inputChar);
							nextScanColumn++;
						} else {
							endStateCode = curMidState.goToEndStateWithInput(inputChar);
							if(endStateCode != null)
								//到达结束状态，一个token识别结束（当前输入的字符不追加到curWord）
								curToken = actAtEndState(endStateCode, preToken);
							else 	//发生词法错误
								throw new LexicalException(curMidState, curLine, nextScanColumn + 1);
						}
					} else if(curMidState.hasRouteToEndState()) {
						//在行尾如果curMidState存在到结束状态的路由，说明当前Token正确结束，否则存在词法错误
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
	 * 在结束状态执行动作，识别出一个Token
	 * @param endStateCode
	 * @param preToken 
	 * @throws LexicalException 
	 */
	private TerminalToken actAtEndState(DFAEndStateCode endStateCode, TerminalToken preToken) throws LexicalException {
		TerminalToken curToken = null;	//当前识别出的Token
		String curWordText = curWord.toString();	//当前Token的字面内容
		int wordStartColumn = nextScanColumn - curWordText.length() + 1;	//当前Token开始的列位置
		
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
			//依次判断是否为布尔常量、关键字、函数名，如果都不是，则判断为变量
			if("true".equals(curWordText) || "TRUE".equals(curWordText)
					|| "false".equals(curWordText) || "FALSE".equals(curWordText)) {
				//识别布尔常量
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).dataType(DataType.BOOLEAN)
								.index(DataCache.getBooleanIndex(Boolean.valueOf(curWordText)))
								.buildConst();
			} else if(KEY_WORDS.contains(curWordText)) { //识别关键字
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildKey();
			} else if(preTokenIsDotToken(preToken)) { //对象属性
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
						.text(curWordText).buildObjectField();
			} else if(hasFunction(curWordText)) { //函数
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).executable(findFunction(curWordText)).buildFunction();
			} else {//变量
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildVariable();
				if (curWordText.startsWith("$")){
					((VariableToken)curToken).setKeepInContext(true);
				}
			}
			break;
		case SINGLE_DELIMITER_END:
			//判断是否为合法的单字符界符，否则词法错误
			if(SINGLE_DELIMITERS.contains(curWordText))
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).buildDelimiter();
			else
				throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			break;
		case DOUBLE_DELIMITER_END:
			if(DOUBLE_DELIMITERS.contains(curWordText)) {	//判断是否为合法的双字符界符
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildDelimiter();
			} else {
				//取第一个字符，如果是合法的单字符界符，当前列扫描位置减1，下次扫描从第二个界符开始
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
					//日期格式yyyy-MM-dd
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(curWordText));
				} else if(curWordText.matches(ACCURATE_DATE_PATTERN)) {
					//日期格式yyyy-MM-dd HH:mm:ss
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
			else	//识别转义字符
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
	 * 判断新识别出的token是否是被赋值的变量
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
	 * @return 下一行，去掉注释
	 */
	private String nextLine(Scanner scanner){
		String nextLine = scanner.nextLine();
		//仅支持行注释，注释以##开头
		if(nextLine.indexOf("##") < 0)
			return nextLine;
		Pattern commentPattern = Pattern.compile("##.*");
		Matcher matcher = commentPattern.matcher(nextLine);
		return matcher.replaceFirst("");
	}
	
	/**
	 * 判断函数是否存在
	 * @param functionName
	 * @return
	 */
	private boolean hasFunction(String functionName) {
		return hasCustomizedFunction(functionName)
				|| SystemFunctions.hasFunction(functionName);
	}
	
	/**
	 * 查找函数，先判断函数是否为自定义函数，如果不是再判断是否为系统函数
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
	 * 判断函数是否为自定义函数
	 * @param functionName
	 * @return
	 */
	private boolean hasCustomizedFunction(String functionName) {
		return CacheFunction.hasFunction(functionName);
	}
}
