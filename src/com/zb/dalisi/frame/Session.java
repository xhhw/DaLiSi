package com.zb.dalisi.frame;

import java.sql.Connection;
import java.sql.SQLException;

public abstract interface Session {
	public abstract void startTransaction() throws Exception;
	public abstract void commitTransaction() throws Exception;
	public abstract void rollbackTransaction() throws Exception;
	public abstract Connection getConnection() throws SQLException;

}
