package com.zb.dalisi.db.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import com.zb.dalisi.db.DBOperateIntf;

public abstract class DBObject implements DBOperateIntf {
	private String name = null;
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public DBObject(DBObject obj){
		this.name = obj.name;
		this.data.putAll(obj.data);
	}
	public DBObject(ResultSet rs) throws Exception{
		ResultSetMetaData rsmd = rs.getMetaData();
		this.name = rsmd.getTableName(1);
		for (int i=1; i<=rsmd.getColumnCount(); i++){
			String columnName = rsmd.getColumnName(i).toUpperCase();
			int typeid = rsmd.getColumnType(i);
			Object value = getValue(rs,i,typeid);
			data.put(columnName, value);
		}
	}
	public Object getValue(String columnName){
		String key = columnName.toUpperCase();
		if (data.containsKey(key))
			return data.get(key);
		return null;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (String key : data.keySet()) {
			buf.append(key+":"+data.get(key)+"\n");
		}
		return buf.toString();
	}
	
	public Object getValue(ResultSet rs, int i, int type) throws Exception{
		switch(type){
		case java.sql.Types.BIT:return rs.getBoolean(i);
		case java.sql.Types.TINYINT:return rs.getByte(i);
		case java.sql.Types.SMALLINT:return rs.getShort(i);
		case java.sql.Types.INTEGER:return rs.getInt(i);
		case java.sql.Types.BIGINT:return rs.getLong(i);
		case java.sql.Types.FLOAT:return rs.getDouble(i);
		case java.sql.Types.REAL:return rs.getFloat(i);
		case java.sql.Types.DOUBLE:return rs.getDouble(i);
		case java.sql.Types.NUMERIC:return rs.getBigDecimal(i);
		case java.sql.Types.DECIMAL:return rs.getBigDecimal(i);
		case java.sql.Types.CHAR:return rs.getString(i);
		case java.sql.Types.VARCHAR:return rs.getString(i);
		case java.sql.Types.LONGVARCHAR:return rs.getString(i);
		case java.sql.Types.DATE:return rs.getDate(i);
		case java.sql.Types.TIME:return rs.getTime(i);
		case java.sql.Types.TIMESTAMP:return rs.getTimestamp(i);
		case java.sql.Types.BINARY:return rs.getBytes(i);
		case java.sql.Types.VARBINARY:return rs.getBytes(i);
		case java.sql.Types.LONGVARBINARY:return rs.getBytes(i);
		case java.sql.Types.CLOB:return rs.getClob(i);
		case java.sql.Types.BLOB:return rs.getBlob(i);
		default:
			return rs.getObject(i);
		}
	}
}
