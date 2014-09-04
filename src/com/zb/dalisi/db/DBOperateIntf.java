package com.zb.dalisi.db;

import java.util.ArrayList;
import java.util.Map;

public interface DBOperateIntf<T extends DBBase> {
	public boolean insert(T obj);
	public int update(T obj);
	public int delete(T obj);
	public ArrayList<T> selectAll();
	public ArrayList<T> select(Map<?,?> conditionMap);
}
