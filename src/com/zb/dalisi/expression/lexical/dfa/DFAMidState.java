package com.zb.dalisi.expression.lexical.dfa;

import java.util.HashMap;
import java.util.Map;

/**
 * �����Զ������м�״̬��������ʼ״̬��
 * @author zhangbin2
 *
 */
public class DFAMidState {
	/**
	 * �м�״̬����
	 */
	private DFAMidStateCode midStateCode = null;
	
	/**
	 * ״̬ת����������·�������Ӧ����һ���м�״̬��ӳ�䣬·��ʹ��������ʽ����
	 */
	private Map<String,DFAMidState> nextMidStateMap = new HashMap<String,DFAMidState>();
	
	/**
	 * �м�״̬������״̬��·��
	 */
	private RouteToEndState routeToEndState;
	
	/**
	 * ���������ַ��޷��ҵ���һ��״̬ʱ�Ĵ�����Ϣ�����ʷ�������Ϣ
	 */
	private String errorMessage = "Lexical error.";
	
	public DFAMidState(DFAMidStateCode midStateCode) {
		this.midStateCode = midStateCode;
	}
	
	public void setNextMidState(String pattern,DFAMidState nextState) {
		nextMidStateMap.put(pattern, nextState);
	}
	
	/**
	 * ���������ַ���ȡ��һ�м�״̬�������ƥ�䣬�Ż�null
	 * @param inputChar
	 * @return
	 */
	public DFAMidState getNextMidState(Character inputChar) {
		DFAMidState nextState = null;
		for(String pattern : nextMidStateMap.keySet()){
			if(inputChar.toString().matches(pattern)){
				nextState = nextMidStateMap.get(pattern);
				break;
			}
		}
		return nextState;
	}
	
	public void setRouteToEndState(String route, DFAEndStateCode endStateCode) {
		routeToEndState = new RouteToEndState(route, endStateCode);
	}
	
	/**
	 * ���������ַ���ȡ����״̬����
	 * @param inputChar
	 * @return
	 */
	public DFAEndStateCode goToEndStateWithInput(Character inputChar) {
		if(routeToEndState == null)
			return null;
		return routeToEndState.goToEndStateWithInput(inputChar);
	}
	
	/**
	 * ȡ���뱾�м�״̬����·���Ľ���״̬���룬
	 * ���������ָ�����״̬��·��������null
	 * @return
	 */
	public DFAEndStateCode getNextEndStateCode() {
		if(routeToEndState == null)
			return null;
		return routeToEndState.getEndStateCode();
	}
	
	public boolean hasRouteToEndState() {
		return routeToEndState != null;
	}
	
	public DFAMidStateCode getMidStateCode() {
		return midStateCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
