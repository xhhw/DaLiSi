package com.zb.dalisi.expression.lexical.dfa;

/**
 * �����Զ����м�״̬���루������ʼ״̬��
 * @author zhangbin2
 *
 */
public enum DFAMidStateCode {
	//��ʼ״̬
	START,
	
	//�����м�״̬
	NUMBER_1,
	NUMBER_2,
	NUMBER_3,
	NUMBER_4,
	NUMBER_5,
	NUMBER_6,
	NUMBER_7,
	
	//��ʶ���м�״̬
	ID_1,
	
	//����м�״̬
	DELIMITER_1,
	DELIMITER_2,
	
	//�����м�״̬
	DATE_1,
	DATE_2,
	
	//�ַ��м�״̬
	CHAR_1,
	CHAR_2,
	CHAR_3,
	CHAR_4,
	CHAR_5,
	
	//�ַ����м�״̬
	STRING_1,
	STRING_2,
	STRING_3,
	STRING_4,
	
	//�Զ������״̬
	OBJECT_1
}
