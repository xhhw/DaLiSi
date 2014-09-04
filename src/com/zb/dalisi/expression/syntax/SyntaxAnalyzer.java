package com.zb.dalisi.expression.syntax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zb.dalisi.app.module.CDefFunction;
import com.zb.dalisi.expression.lexical.LexicalConstants;
import com.zb.dalisi.expression.syntax.function.Function;
import com.zb.dalisi.expression.syntax.function.UserFunction;
import com.zb.dalisi.expression.syntax.operator.AssignOperator;
import com.zb.dalisi.expression.syntax.operator.DotOperator;
import com.zb.dalisi.expression.syntax.operator.FieldGetOperator;
import com.zb.dalisi.expression.syntax.operator.FieldSetOperator;
import com.zb.dalisi.expression.syntax.operator.Operator;
import com.zb.dalisi.expression.tokens.ConstToken;
import com.zb.dalisi.expression.tokens.ContextOperationToken;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.DelimiterToken;
import com.zb.dalisi.expression.tokens.ExecutionToken;
import com.zb.dalisi.expression.tokens.FunctionToken;
import com.zb.dalisi.expression.tokens.NonterminalToken;
import com.zb.dalisi.expression.tokens.ObjectFieldToken;
import com.zb.dalisi.expression.tokens.ObjectMethodToken;
import com.zb.dalisi.expression.tokens.TerminalToken;
import com.zb.dalisi.expression.tokens.Token;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.TokenType;
import com.zb.dalisi.expression.tokens.Valuable;
import com.zb.dalisi.expression.tokens.VariableToken;
import com.zb.dalisi.expression.utils.Stack;
import com.zb.dalisi.session.SessionManager;

/**
 * 语法分析
 * @author zhangbin2
 *
 */
public class SyntaxAnalyzer {
	
	/**
	 * 文法
	 */
	private Grammar grammar = Grammar.getGrammar();
	
	/**
	 * 最终结果
	 */
	private Valuable finalResult;
	
	/**
	 * 语法栈
	 */
	private Stack<Token> syntaxStack = new Stack<Token>();
	
	/**
	 * 语义栈
	 */
	private Stack<Valuable> semanticStack = new Stack<Valuable>();
	
	/**
	 * 操作符栈
	 */
	private Stack<DelimiterToken> operatorTokenStack = new Stack<DelimiterToken>();
	
	/**
	 * 函数符号栈
	 */
	private Stack<TerminalToken> functionTokenStack = new Stack<TerminalToken>();
	
	/**
	 * 用于记录函数参数在Token序列中的开始位置
	 */
	private Stack<Integer> argumentStartIndexStack = new Stack<Integer>();
	
	/**
	 * 上下文栈
	 */
	private Stack<Context> contextStack = new Stack<Context>();
	
	/**
	 * 用于压入if-else语句各分支的条件的栈
	 */
	private Stack<Boolean> conditionStack = new Stack<Boolean>();
	
	public SyntaxAnalyzer() {}
	
	/**
	 * 获取所有参数
	 * @return
	 */
	public Map<String, Valuable> getVariableTable() {
		if(contextStack.isEmpty())
			return new HashMap<String, Valuable>();
		return contextStack.top().getVariableTable();
	}
	
	/**
	 * 解析表达式
	 * @param tokens Token序列
	 * @return
	 * @throws SyntaxException
	 */
	public Valuable analysis(List<TerminalToken> tokens) throws SyntaxException {
		return analysis(tokens, null);
	}
	
	/**
	 * 解析表达式
	 * @param tokens Token序列
	 * @param variableTable 初始变量值
	 * @return
	 * @throws SyntaxException
	 */
	public Valuable analysis(List<TerminalToken> tokens, Map<String, Valuable> variableTable)
				throws SyntaxException {
		this.finalResult = null;
		
		//构造初始上下文，并压入上下文栈
		Map<String, Valuable> initVariableTable = new HashMap<String, Valuable>();
		initVariableTable.putAll(variableTable);
		contextStack.push(new Context(true, initVariableTable, 0));
		
		int index = 0;
		while(index < tokens.size()) {
			//一条语句解析结束时，返回下一语句的开始位置
			index = analysisSentence(tokens, index);
		}
		
		return finalResult;
	}
	
	/**
	 * 解析一条语句，解析成功后返回下一语句的开始位置
	 * @param tokens
	 * @param index
	 * @return
	 * @throws SyntaxException
	 */
	private int analysisSentence(List<TerminalToken> tokens, int index)
				throws SyntaxException {
		clearStacks();
		syntaxStack.push(grammar.getStart());//压入文法开始符号，开始解析一条语句
		TerminalToken currentToken = tokens.get(index++);
		Token syntaxStackTop = null;
		while(!syntaxStack.isEmpty()) {//栈空时一条语句分析结束
			syntaxStackTop = syntaxStack.pop();
			switch(syntaxStackTop.getTokenType()) {
			case NT: //语法栈顶为非终结符时，查找产生式
				Token[] production = ((NonterminalToken)syntaxStackTop).getProduction(currentToken);
				if(production != null)
					reverseProductionIntoSyntaxStack(production);
				else //找不到对应的产生式，存在语法错误
					throw new SyntaxException(currentToken);
				break;
			case EXECUTION:
				if (! conditionStack.isEmpty() && conditionStack.top()==false){
					break;
				}
				Executable executable = ((ExecutionToken)syntaxStackTop).getExecutable();
				if(executable == null) {//需要执行的是函数，从函数符号栈取出函数定义
					if (functionTokenStack.top().getTokenType()==TokenType.FUNCTION){
						executable = ((FunctionToken)functionTokenStack.top()).getFunction();
					}
					else if (functionTokenStack.top().getTokenType()==TokenType.METHOD){
						executable = ((ObjectMethodToken)functionTokenStack.top()).getFunction();
					}
				}
				execute(executable);
				break;
			case CONTEXT_OPERATION:
				try {
					//上下文操作
					contextOperate((ContextOperationToken)syntaxStackTop);
				} catch (SyntaxException e) {
					throw new SyntaxException(e.getMessage(), currentToken, e);
				}
				break;
			default: //语法栈顶为终结符，检查是否匹配
				if(currentToken.equalsInGrammar((TerminalToken)syntaxStackTop)) {
					if (conditionStack.isEmpty() || conditionStack.top()==true){
						dealTerminalToken(currentToken);
					}
					//语法栈不空，取后续Token继续解析
					if(!syntaxStack.isEmpty()) {
						if(index < tokens.size())
							currentToken = tokens.get(index++);
						else 	//没有后续Token，说明语句未正确结束
							throw new SyntaxException("Sentence is not properly over at line:"
									+ currentToken.getLine() + ".");
					}
				} else	//终结符未匹配，存在语法错误
					throw new SyntaxException(currentToken);
				break;
			}
		}
		
		if(!semanticStack.isEmpty())
			finalResult = semanticStack.pop();
		
		return index;
	}
	
	private void dealTerminalToken(TerminalToken currentToken) {
		switch(currentToken.getTokenType()) {
		case CONST:	//常量压入语义栈
			semanticStack.push((ConstToken)currentToken);
			break;
		case VARIABLE:	//变量设值后压入语义栈
			VariableToken variable = (VariableToken)currentToken;
			Valuable valueOfVariable = null;
			if (variable.isKeepInContext()){
				String key = variable.getText().substring(1);
				valueOfVariable = getVariableValueFromSession(key);
			}else{
				valueOfVariable = getVariableValue(variable.getText());
			}
			if(valueOfVariable != null)
				variable.assignWith(valueOfVariable);
			semanticStack.push(variable);
			break;
		case DELIMITER:
			if(LexicalConstants.OPERATORS.contains(currentToken.getText()))
				operatorTokenStack.push((DelimiterToken)currentToken);
			break;
		case FUNCTION:
			FunctionToken functionToken = (FunctionToken)currentToken;
			functionTokenStack.push(functionToken);	//压入函数栈
			argumentStartIndexStack.push(semanticStack.size());	//压入函数参数在语义栈中的起始位置
			break;
		case FIELD://对象属性
			ObjectFieldToken fieldToken = (ObjectFieldToken)currentToken;
			semanticStack.push(fieldToken);
			break;
		case METHOD://对象函数
			ObjectMethodToken methodToken = (ObjectMethodToken)currentToken;
			functionTokenStack.push(methodToken);
			argumentStartIndexStack.push(semanticStack.size());
			semanticStack.push(methodToken);
			break;
		default:
			break;
		}
	}
	
	//zhangbin2
	private Valuable getVariableValueFromSession(String text) {
		// TODO Auto-generated method stub
		Object obj = SessionManager.getSession().getValue(text);
		if (obj != null){
			if (obj instanceof Valuable){
				return (Valuable)obj;
			}
			return TokenBuilder.buildRuntimeValue(obj);
		}
		else
			throw new SyntaxException("Session mismatch: cannot find $"+text+" from this Session.");
	}

	/**
	 * 将产生式反序压入语法栈
	 * @param production
	 */
	private void reverseProductionIntoSyntaxStack(Token[] production) {
		if(production.length > 0)
			for(int i=production.length-1; i>=0; i--)
				syntaxStack.push(production[i]);
	}
	
	private void execute(Executable executable) {
		Valuable[] arguments = getArguments(executable);
		Valuable result = null;
		if(executable instanceof Operator)
			result = executeOperator((Operator)executable, arguments);
		else if(executable instanceof Function)
			result = executeFunction((Function)executable, arguments);
		else if(executable instanceof CDefFunction || executable instanceof UserFunction)
			result = executeUserFunction(executable, arguments);
		//结果压入语义栈
		semanticStack.push(result);
	}
	
	private Valuable executeUserFunction(Executable function, Valuable[] arguments) {
		//弹出函数符号，如果发生错误，记录错误位置
		TerminalToken functionToken = functionTokenStack.pop();
		try {
			Valuable result = function.execute(arguments);
			return result;
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), functionToken, e);
		}
	}

	/**
	 * 执行操作符
	 * @param operator 操作符定义
	 * @param arguments 参数
	 * @return
	 */
	private Valuable executeOperator(Operator operator, Valuable[] arguments) {
		//弹出操作符，如果发生错误，记录错误位置
		DelimiterToken operatorToken = operatorTokenStack.pop();
		try {
			Valuable result = operator.execute(arguments);
			//如果是赋值操作，则需要更新被赋值变量到variableTable
			if(operator instanceof AssignOperator){
				VariableToken variable = (VariableToken)arguments[0];
				setVariableValue(variable.getText(), result);
			}
			return result;
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), operatorToken, e);
		} catch(ArithmeticException e) {
			ArithmeticException arithmeticException = new ArithmeticException(e.getMessage()
					+ " At line:" + operatorToken.getLine() + ", column:" + operatorToken.getColumn() + ".");
			arithmeticException.initCause(e);
			throw arithmeticException;
		}
	}
	
	/**
	 * 执行函数
	 * @param function 函数定义
	 * @param arguments 参数
	 * @return
	 */
	private Valuable executeFunction(Function function, Valuable[] arguments) {
		//弹出函数符号，如果发生错误，记录错误位置
		FunctionToken functionToken = (FunctionToken) functionTokenStack.pop();
		try {
			Valuable result = function.execute(arguments);
			return result;
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), functionToken, e);
		}
	}

	/**
	 * 获取参数
	 * @param executable 操作
	 * @return 参数数组
	 */
	private Valuable[] getArguments(Executable executable) {
		int argumentNum = 0; //参数个数
		boolean isAssignOperator = false;//是否为赋值操作
		
		boolean isDotOperator = false;//是否"."操作
		boolean isFieldGetOperator = false;//是否获取对象属性值的操作
		boolean isFieldSetOperator = false;//是否为对象属性赋值的操作
		
		if (executable instanceof Operator) {
			argumentNum = ((Operator) executable).getArgumentNum();
			isAssignOperator = executable instanceof AssignOperator;
			isDotOperator = executable instanceof DotOperator;
			isFieldGetOperator = executable instanceof FieldGetOperator;
			isFieldSetOperator = executable instanceof FieldSetOperator;
		} else if (executable instanceof Function
				|| executable instanceof UserFunction
				|| executable instanceof CDefFunction) {
			// 参数个数为当前语义栈大小减去参数起始位置
			argumentNum = semanticStack.size() - argumentStartIndexStack.pop();
		}
		Valuable[] arguments = new Valuable[argumentNum];
		for(int i=argumentNum-1; i>=0; i--) {
			arguments[i] = semanticStack.pop();
			if(arguments[i].getTokenType() == TokenType.VARIABLE) {
				//如果参数是变量，则检查变量是否已定义，赋值操作中被赋值变量除外
				if(isAssignOperator && i == 0)
					break;
				
				if(isDotOperator && i == 1)
					continue;
				
				if(isFieldGetOperator)
					break;
				if(isFieldSetOperator && i == 0)
					break;
				
				if(arguments[i].getIndex() < 0) {
					throw new VariableNotInitializedException((VariableToken)arguments[i]);
				}
			}
		}
		return arguments;
	}
	
	/**
	 * 上下文操作
	 * @param contextOperationToken
	 * @throws SyntaxException
	 */
	private void contextOperate(ContextOperationToken contextOperationToken) throws SyntaxException {
		switch(contextOperationToken.getContextOperation()) {
		case AND_CONDITION:
			if (! conditionStack.isEmpty() && conditionStack.top()==false){
				conditionStack.push(false);
				break;
			}
			Valuable andCondition = semanticStack.top();
			if(andCondition.getDataType() != DataType.BOOLEAN)
				throw new SyntaxException("Type mismatch: cannot convert from " +
						andCondition.getDataType().name() + " to BOOLEAN.");
			else
				conditionStack.push(andCondition.getBooleanValue());
			break;
		case END_AND:
			conditionStack.pop();
			break;
		case OR_CONDITION:
			if (! conditionStack.isEmpty() && conditionStack.top()==false){
				conditionStack.push(false);
				break;
			}
			Valuable orCondition = semanticStack.top();
			if(orCondition.getDataType() != DataType.BOOLEAN)
				throw new SyntaxException("Type mismatch: cannot convert from " +
						orCondition.getDataType().name() + " to BOOLEAN.");
			else
				conditionStack.push(!orCondition.getBooleanValue());
			break;
		case END_OR:
			conditionStack.pop();
			break;
		case IF_CONDITION:
			//当前上下文整体为false，则子语句块都无需执行，子上下文都是false
			if (! conditionStack.isEmpty() && conditionStack.top()==false){
				conditionStack.push(false);
				break;
			}
			//取if后的条件，并压入条件栈
			Valuable ifCondition = semanticStack.pop();
			if(ifCondition.getDataType() != DataType.BOOLEAN)
				throw new SyntaxException("Type mismatch: cannot convert from " +
						ifCondition.getDataType().name() + " to BOOLEAN.");
			else
				conditionStack.push(ifCondition.getBooleanValue());
			break;
		case ELSE_CONDITION:
			//设置else部分的条件，即从条件栈中弹出其对应的if部分的条件，取反重新压入
			conditionStack.push(!conditionStack.pop());
			break;
		case END_IF:
			//if语句结束，从条件栈中弹出条件
			conditionStack.pop();
			break;
		case NEW_CONTEXT:	//新建上下文
			//取条件栈顶作为新建上下文是否有效的标志
			boolean effective = conditionStack.top();
			//从上下文栈中取当前上下文
			Context currentContext = contextStack.top();
			//基于当前上下文创建新的上下文，并压入上下文栈
			contextStack.push(currentContext.constructUpon(effective, semanticStack.size()));
			break;
		case END_CONTEXT:
			//上下文结束，从上下文栈弹出
			Context topContext = contextStack.pop();
			if(topContext.isEffective()) {
				//如果该上下文有效，即条件为真true，则将其更新到当前栈顶上下文
				contextStack.top().update(topContext);
			} else {
				//如果该上下文无效，即条件为false，则在语义栈中将其开始位置之后的所有元素弹出
				recoverSemanticStack(topContext.getStartIndex());
			}
			break;
		}
	}
	
	private Valuable getVariableValue(String variableName) {
		Context currentContext = contextStack.top();
		return currentContext.getVariableValue(variableName);
	}
	
	private void setVariableValue(String text, Valuable value) {
		Context currentContext = contextStack.top();
		currentContext.setVariableValue(text, value);
	}
	
	/**
	 * 弹出语义栈在指定位置之后的所有元素
	 * @param startIndex
	 */
	private void recoverSemanticStack(int startIndex) {
		while(semanticStack.size() > startIndex)
			semanticStack.pop();
	}
	
	private void clearStacks() {
		syntaxStack.clear();
		semanticStack.clear();
		operatorTokenStack.clear();
		functionTokenStack.clear();
		argumentStartIndexStack.clear();
	}
	
}
