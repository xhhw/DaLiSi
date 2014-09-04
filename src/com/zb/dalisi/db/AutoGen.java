package com.zb.dalisi.db;
import java.io.*;
import java.net.URL;
import java.sql.*;

public class AutoGen {
	
	public static String replaceUnderline(String in) {
		int cursor=0;
		StringBuffer out=new StringBuffer();
		while(cursor<in.length()){
			char nextChar = in.charAt(cursor);
			if (nextChar=='_'){
				cursor++;
				nextChar = in.charAt(cursor);
				if ((('a'<=nextChar)&&(nextChar<='z'))==true){
					nextChar = (char)(nextChar-32);
				}
			}
			out.append(nextChar);
			cursor++;
		}
		return out.toString();
	}
	
	public static String tableNameToClassName(String inStr) {
		String in = inStr.toLowerCase();
		int cursor=0;
		StringBuffer out=new StringBuffer();
		while(cursor<in.length()){
			char nextChar = in.charAt(cursor);
			if (cursor==0) {
				if ((('a'<=nextChar)&&(nextChar<='z'))==true){
					nextChar = (char)(nextChar-32);
				}
			}
			if (nextChar=='_'){
				cursor++;
				nextChar = in.charAt(cursor);
				if ((('a'<=nextChar)&&(nextChar<='z'))==true){
					nextChar = (char)(nextChar-32);
				}
			}
			out.append(nextChar);
			cursor++;
		}
		return out.toString();
	}
	
	public static String getJavaType(int typeid){
		String javaType=null;
		switch(typeid){
		case java.sql.Types.BIT:javaType="boolean";break;
		case java.sql.Types.TINYINT:javaType="byte";break;
		case java.sql.Types.SMALLINT:javaType="short";break;
		case java.sql.Types.INTEGER:javaType="int";break;
		case java.sql.Types.BIGINT:javaType="long";break;
		case java.sql.Types.FLOAT:javaType="double";break;
		case java.sql.Types.REAL:javaType="float";break;
		case java.sql.Types.DOUBLE:javaType="double";break;
		case java.sql.Types.NUMERIC:javaType="java.math.BigDecimal";break;
		case java.sql.Types.DECIMAL:javaType="java.math.BigDecimal";break;
		case java.sql.Types.CHAR:javaType="String";break;
		case java.sql.Types.VARCHAR:javaType="String";break;
		case java.sql.Types.LONGVARCHAR:javaType="String";break;
		case java.sql.Types.DATE:javaType="java.util.Date";break;//javaType="java.sql.Date";break;
		case java.sql.Types.TIME:javaType="java.util.Date";break;//javaType="java.sql.Time";break;
		case java.sql.Types.TIMESTAMP:javaType="java.util.Date";break;//javaType="java.sql.Timestamp";break;
		case java.sql.Types.BINARY:javaType="byte[]";break;
		case java.sql.Types.VARBINARY:javaType="byte[]";break;
		case java.sql.Types.LONGVARBINARY:javaType="byte[]";break;
		case java.sql.Types.CLOB:javaType="String";break;
		default:
			javaType="Undefined";
		}
		return javaType;
	}
	
	public static String getDefaultValue(int typeid){
		String defaultValue=null;
		switch(typeid){
		case java.sql.Types.BIT:defaultValue="false";break;
		case java.sql.Types.TINYINT:defaultValue="0";break;
		case java.sql.Types.SMALLINT:defaultValue="0";break;
		case java.sql.Types.INTEGER:defaultValue="0";break;
		case java.sql.Types.BIGINT:defaultValue="0";break;
		case java.sql.Types.FLOAT:defaultValue="0";break;
		case java.sql.Types.REAL:defaultValue="0";break;
		case java.sql.Types.DOUBLE:defaultValue="0";break;
		case java.sql.Types.NUMERIC:defaultValue="null";break;
		case java.sql.Types.DECIMAL:defaultValue="null";break;
		case java.sql.Types.CHAR:defaultValue="null";break;
		case java.sql.Types.VARCHAR:defaultValue="null";break;
		case java.sql.Types.LONGVARCHAR:defaultValue="null";break;
		case java.sql.Types.DATE:defaultValue="null";break;
		case java.sql.Types.TIME:defaultValue="null";break;
		case java.sql.Types.TIMESTAMP:defaultValue="null";break;
		case java.sql.Types.BINARY:defaultValue="null";break;
		case java.sql.Types.VARBINARY:defaultValue="null";break;
		case java.sql.Types.LONGVARBINARY:defaultValue="null";break;
		default:
			defaultValue="null";
		}
		return defaultValue;
	}
	
	public static String getFunction(int typeid){
		String strFunction=null;
		switch(typeid){
		case java.sql.Types.BIT:strFunction="getBoolean";break;
		case java.sql.Types.TINYINT:strFunction="getByte";break;
		case java.sql.Types.SMALLINT:strFunction="getShort";break;
		case java.sql.Types.INTEGER:strFunction="getInt";break;
		case java.sql.Types.BIGINT:strFunction="getLong";break;
		case java.sql.Types.FLOAT:strFunction="getDouble";break;
		case java.sql.Types.REAL:strFunction="getFloat";break;
		case java.sql.Types.DOUBLE:strFunction="getDouble";break;
		case java.sql.Types.NUMERIC:strFunction="getBigDecimal";break;
		case java.sql.Types.DECIMAL:strFunction="getBigDecimal";break;
		case java.sql.Types.CHAR:strFunction="getString";break;
		case java.sql.Types.VARCHAR:strFunction="getString";break;
		case java.sql.Types.LONGVARCHAR:strFunction="getString";break;
		case java.sql.Types.DATE:strFunction="getDate";break;
		case java.sql.Types.TIME:strFunction="getTime";break;
		case java.sql.Types.TIMESTAMP:strFunction="getTimestamp";break;
		case java.sql.Types.BINARY:strFunction="getBytes";break;
		case java.sql.Types.VARBINARY:strFunction="getBytes";break;
		case java.sql.Types.LONGVARBINARY:strFunction="getBytes";break;
		default:
			strFunction="null";
		}
		return strFunction;
	}
	
	public static void genJavaCode(String table) {
		String tplClass=null;
		String tplParam=null;
		String tplGetfunction=null;
		String tplSetfunction=null;
		String tplTostring=null;
		String tplRecord=null;
		String tplUpdateRec=null;
		BufferedReader breader=null;
		StringBuffer buf=null;
		URL resUrl=null;
		String strPath=null;
		try {
			ClassLoader classLoader = AutoGen.class.getClassLoader();
	        resUrl = classLoader.getResource("");
	        strPath = resUrl.getPath()+"../../src/com/zb/dalisi/db/bean/";
	        
	        resUrl = classLoader.getResource("templates/template_class.tpl");
	        InputStream in = resUrl.openStream();
	        breader = new BufferedReader(new InputStreamReader(in));
	        buf = new StringBuffer();
            while (breader.ready()) {
                buf.append((char) breader.read());
            }
            breader.close();
	        tplClass = buf.toString();
	        //System.out.println(tplClass.toString());
	        
	        resUrl = classLoader.getResource("templates/template_param.tpl");
	        in = resUrl.openStream();
	        breader = new BufferedReader(new InputStreamReader(in));
	        buf = new StringBuffer();
            while (breader.ready()) {
                buf.append((char) breader.read());
            }
            breader.close();
	        tplParam = buf.toString();
	        //System.out.println(tplParam.toString());
	        
	        resUrl = classLoader.getResource("templates/template_getfunction.tpl");
	        in = resUrl.openStream();
	        breader = new BufferedReader(new InputStreamReader(in));
	        buf = new StringBuffer();
            while (breader.ready()) {
                buf.append((char) breader.read());
            }
            breader.close();
	        tplGetfunction = buf.toString();
	        //System.out.println(tplGetfunction.toString());
	        
	        resUrl = classLoader.getResource("templates/template_setfunction.tpl");
	        in = resUrl.openStream();
	        breader = new BufferedReader(new InputStreamReader(in));
	        buf = new StringBuffer();
            while (breader.ready()) {
                buf.append((char) breader.read());
            }
            breader.close();
	        tplSetfunction = buf.toString();
	        //System.out.println(tplSetfunction.toString());
	        
	        resUrl = classLoader.getResource("templates/template_tostring.tpl");
	        in = resUrl.openStream();
	        breader = new BufferedReader(new InputStreamReader(in));
	        buf = new StringBuffer();
            while (breader.ready()) {
                buf.append((char) breader.read());
            }
            breader.close();
	        tplTostring = buf.toString();
	        //System.out.println(tplTostring.toString());
		} catch(Exception e) {
			System.out.println("IO Error : " + e.toString());
			System.exit(0);
		}
		StringBuffer outClass=new StringBuffer();
		StringBuffer outParam=new StringBuffer();
		StringBuffer outFunctionSet=new StringBuffer();
		StringBuffer outFunctionGet=new StringBuffer();
		StringBuffer outToString=new StringBuffer();
		
		String sql="SELECT * FROM "+table+" where 1=2";
		String strClassName=tableNameToClassName(table);
		try {
			Connection conn = DBPool.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i=1; i<=rsmd.getColumnCount(); i++){
				String strParamType=null;
				String strColumnName=null;
				String strParamDefaultValue=null;
				String strGetColumnValue=null;
				strColumnName = rsmd.getColumnName(i).toLowerCase();
				strColumnName = replaceUnderline(strColumnName);
				strParamType = getJavaType(rsmd.getColumnType(i));
				strParamDefaultValue = getDefaultValue(rsmd.getColumnType(i));
				strGetColumnValue = getFunction(rsmd.getColumnType(i));
				
				String strFuncColumnName = Character.toUpperCase(strColumnName.charAt(0))+strColumnName.substring(1);
				outParam.append(tplParam.replaceAll("\\{\\{column_name\\}\\}", strColumnName).replaceAll("\\{\\{param_type\\}\\}", strParamType).replaceAll("\\{\\{param_default_value\\}\\}", strParamDefaultValue));
				outFunctionGet.append(tplGetfunction.replaceAll("\\{\\{column_name\\}\\}", strColumnName).replaceAll("\\{\\{param_type\\}\\}", strParamType).replaceAll("\\{\\{func_column_name\\}\\}", strFuncColumnName));
				outFunctionSet.append(tplSetfunction.replaceAll("\\{\\{column_name\\}\\}", strColumnName).replaceAll("\\{\\{param_type\\}\\}", strParamType).replaceAll("\\{\\{func_column_name\\}\\}", strFuncColumnName));
				outToString.append(tplTostring.replaceAll("\\{\\{upper_column_name\\}\\}",rsmd.getColumnName(i).toUpperCase()).replaceAll("\\{\\{column_name\\}\\}", strColumnName));
			}
			rs.close();
			stmt.close();
//			conn.close();
			DBPool.getInstance().freeConnection(conn);
		} catch(Exception e) {
			System.out.println("Query Error : " + e.toString());
		}
		outClass.append(tplClass.toString().replaceAll("\\{\\{class_name\\}\\}",
				strClassName).replaceAll("\\{\\{table_name\\}\\}",
				table).replaceAll("\\{\\{template_param_list\\}\\}",
				outParam.toString()).replaceAll("\\{\\{template_tostring_list\\}\\}",
				outToString.toString()).replaceAll("\\{\\{template_getfunction_list\\}\\}",
				outFunctionGet.toString()).replaceAll("\\{\\{template_setfunction_list\\}\\}",
				outFunctionSet.toString()));

		try
		{
			String fileName=strPath+"DB"+strClassName+".java";
			System.out.println(fileName);
			FileWriter fw = new FileWriter(fileName); 
			fw.write(outClass.toString());
			fw.flush();
		}
		catch (Exception e)
		{
			System.exit(1);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] tables = {
//				"zruler_key",
//				"zruler_attribute",
//				"zruler_msgcode",
//				"zruler_function",
//				"zruler_rule",
//				"zruler_scene",
//				"zruler_event",
//				"zruler_event_object",
//				"zruler_actionruleapp",
//				"zruler_dataruleapp",
//				"page_app",
				"page_js_function",
//				"ZRULER_CALL_LOG",
//				"ZRULER_CALL_LOG_DETAIL",
				"ZRULER_ACTIONRULE_EXT"
				};
		for (int i=0;i<tables.length;i++) {
			genJavaCode(tables[i]);
		}
		DBPool.getInstance().release();
	}

}
