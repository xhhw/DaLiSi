package com.zb.dalisi.utils;

import static com.zb.dalisi.expression.lexical.LexicalConstants.ACCURATE_DATE_PATTERN;
import static com.zb.dalisi.expression.lexical.LexicalConstants.DATE_PATTERN;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.zb.dalisi.app.module.EDataType;

public class DataTypeUtil {
	public static Object newObjFromString(String value, EDataType type){
		Object obj = null;
		switch(type){
		case STRING:
			obj = value;
			break;
		case NUMBER:
			obj = new BigDecimal(value);
			break;
		case DATETIME:
			Calendar date = null;
			DateFormat dateFormate;
			try{
				if(value.matches(DATE_PATTERN)) {
					//日期格式yyyy-MM-dd
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(value));
				} else if(value.matches(ACCURATE_DATE_PATTERN)) {
					//日期格式yyyy-MM-dd HH:mm:ss
					dateFormate = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
					date = Calendar.getInstance();
					date.setTime(dateFormate.parse(value));
				} else {
					throw new Exception("Wrong date format, please input as [yyyy-MM-dd] or [yyyy-MM-dd HH:mm:ss].");
				}
			} catch (Exception e){
				
			}
			obj = date;
			break;
		case OBJECT:
			try {
				obj = Class.forName(value).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return obj;
	}
	
	public static String clobToString(Clob c1) {
		Reader r;
		String str = null;
		try {
			r = c1.getCharacterStream();
			ByteArrayOutputStream sb = new ByteArrayOutputStream();
			int i = -1;
			while ((i = r.read()) != -1) {
				sb.write(i);
			}
			str = new String(sb.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public static void setPrepareStatementParameter(PreparedStatement stmt,
			int index, String type, Object value) throws SQLException {
		if (type.equalsIgnoreCase("String")) {
			String content = value.toString();
			if (content.length() > 2000) {
				stmt.setCharacterStream(index, new StringReader(content),
						content.length());
			} else
				stmt.setString(index, content);
		} else if (type.equalsIgnoreCase("Short")) {
			stmt.setShort(index, Short.parseShort(value.toString()));
		} else if (type.equalsIgnoreCase("Integer")) {
			stmt.setInt(index, Integer.parseInt(value.toString()));
		} else if (type.equalsIgnoreCase("Long")) {
			stmt.setLong(index, Long.parseLong(value.toString()));
		} else if (type.equalsIgnoreCase("Double")) {
			stmt.setDouble(index, Double.parseDouble(value.toString()));
		} else if (type.equalsIgnoreCase("Float")) {
			stmt.setFloat(index, Float.parseFloat(value.toString()));
		} else if (type.equalsIgnoreCase("Byte")) {
			stmt.setByte(index, Byte.parseByte(value.toString()));
		} else if (type.equalsIgnoreCase("Char")) {
			stmt.setString(index, value.toString());
		} else if (type.equalsIgnoreCase("Boolean")) {
			stmt.setBoolean(index, Boolean.getBoolean(value.toString()));
		} else if (type.equalsIgnoreCase("Date")) {
			if (value instanceof java.sql.Date)
				stmt.setDate(index, (java.sql.Date)value);
			else
				stmt.setDate(index, java.sql.Date.valueOf(value.toString()));
		} else if (type.equalsIgnoreCase("Time")) {
			if (value instanceof Time)
				stmt.setTime(index, (Time)value);
			else
				stmt.setTime(index, Time.valueOf(value.toString()));
		} else if (type.equalsIgnoreCase("DateTime")) {
			if (value instanceof Timestamp)
				stmt.setTimestamp(index, (Timestamp)value);
			else if (value instanceof java.sql.Date)
				stmt.setTimestamp(index,
						new Timestamp(((java.sql.Date) value).getTime()));
			else
				stmt.setTimestamp(index, Timestamp.valueOf(value.toString()));
		} else if (value instanceof Character) {
			stmt.setString(index, value.toString());
		} else {
			stmt.setObject(index, value);
		}
	}
}
