package com.zb.dalisi.expression.syntax;

import com.zb.dalisi.expression.syntax.operator.OperatorFactory;
import com.zb.dalisi.expression.tokens.ConstToken;
import com.zb.dalisi.expression.tokens.ContextOperation;
import com.zb.dalisi.expression.tokens.ContextOperationToken;
import com.zb.dalisi.expression.tokens.DelimiterToken;
import com.zb.dalisi.expression.tokens.ExecutionToken;
import com.zb.dalisi.expression.tokens.FunctionToken;
import com.zb.dalisi.expression.tokens.KeyToken;
import com.zb.dalisi.expression.tokens.NonterminalToken;
import com.zb.dalisi.expression.tokens.ObjectFieldToken;
import com.zb.dalisi.expression.tokens.ObjectMethodToken;
import com.zb.dalisi.expression.tokens.TerminalToken;
import com.zb.dalisi.expression.tokens.Token;
import com.zb.dalisi.expression.tokens.TokenBuilder;
import com.zb.dalisi.expression.tokens.VariableToken;

/**
 * �ķ����ķ����ų�ʼ��
 * @author shanxuecheng
 *
 */
public class Grammar {
	
	private Grammar() {
		start.addProduction(new TerminalToken[]{variableToBeAssigned, variable, constant, minusMark, leftBracket, function, notMark}, //paramMark},
								new Token[]{sentence});
		start.addProduction(new TerminalToken[]{ifKey}, 
								new Token[]{ifStatement});
		
		ifStatement.addProduction(new TerminalToken[]{ifKey}, 
								new Token[]{ifKey, leftBracket, bolExpression, ifConditionCo, rightBracket,  
											newContextCo, block, endContextCo, elseSection, endIFCo, endIfKey});
		
		block.addProduction(new TerminalToken[]{variableToBeAssigned, variable, constant, minusMark, leftBracket, function, notMark}, //paramMark},
								new Token[]{sentence, block});
		block.addProduction(new TerminalToken[]{ifKey}, 
								new Token[]{ifStatement, block});
		block.addProduction(new TerminalToken[]{elseKey, endIfKey}, 
								new Token[]{});
		
		elseSection.addProduction(new TerminalToken[]{elseKey}, 
								new Token[]{elseKey, elseConditionCo, newContextCo, block, endContextCo});
		elseSection.addProduction(new TerminalToken[]{endIfKey}, 
								new Token[]{});
		
		sentence.addProduction(new TerminalToken[]{variableToBeAssigned},
								new Token[]{variableToBeAssigned, assignMark, bolExpression, assignExe, semicolon});
		sentence.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function, notMark}, //paramMark},
								new Token[]{bolExpression, semicolon});
		
		bolExpression.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function, notMark}, //paramMark},
								new Token[]{bolTerm, _bolExpression});
		
		_bolExpression.addProduction(new TerminalToken[]{orMark},
								new Token[]{orMark, orCo, bolTerm, orExe, endOrCo, _bolExpression});
		_bolExpression.addProduction(new TerminalToken[]{rightBracket, comma, semicolon},
								new Token[]{});
		
		bolTerm.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function, notMark}, //paramMark},
								new Token[]{bolFactor, _bolTerm});
		
		_bolTerm.addProduction(new TerminalToken[]{andMark},
				new Token[]{andMark, andCo, bolFactor, andExe, endAndCo, _bolTerm});
//								new Token[]{andMark, bolFactor, andExe, _bolTerm});
		_bolTerm.addProduction(new TerminalToken[]{orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		bolFactor.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function}, //paramMark},
								new Token[]{compare});
		bolFactor.addProduction(new TerminalToken[]{notMark},
								new Token[]{notMark, bolFactor, notExe});
		
		compare.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function}, //paramMark},
								new Token[]{expression, _compare});
		
		_compare.addProduction(new TerminalToken[]{equalMark},
								new Token[]{equalMark, expression, equalExe});
		_compare.addProduction(new TerminalToken[]{notEMark},
								new Token[]{notEMark, expression, notEqualExe});
		_compare.addProduction(new TerminalToken[]{greatMark},
								new Token[]{greatMark, expression, greatExe});
		_compare.addProduction(new TerminalToken[]{greatEMark},
								new Token[]{greatEMark, expression, greatEExe});
		_compare.addProduction(new TerminalToken[]{lessMark},
								new Token[]{lessMark, expression, lessExe});
		_compare.addProduction(new TerminalToken[]{lessEMark},
								new Token[]{lessEMark, expression, lessEExe});
		_compare.addProduction(new TerminalToken[]{andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		expression.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function}, //paramMark},
								new Token[]{term, _expression});
		
		_expression.addProduction(new TerminalToken[]{addMark},
								new Token[]{addMark, term, addExe, _expression});
		_expression.addProduction(new TerminalToken[]{minusMark},
								new Token[]{minusMark, term, minusExe, _expression});
		_expression.addProduction(new TerminalToken[]{equalMark, notEMark, greatMark, greatEMark, lessMark, lessEMark, andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		term.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function}, //paramMark},
								new Token[]{factor, _term});
		
		_term.addProduction(new TerminalToken[]{multiplyMark},
								new Token[]{multiplyMark, factor, multiplyExe, _term});
		_term.addProduction(new TerminalToken[]{divideMark},
								new Token[]{divideMark, factor, divideExe, _term});
		_term.addProduction(new TerminalToken[]{modMark},
								new Token[]{modMark, factor, modExe, _term});
		_term.addProduction(new TerminalToken[]{addMark, minusMark, equalMark, notEMark, greatMark, greatEMark, lessMark, lessEMark, andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		factor.addProduction(new TerminalToken[]{variable},
								new Token[]{variable, _objectMembers});
		factor.addProduction(new TerminalToken[]{constant},
								new Token[]{constant});
		factor.addProduction(new TerminalToken[]{minusMark},
								new Token[]{minusMark, factor, negativeExe});
		factor.addProduction(new TerminalToken[]{leftBracket},
								new Token[]{leftBracket, bolExpression, rightBracket});
		
		factor.addProduction(new TerminalToken[]{function}, 
								new Token[]{function, leftBracket, parameters, rightBracket, functionExe});
//		factor.addProduction(new TerminalToken[]{paramMark},
//								new Token[]{paramMark, variable, paramExe, _member});
		

		_objectMembers.addProduction(new TerminalToken[]{dotMark}, 
									new Token[]{dotMark, _member});
		_objectMembers.addProduction(new TerminalToken[]{addMark, minusMark, multiplyMark, divideMark, modMark, equalMark, notEMark, greatMark, greatEMark, lessMark, lessEMark, andMark, orMark, leftBracket, rightBracket, comma, semicolon},
									new Token[]{});
		_member.addProduction(new TerminalToken[]{objectField}, 
								new Token[]{objectField, dotExe, fieldGetExe});
		_member.addProduction(new TerminalToken[]{objectMethod},
								new Token[]{objectMethod, dotExe, leftBracket, parameters, rightBracket, functionExe});
		_member.addProduction(new TerminalToken[]{objectFieldToBeAssigned}, 
								new Token[]{objectFieldToBeAssigned, dotExe, assignMark, bolExpression, fieldSetExe});
		
		
		parameters.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, function}, //paramMark},
								new Token[]{bolExpression, _parameters});
		parameters.addProduction(new TerminalToken[]{rightBracket},
								new Token[]{});
		
		_parameters.addProduction(new TerminalToken[]{comma},
								new Token[]{comma, bolExpression, _parameters});
		_parameters.addProduction(new TerminalToken[]{rightBracket},
								new Token[]{});
	}
	
	public static Grammar getGrammar() {
		return new Grammar();
	}
	
	public NonterminalToken getStart() {
		return start;
	}
	
	public TerminalToken getGrammarEnd() {
		return semicolon;
	}
	
	public TerminalToken getAssignMark() {
		return assignMark;
	}
	
	//����
	private ConstToken constant = TokenBuilder.getBuilder().buildConst();
	
	//����
	private VariableToken variable = TokenBuilder.getBuilder().buildVariable();
	
	//�������Ժͷ���
	private ObjectFieldToken objectField = TokenBuilder.getBuilder().buildObjectField();
	private ObjectMethodToken objectMethod = TokenBuilder.getBuilder().buildObjectMethod();
	private ObjectFieldToken objectFieldToBeAssigned = TokenBuilder.getBuilder().toBeAssigned(true).buildObjectField();
	
	//Ҫ����ֵ�ı���
	private VariableToken variableToBeAssigned = TokenBuilder.getBuilder().toBeAssigned(true).buildVariable();
	
	//����
	private FunctionToken function = TokenBuilder.getBuilder().buildFunction();
	
	//���
	private DelimiterToken addMark =  TokenBuilder.getBuilder().text("+").buildDelimiter();
	private DelimiterToken minusMark = TokenBuilder.getBuilder().text("-").buildDelimiter();
	private DelimiterToken multiplyMark = TokenBuilder.getBuilder().text("*").buildDelimiter();
	private DelimiterToken divideMark = TokenBuilder.getBuilder().text("/").buildDelimiter();
	private DelimiterToken modMark = TokenBuilder.getBuilder().text("%").buildDelimiter();
	private DelimiterToken greatMark = TokenBuilder.getBuilder().text(">").buildDelimiter();
	private DelimiterToken greatEMark = TokenBuilder.getBuilder().text(">=").buildDelimiter();
	private DelimiterToken lessMark = TokenBuilder.getBuilder().text("<").buildDelimiter();
	private DelimiterToken lessEMark = TokenBuilder.getBuilder().text("<=").buildDelimiter();
	private DelimiterToken equalMark = TokenBuilder.getBuilder().text("==").buildDelimiter();
	private DelimiterToken notEMark = TokenBuilder.getBuilder().text("!=").buildDelimiter();
	private DelimiterToken andMark = TokenBuilder.getBuilder().text("&&").buildDelimiter();
	private DelimiterToken orMark = TokenBuilder.getBuilder().text("||").buildDelimiter();
	private DelimiterToken notMark = TokenBuilder.getBuilder().text("!").buildDelimiter();
	private DelimiterToken comma = TokenBuilder.getBuilder().text(",").buildDelimiter();
	private DelimiterToken semicolon = TokenBuilder.getBuilder().text(";").buildDelimiter();
	private DelimiterToken leftBracket = TokenBuilder.getBuilder().text("(").buildDelimiter();
	private DelimiterToken rightBracket = TokenBuilder.getBuilder().text(")").buildDelimiter();
	private DelimiterToken assignMark = TokenBuilder.getBuilder().text("=").buildDelimiter();
//	private DelimiterToken paramMark = TokenBuilder.getBuilder().text("$").buildDelimiter();
	private DelimiterToken dotMark = TokenBuilder.getBuilder().text(".").buildDelimiter();
	
	//�ؼ���
	private KeyToken ifKey = TokenBuilder.getBuilder().text("if").buildKey();
	private KeyToken elseKey = TokenBuilder.getBuilder().text("else").buildKey();
	private KeyToken endIfKey = TokenBuilder.getBuilder().text("endif").buildKey();
	
	//���ս��
	private NonterminalToken start = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken ifStatement = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken block = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken elseSection = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken sentence = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolExpression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _bolExpression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolTerm = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _bolTerm = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolFactor = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken compare = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _compare = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken expression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _expression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken term = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _term = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken factor = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken parameters = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _parameters = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _member = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _objectMembers = TokenBuilder.getBuilder().buildNT();
	
	//���嶯��
	private ExecutionToken addExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("ADD")).buildExecution();
	private ExecutionToken minusExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MINUS")).buildExecution();
	private ExecutionToken multiplyExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MULTIPLY")).buildExecution();
	private ExecutionToken divideExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("DIVIDE")).buildExecution();
	private ExecutionToken modExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MOD")).buildExecution();
	private ExecutionToken negativeExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NEGATIVE")).buildExecution();
	private ExecutionToken andExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("AND")).buildExecution();
	private ExecutionToken orExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("OR")).buildExecution();
	private ExecutionToken notExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NOT")).buildExecution();
	private ExecutionToken greatExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("GREAT")).buildExecution();
	private ExecutionToken greatEExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("GREATE")).buildExecution();
	private ExecutionToken lessExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("LESS")).buildExecution();
	private ExecutionToken lessEExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("LESSE")).buildExecution();
	private ExecutionToken equalExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("EQUAL")).buildExecution();
	private ExecutionToken notEqualExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NOTEQUAL")).buildExecution();
	private ExecutionToken assignExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("ASSIGN")).buildExecution();
	private ExecutionToken functionExe = TokenBuilder.getBuilder().executable(null).buildExecution();//����ִ�б�־
//	private ExecutionToken paramExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("PARAM")).buildExecution();
	private ExecutionToken dotExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("DOT")).buildExecution();
	private ExecutionToken fieldGetExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("FIELD_GET")).buildExecution();
	private ExecutionToken fieldSetExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("FIELD_SET")).buildExecution();
	
	//�����Ĳ�������
	private ContextOperationToken ifConditionCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.IF_CONDITION).buildContextOperation();
	private ContextOperationToken elseConditionCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.ELSE_CONDITION).buildContextOperation();
	private ContextOperationToken newContextCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.NEW_CONTEXT).buildContextOperation();
	private ContextOperationToken endContextCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.END_CONTEXT).buildContextOperation();
	private ContextOperationToken endIFCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.END_IF).buildContextOperation();
	private ContextOperationToken andCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.AND_CONDITION).buildContextOperation();
	private ContextOperationToken endAndCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.END_AND).buildContextOperation();
	private ContextOperationToken orCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.OR_CONDITION).buildContextOperation();
	private ContextOperationToken endOrCo = TokenBuilder.getBuilder().contextOperation(ContextOperation.END_OR).buildContextOperation();
}
