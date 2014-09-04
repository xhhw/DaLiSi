package com.zb.dalisi.app;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import com.zb.dalisi.db.DBEngine;
import com.zb.dalisi.db.bean.DBZrulerCallLog;
import com.zb.dalisi.db.bean.DBZrulerCallLogDetail;

public class AppResult implements Observer {
	private ArrayList<AppResultItem> resluts = new ArrayList<AppResultItem>();

	public void getReslut(ResultObservable rule) {
		// TODO Auto-generated method stub
		
		AppResultItem resultItem = new AppResultItem();
		resultItem.setId(((AppRule)rule).getRule().getRuleId());
		resultItem.setResult(((AppRule)rule).getResult());
		resultItem.setCode(((AppRule)rule).getRule().getMsgCode());
		resluts.add(resultItem);
		
		System.out.println(resultItem.toString());
	}

	public ArrayList<AppResultItem> getResluts() {
		return resluts;
	}
	
	public void loggingDB(DBZrulerCallLog rec) {
		for (int i = 0; i < resluts.size(); i++) {
			DBZrulerCallLogDetail detail = new DBZrulerCallLogDetail();
			detail.setAppId(rec.getAppId());
			detail.setDoneDate(rec.getDoneDate());
			detail.setRuleId(resluts.get(i).getId());
			detail.setMsgCode(resluts.get(i).getCode());
			detail.setResult(new BigDecimal(resluts.get(i).isPassed()?0:1).intValue());
			DBEngine.instance(detail.getClass()).insert(detail);
		}
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		Iterator<AppResultItem> iter = resluts.iterator();
		while(iter.hasNext()){
			AppResultItem item = iter.next();
			buf.append(item.toString());
		}
		return buf.toString();
	}
}
