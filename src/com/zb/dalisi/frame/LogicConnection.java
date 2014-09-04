package com.zb.dalisi.frame;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogicConnection implements Connection {
	private static transient Log log = LogFactory.getLog(LogicConnection.class);
	
	private Connection physicalConnection = null;
	private int queryTimeoutSeconds = -1;
	private boolean isClosed = false;
	
	public LogicConnection(Connection physicalConnection){
		if(physicalConnection==null){
			throw new RuntimeException("com.zb.dalisi.common.LogicConnection:physical conn is null.");
		}
		this.physicalConnection = physicalConnection;
	}
	
	public Connection getPhysicalConnection(){
		return this.physicalConnection;
	}
	
	public String toString(){
		return "PhysicalConneciton:" + this.physicalConnection + ",LogicConneciton:" + super.toString();
	}
	
	public void setQueryTimeout(int queryTimeoutSeconds) {
		this.queryTimeoutSeconds = queryTimeoutSeconds;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		// TODO Auto-generated method stub
		Statement ret = null;
		ret = this.physicalConnection.createStatement();
		if(ret != null && this.queryTimeoutSeconds>0){
			ret.setQueryTimeout(this.queryTimeoutSeconds);
		}
		return ret;
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement ret = null;
		ret = this.physicalConnection.prepareStatement(sql);
		if (ret != null && this.queryTimeoutSeconds > 0) {
			ret.setQueryTimeout(this.queryTimeoutSeconds);
		}
		return ret;
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareCall(sql);
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.nativeSQL(sql);
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getAutoCommit();
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		throw new RuntimeException("com.zb.dalisi.common.LogicConnection: logic conn commit nonsupport.");
	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.rollback();
	}

	@Override
	public void close() throws SQLException {
		// TODO Auto-generated method stub
		this.isClosed = true;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return this.isClosed;
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getCatalog();
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.setTransactionIsolation(level);
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getTransactionIsolation();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.clearWarnings();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		this.physicalConnection.releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		this.physicalConnection.setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		this.physicalConnection.setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.physicalConnection.createStruct(typeName, attributes);
	}

}
