package com.zb.dalisi.expression.syntax.function;

import com.zb.dalisi.app.AppRule;
import com.zb.dalisi.app.module.CDefRule;
import com.zb.dalisi.cache.CacheRule;
import com.zb.dalisi.expression.tokens.DataType;
import com.zb.dalisi.expression.tokens.Valuable;
import com.zb.dalisi.session.SessionManager;

public class CheckRule extends Function {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "checkRule";
	}

	@Override
	public DataType[] getArgumentsDataType() {
		// TODO Auto-generated method stub
		return new DataType[]{DataType.NUMBER};
	}

	@Override
	public int getArgumentNum() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	protected Object executeFunction(Valuable[] arguments) {
		// TODO Auto-generated method stub
		int ruleId = (Integer) arguments[0].getValue();
		CDefRule defRule = CacheRule.getCDefRule(String.valueOf(ruleId));
		AppRule curAppRule = SessionManager.getSession().getCurAppRule();
		AppRule appRule = new AppRule(defRule, curAppRule);
		appRule.registerObserver(SessionManager.getSession().getAppResult());
		
		String strExpression = appRule.getRule().getExpression();
		System.out.println(strExpression);
		boolean result = appRule.run();

		return result;
	}

}
