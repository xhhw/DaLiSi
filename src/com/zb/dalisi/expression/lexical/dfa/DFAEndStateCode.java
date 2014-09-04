package com.zb.dalisi.expression.lexical.dfa;

/**
 * ���������Զ����Ľ���״̬����
 * @author zhangbin2
 *
 */
public enum DFAEndStateCode {
	NUMBER_END,	//���ֽ���״̬
	NUMBER_INT_END,	//int���ֽ���״̬
	NUMBER_LONG_END,	//long���ֽ���״̬
	
	//��ʶ������״̬
	//ʶ�����ʶ��ʱ���������ж��Ƿ�Ϊ�������������ؼ��֡�������������
	ID_END,	
	
	//���ַ��������״̬����+��-��
	SINGLE_DELIMITER_END,
	
	//˫�ַ��������״̬����>=��&&��
	DOUBLE_DELIMITER_END,
	
	//���ڽ���״̬
	DATE_END,
	
	//�ַ�����״̬����ʹ��ת���ַ���
	CHAR_END,
	
	//�ַ�������״̬����ʹ��ת���ַ���
	STRING_END,
	
	//�Զ������״̬
	OBJECT_END
}
