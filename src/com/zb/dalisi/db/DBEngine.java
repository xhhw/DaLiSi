package com.zb.dalisi.db;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.zb.dalisi.db.bean.DBZrulerCallLog;
import com.zb.dalisi.utils.DataTypeUtil;

public class DBEngine<T extends DBBase> implements DBOperateIntf<T> {
	private Class<T> type;
	
	private DBEngine(Class<T> type){
		this.type = type;
	}
	
	public static DBEngine instance(Class<?> type){
		return new DBEngine(type);
	}
	
	public String getTableName(){
		String tableName = null;
		tableName =  type.getSimpleName();
		tableName = decodeTableName(tableName);
//		String tmp = encodeTableName(tableName);
		return tableName;
	}
	
	public String decodeColName(String src){
		//引擎所用JAVA对象为DBTableName解码转换成数据库表名为table_name
		StringBuffer buf = new StringBuffer();
		char[] c = src.toCharArray();
		for (int i=0; i<c.length; i++){
			if(i>0 && Character.isUpperCase(c[i])){
				buf.append('_');
			}
			buf.append(Character.toLowerCase(c[i]));
		}
		return buf.toString();
	}
	
	public String decodeTableName(String src){
		//引擎所用JAVA对象为DBTableName解码转换成数据库表名为table_name
		StringBuffer buf = new StringBuffer();
		char[] c = src.toCharArray();
		for (int i=2; i<c.length; i++){
			if(i>2 && Character.isUpperCase(c[i])){
				buf.append('_');
			}
			buf.append(Character.toLowerCase(c[i]));
		}
		return buf.toString();
	}
	
	public String encodeTableName(String src){
		//数据库表名为table_name编码成引擎所用JAVA对象为DBTableName
		StringBuffer buf = new StringBuffer();
		char[] c = src.toCharArray();
		for (int i=-1; i<c.length; i++){
			if(i==-1 || c[i]=='_'){
				i++;
				buf.append(Character.toUpperCase(c[i]));
			}else{
				buf.append(c[i]);
			}
		}
		return buf.toString();
	}

	@Override
	public boolean insert(T obj) {
		boolean bRet = false;
		Connection conn = DBPool.getInstance().getConnection();
		try {
			ArrayList plist = new ArrayList();
			ArrayList ptype = new ArrayList();
			
			Field[] fields = obj.getClass().getDeclaredFields();
			boolean isFirst = true;
			StringBuilder insertFields = new StringBuilder();
			StringBuilder insertValues = new StringBuilder();
			for(int i=0; i<fields.length; i++){
				fields[i].setAccessible(true);
				Object value = fields[i].get(obj);
				if (value!=null){
					String name = decodeColName(fields[i].getName());
					System.out.println("插入字段 "+name+"="+value);
					if (isFirst){
						insertFields.append(" ("+name+"");
						insertValues.append(" (?");
						ptype.add(fields[i].getGenericType().toString());
						plist.add(value);
						isFirst = false;
					}else{
						insertFields.append(", "+name+"");
						insertValues.append(", ?");
						ptype.add(fields[i].getGenericType().toString());
						plist.add(value);
					}
				}
			}
			if(insertFields.toString().equals("")){
				System.out.println("没有要插入的字段！ ");
				return false;
			}
			insertFields.append(") ");
			insertValues.append(") ");
			
			String table = getTableName();
			String strSql = "insert into "+table+insertFields.toString()+" values "+insertValues.toString();
			System.out.println(strSql);
			PreparedStatement pstmt = conn.prepareStatement(strSql);
			for (int i = 0; i < plist.size(); ++i) {
				if (plist.get(i) instanceof String) {
					String content = (String) plist.get(i);
					if (content.length() > 2000) {
						pstmt.setCharacterStream(i + 1, new StringReader(
								content), content.length());
					} else
						pstmt.setString(i + 1, content);
				} else {
					pstmt.setObject(i + 1, plist.get(i));
//					DataTypeUtil.setPrepareStatementParameter(pstmt, i + 1,
//							(String) ptype.get(i), plist.get(i));
				}
			}
			bRet = pstmt.execute();
			pstmt.close();
			DBPool.getInstance().freeConnection(conn);
			bRet = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("插入记录！");
		return bRet;
	}

	@Override
	public int delete(T obj) {
		Connection conn = DBPool.getInstance().getConnection();
		try {
			String table = getTableName();
			ArrayList<T> rows = new ArrayList<T>();
			
			ArrayList plist = new ArrayList();
			ArrayList ptype = new ArrayList();
			StringBuilder strCondition = new StringBuilder();
			Field[] fields = obj.getClass().getDeclaredFields();
			for(int i=0; i<fields.length; i++){
				fields[i].setAccessible(true);
				Object value = fields[i].get(obj);
				if (value!=null){
					String name = decodeColName(fields[i].getName());
					System.out.println("删除记录的关联字段 "+name+"="+value);
					strCondition.append(" and "+name+"=?");
					ptype.add(fields[i].getGenericType());
					plist.add(value);
				}
			}
			if(strCondition.toString().equals("")){
				System.out.println("删除数据必须要带条件，严禁全表删除！ ");
				return 0;
			}
			
			String strSql = "delete from "+table+" where 1=1"+strCondition.toString();
			PreparedStatement pstmt = conn.prepareStatement(strSql);
			for (int i = 0; i < plist.size(); ++i) {
				if (plist.get(i) instanceof String) {
					String content = (String) plist.get(i);
					if (content.length() > 2000) {
						pstmt.setCharacterStream(i + 1, new StringReader(
								content), content.length());
					} else
						pstmt.setString(i + 1, content);
				} else {
					DataTypeUtil.setPrepareStatementParameter(pstmt, i + 1,
							(String) ptype.get(i), plist.get(i));
				}
			}

			ResultSet rs = pstmt.executeQuery();
			rs.close();
			pstmt.close();
			DBPool.getInstance().freeConnection(conn);
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	@Override
	public ArrayList<T> selectAll() {
		Connection conn = DBPool.getInstance().getConnection();
		try {
			String table = getTableName();
			ArrayList<T> rows = new ArrayList<T>();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from "+table);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			Map<String, Integer> props = new HashMap<String, Integer>();
			for (int i=1; i<=rsmd.getColumnCount(); i++){
				String strColumnName = rsmd.getColumnName(i).toLowerCase();
				int type = rsmd.getColumnType(i);
				props.put(strColumnName, type);
			}
			
			while (rs.next()) {
				T obj = type.newInstance();
				Field[] fields = obj.getClass().getDeclaredFields();
				for(int i=0; i<fields.length; i++){
					fields[i].setAccessible(true);
					String name = decodeColName(fields[i].getName());
					Object value = getValueByDataType(rs,props.get(name),name);
					fields[i].set(obj, value);
				}
				rows.add(obj);
			}
			rs.close();
			stmt.close();
			DBPool.getInstance().freeConnection(conn);
			return rows;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public Object getValueByDataType(ResultSet rs, int typeid, String colName) throws SQLException{
		Object obj = null;
		switch(typeid){
		case java.sql.Types.BIT:obj=rs.getBoolean(colName);break;
		case java.sql.Types.TINYINT:obj=rs.getByte(colName);break;
		case java.sql.Types.SMALLINT:obj=rs.getShort(colName);break;
		case java.sql.Types.INTEGER:obj=rs.getInt(colName);break;
		case java.sql.Types.BIGINT:obj=rs.getLong(colName);break;
		case java.sql.Types.FLOAT:obj=rs.getDouble(colName);break;
		case java.sql.Types.REAL:obj=rs.getFloat(colName);break;
		case java.sql.Types.DOUBLE:obj=rs.getDouble(colName);break;
		case java.sql.Types.NUMERIC:obj=rs.getBigDecimal(colName);break;
		case java.sql.Types.DECIMAL:obj=rs.getBigDecimal(colName);break;
		case java.sql.Types.CHAR:obj=rs.getString(colName);break;
		case java.sql.Types.VARCHAR:obj=rs.getString(colName);break;
		case java.sql.Types.LONGVARCHAR:obj=rs.getString(colName);break;
		case java.sql.Types.DATE:obj=new Date(rs.getDate(colName).getTime());break;
		case java.sql.Types.TIME:obj=new Date(rs.getTime(colName).getTime());break;
		case java.sql.Types.TIMESTAMP:obj=new Date(rs.getTimestamp(colName).getTime());break;
		case java.sql.Types.BINARY:obj=rs.getBytes(colName);break;
		case java.sql.Types.VARBINARY:obj=rs.getBytes(colName);break;
		case java.sql.Types.LONGVARBINARY:obj=rs.getBytes(colName);break;
		case java.sql.Types.CLOB:obj=DataTypeUtil.clobToString(rs.getClob(colName));break;
		default:
		}
		return obj;
	}

	@Override
	public ArrayList<T> select(Map conditionMap) {
		Connection conn = DBPool.getInstance().getConnection();
		try {
			String table = getTableName();
			ArrayList<T> rows = new ArrayList<T>();
			
			ArrayList plist = new ArrayList();
			ArrayList ptype = new ArrayList();
			StringBuilder strCondition = new StringBuilder();
			Set<Map.Entry<String, Object>> set = conditionMap.entrySet();
			Iterator<Map.Entry<String, Object>> iter = set.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
				strCondition.append(" and "+entry.getKey()+"=?");
				ptype.add(entry.getValue().getClass());
				plist.add(entry.getValue());
			}
			
			String strSql = "select * from "+table+" where 1=1"+strCondition.toString();
			PreparedStatement pstmt = conn.prepareStatement(strSql);
			for (int i = 0; i < plist.size(); ++i) {
				if (plist.get(i) instanceof String) {
					String content = (String) plist.get(i);
					if (content.length() > 2000) {
						pstmt.setCharacterStream(i + 1, new StringReader(
								content), content.length());
					} else
						pstmt.setString(i + 1, content);
				} else {
					DataTypeUtil.setPrepareStatementParameter(pstmt, i + 1,
							(String) ptype.get(i), plist.get(i));
				}
			}

			ResultSet rs = pstmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			Map<String, Integer> props = new HashMap<String, Integer>();
			for (int i=1; i<=rsmd.getColumnCount(); i++){
				String strColumnName = rsmd.getColumnName(i).toLowerCase();
				int type = rsmd.getColumnType(i);
				props.put(strColumnName, type);
			}
			
			while (rs.next()) {
				T obj = type.newInstance();
				Field[] fields = obj.getClass().getDeclaredFields();
				for(int i=0; i<fields.length; i++){
					fields[i].setAccessible(true);
					String name = decodeColName(fields[i].getName());
					Object value = getValueByDataType(rs,props.get(name),name);
					fields[i].set(obj, value);
				}
				rows.add(obj);
			}
			rs.close();
			pstmt.close();
			DBPool.getInstance().freeConnection(conn);
			return rows;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public int update(T obj) {
		int count = 0;
		Connection conn = DBPool.getInstance().getConnection();
		try {
			ArrayList plist = new ArrayList();
			ArrayList ptype = new ArrayList();
			
			Field[] fields = obj.getClass().getDeclaredFields();
			boolean isFirst = true;
			StringBuilder updateFields = new StringBuilder();
			for(int i=0; i<fields.length; i++){
				fields[i].setAccessible(true);
				Object value = fields[i].get(obj);
				if (value!=null){
					String name = decodeColName(fields[i].getName());
					System.out.println("更新字段 "+name+"="+value);
					if (isFirst){
						updateFields.append(" "+name+"=? ");
						ptype.add(fields[i].getGenericType());
						plist.add(value);
						isFirst = false;
					}else{
						updateFields.append(", "+name+"=? ");
						ptype.add(fields[i].getGenericType());
						plist.add(value);
					}
				}
			}
			if(updateFields.toString().equals("")){
				System.out.println("没有要更新的字段！ ");
				return 0;
			}
			
			String table = getTableName();
			String strSql = "update "+table+" set "+updateFields.toString()+" where 1=1";
			PreparedStatement pstmt = conn.prepareStatement(strSql);
			for (int i = 0; i < plist.size(); ++i) {
				if (plist.get(i) instanceof String) {
					String content = (String) plist.get(i);
					if (content.length() > 2000) {
						pstmt.setCharacterStream(i + 1, new StringReader(
								content), content.length());
					} else
						pstmt.setString(i + 1, content);
				} else {
					DataTypeUtil.setPrepareStatementParameter(pstmt, i + 1,
							(String) ptype.get(i), plist.get(i));
				}
			}
			count = pstmt.executeUpdate();
			pstmt.close();
			DBPool.getInstance().freeConnection(conn);
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("更新了 "+count+" 条记录！");
		return count;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		DBEngine obj = new DBEngine(DBZrulerRule.class);
//		ArrayList<DBBase> list = (ArrayList<DBBase>) obj.selectAll();
//		Iterator<DBBase> iter = list.iterator();
//		while(iter.hasNext()){
//			DBBase a = iter.next();
//			System.out.println(a.toString());
//		}
		DBZrulerCallLog data = new DBZrulerCallLog();
//		data.setExt2("test");
//		System.out.println(DBEngine.instance(data.getClass()).update(data));
		data.setAppId("测试01");
		data.setAppArgs("我靠我是CLOB字段啊！");
		data.setResult(new BigDecimal("12").intValue());
		data.setMsgCode("500001");
		data.setDoneDate(new Timestamp((new Date()).getTime()));
		System.out.println(DBEngine.instance(data.getClass()).insert(data));
	}

}
