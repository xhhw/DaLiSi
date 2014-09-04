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
 * �﷨����
 * @author zhangbin2
 *
 */
public class SyntaxAnalyzer {
	
	/**
	 * �ķ�
	 */
	private Grammar grammar = Grammar.getGrammar();
	
	/**
	 * ���ս��
	 */
	private Valuable finalResult;
	
	/**
	 * �﷨ջ
	 */
	private Stack<Token> syntaxStack = new Stack<Token>();
	
	/**
	 * ����ջ
	 */
	private Stack<Valuable> semanticStack = new Stack<Valuable>();
	
	/**
	 * ������ջ
	 */
	private Stack<DelimiterToken> operatorTokenStack = new Stack<DelimiterToken>();
	
	/**
	 * ��������ջ
	 */
	private Stack<TerminalToken> functionTokenStack = new Stack<TerminalToken>();
	
	/**
	 * ���ڼ�¼����������Token�����еĿ�ʼλ��
	 */
	private Stack<Integer> argumentStartIndexStack = new Stack<Integer>();
	
	/**
	 * ������ջ
	 */
	private Stack<Context> contextStack = new Stack<Context>();
	
	/**
	 * ����ѹ��if-else������֧��������ջ
	 */
	private Stack<Boolean> conditionStack = new Stack<Boolean>();
	
	public SyntaxAnalyzer() {}
	
	/**
	 * ��ȡ���в���
	 * @return
	 */
	public Map<String, Valuable> getVariableTable() {
		if(contextStack.isEmpty())
			return new HashMap<String, Valuable>();
		return contextStack.top().getVariableTable();
	}
	
	/**
	 * �������ʽ
	 * @param tokens Token����
	 * @return
	 * @throws SyntaxException
	 */
	public Valuable analysis(List<TerminalToken> tokens) throws SyntaxException {
		return analysis(tokens, null);
	}
	
	/**
	 * �������ʽ
	 * @param tokens Token����
	 * @param variableTable ��ʼ����ֵ
	 * @return
	 * @throws SyntaxException
	 */
	public Valuable analysis(List<TerminalToken> tokens, Map<String, Valuable> variableTable)
				throws SyntaxException {
		this.finalResult = null;
		
		//�����ʼ�����ģ���ѹ��������ջ
		Map<String, Valuable> initVariableTable = new HashMap<String, Valuable>();
		initVariableTable.putAll(variableTable);
		contextStack.push(new Context(true, initVariableTable, 0));
		
		int index = 0;
		while(index < tokens.size()) {
			//һ������������ʱ��������һ���Ŀ�ʼλ��
			index = analysisSentence(tokens, index);
		}
		
		return finalResult;
	}
	
	/**
	 * ����һ����䣬�����ɹ��󷵻���һ���Ŀ�ʼλ��
	 * @param tokens
	 * @param index
	 * @return
	 * @throws SyntaxException
	 */
	private int analysisSentence(List<TerminalToken> tokens, int index)
				throws SyntaxException {
		clearStacks();
		syntaxStack.push(grammar.getStart());//ѹ���ķ���ʼ���ţ���ʼ����һ�����
		TerminalToken currentToken = tokens.get(index++);
		Token syntaxStackTop = null;
		while(!syntaxStack.isEmpty()) {//ջ��ʱһ������������
			syntaxStackTop = syntaxStack.pop();
			switch(syntaxStackTop.getTokenType()) {
			case NT: //�﷨ջ��Ϊ���ս��ʱ�����Ҳ���ʽ
				Token[] production = ((NonterminalToken)syntaxStackTop).getProduction(currentToken);
				if(production != null)
					reverseProductionIntoSyntaxStack(production);
				else //�Ҳ�����Ӧ�Ĳ���ʽ�������﷨����
					throw new SyntaxException(currentToken);
				break;
			case EXECUTION:
				if (! conditionStack.isEmpty() && conditionStack.top()==false){
					break;
				}
				Executable executable = ((ExecutionToken)syntaxStackTop).getExecutable();
				if(executable == null) {//��Ҫִ�е��Ǻ������Ӻ�������ջȡ����������
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
					//�����Ĳ���
					contextOperate((ContextOperationToken)syntaxStackTop);
				} catch (SyntaxException e) {
					throw new SyntaxException(e.getMessage(), currentToken, e);
				}
				break;
			default: //�﷨ջ��Ϊ�ս��������Ƿ�ƥ��
				if(currentToken.equalsInGrammar((TerminalToken)syntaxStackTop)) {
					if (conditionStack.isEmpty() || conditionStack.top()==true){
						dealTerminalToken(currentToken);
					}
					//�﷨ջ���գ�ȡ����Token��������
					if(!syntaxStack.isEmpty()) {
						if(index < tokens.size())
							currentToken = tokens.get(index++);
						else 	//û�к���Token��˵�����δ��ȷ����
							throw new SyntaxException("Sentence is not properly over at line:"
									+ currentToken.getLine() + ".");
					}
				} else	//�ս��δƥ�䣬�����﷨����
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
		case CONST:	//����ѹ������ջ
			semanticStack.push((ConstToken)currentToken);
			break;
		case VARIABLE:	//������ֵ��ѹ������ջ
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
			functionTokenStack.push(functionToken);	//ѹ�뺯��ջ
			argumentStartIndexStack.push(semanticStack.size());	//ѹ�뺯������������ջ�е���ʼλ��
			break;
		case FIELD://��������
			ObjectFieldToken fieldToken = (ObjectFieldToken)currentToken;
			semanticStack.push(fieldToken);
			break;
		case METHOD://������
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
	 * ������ʽ����ѹ���﷨ջ
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
		//���ѹ������ջ
		semanticStack.push(result);
	}
	
	private Valuable executeUserFunction(Executable function, Valuable[] arguments) {
		//�����������ţ�����������󣬼�¼����λ��
		TerminalToken functionToken = functionTokenStack.pop();
		try {
			Valuable result = function.execute(arguments);
			return result;
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), functionToken, e);
		}
	}

	/**
	 * ִ�в�����
	 * @param operator ����������
	 * @param arguments ����
	 * @return
	 */
	private Valuable executeOperator(Operator operator, Valuable[] arguments) {
		//����������������������󣬼�¼����λ��
		DelimiterToken operatorToken = operatorTokenStack.pop();
		try {
			Valuable result = operator.execute(arguments);
			//����Ǹ�ֵ����������Ҫ���±���ֵ������variableTable
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
	 * ִ�к���
	 * @param function ��������
	 * @param arguments ����
	 * @return
	 */
	private Valuable executeFunction(Function function, Valuable[] arguments) {
		//�����������ţ�����������󣬼�¼����λ��
		FunctionToken functionToken = (FunctionToken) functionTokenStack.pop();
		try {
			Valuable result = function.execute(arguments);
			return result;
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), functionToken, e);
		}
	}

	/**
	 * ��ȡ����
	 * @param executable ����
	 * @return ��������
	 */
	private Valuable[] getArguments(Executable executable) {
		int argumentNum = 0; //��������
		boolean isAssignOperator = false;//�Ƿ�Ϊ��ֵ����
		
		boolean isDotOperator = false;//�Ƿ�"."����
		boolean isFieldGetOperator = false;//�Ƿ��ȡ��������ֵ�Ĳ���
		boolean isFieldSetOperator = false;//�Ƿ�Ϊ�������Ը�ֵ�Ĳ���
		
		if (executable instanceof Operator) {
			argumentNum = ((Operator) executable).getArgumentNum();
			isAssignOperator = executable instanceof AssignOperator;
			isDotOperator = executable instanceof DotOperator;
			isFieldGetOperator = executable instanceof FieldGetOperator;
			isFieldSetOperator = executable instanceof FieldSetOperator;
		} else if (executable instanceof Function
				|| executable instanceof UserFunction
				|| executable instanceof CDefFunction) {
			// ��������Ϊ��ǰ����ջ��С��ȥ������ʼλ��
			argumentNum = semanticStack.size() - argumentStartIndexStack.pop();
		}
		Valuable[] arguments = new Valuable[argumentNum];
		for(int i=argumentNum-1; i>=0; i--) {
			arguments[i] = semanticStack.pop();
			if(arguments[i].getTokenType() == TokenType.VARIABLE) {
				//��������Ǳ�������������Ƿ��Ѷ��壬��ֵ�����б���ֵ��������
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
	 * �����Ĳ���
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
			//��ǰ����������Ϊfalse���������鶼����ִ�У��������Ķ���false
			if (! conditionStack.isEmpty() && conditionStack.top()==false){
				conditionStack.push(false);
				break;
			}
			//ȡif�����������ѹ������ջ
			Valuable ifCondition = semanticStack.pop();
			if(ifCondition.getDataType() != DataType.BOOLEAN)
				throw new SyntaxException("Type mismatch: cannot convert from " +
						ifCondition.getDataType().name() + " to BOOLEAN.");
			else
				conditionStack.push(ifCondition.getBooleanValue());
			break;
		case ELSE_CONDITION:
			//����else���ֵ���������������ջ�е������Ӧ��if���ֵ�������ȡ������ѹ��
			conditionStack.push(!conditionStack.pop());
			break;
		case END_IF:
			//if��������������ջ�е�������
			conditionStack.pop();
			break;
		case NEW_CONTEXT:	//�½�������
			//ȡ����ջ����Ϊ�½��������Ƿ���Ч�ı�־
			boolean effective = conditionStack.top();
			//��������ջ��ȡ��ǰ������
			Context currentContext = contextStack.top();
			//���ڵ�ǰ�����Ĵ����µ������ģ���ѹ��������ջ
			contextStack.push(currentContext.constructUpon(effective, semanticStack.size()));
			break;
		case END_CONTEXT:
			//�����Ľ�������������ջ����
			Context topContext = contextStack.pop();
			if(topContext.isEffective()) {
				//�������������Ч��������Ϊ��true��������µ���ǰջ��������
				contextStack.top().update(topContext);
			} else {
				//�������������Ч��������Ϊfalse����������ջ�н��俪ʼλ��֮�������Ԫ�ص���
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
	 * ��������ջ��ָ��λ��֮�������Ԫ��
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
