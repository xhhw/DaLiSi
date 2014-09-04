package com.zb.dalisi.expression.lexical.dfa;

/**
 * ָ�����״̬��·��
 * @author zhangbin2
 *
 */
public class RouteToEndState {
	/**
	 * ·����ʹ��������ʽ��ʾ
	 */
	private String route;
	
	/**
	 * ����״̬����
	 */
	private DFAEndStateCode endStateCode;
	
	public RouteToEndState(String route, DFAEndStateCode endStateCode) {
		this.route = route;
		this.endStateCode = endStateCode;
	}

	public DFAEndStateCode getEndStateCode() {
		return endStateCode;
	}
	
	public String getRoute() {
		return route;
	}
	
	/**
	 * ���������ַ���ȡ����״̬���룬�����·����ƥ�䣬����null
	 * @param inputChar
	 * @return
	 */
	public DFAEndStateCode goToEndStateWithInput(Character inputChar) {
		if(inputChar.toString().matches(route))
			return endStateCode;
		return null;
	}
}
