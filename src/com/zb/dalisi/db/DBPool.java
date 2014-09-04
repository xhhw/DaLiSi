package com.zb.dalisi.db;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import com.zb.dalisi.frame.XMLHelper;
import com.zb.dalisi.xml.cfg.Property;

/*
java数据库连接池实现（转载）

作用:
在数据库存取中,数据库连接池是不可缺少的,它可以提高连接利用率
减少连接等待,对于提高数据存储性能会有不小的作用.
原理:
连接池相当于连接的集合,在连接池初始化时先实例化一定数量的空闲连接
等待用户使用,用户使用完连接再将其返回连接池,这样就免去了最耗时的
创建连接的开销在没有空闲连接的情况下,连接池将自动生成连接再分配给
用户请求.
实例:
 */
/*
 * 我的数据库连接池
 * 
 * **********模块说明**************
 * 
 * getInstance()返回POOL唯一实例,第一次调用时将执行构造函数
 * 构造函数Pool()调用驱动装载loadDrivers()函数;连接池创建createPool()函数 loadDrivers()装载驱动
 * createPool()建连接池 getConnection()返回一个连接实例 getConnection(long time)添加时间限制
 * freeConnection(Connection con)将con连接实例返回到连接池 getnum()返回空闲连接数
 * getnumActive()返回当前使用的连接数
 * 
 */
public class DBPool {
	private static DBPool instance = null; // 定义唯一实例
	private int maxConnect = 100;// 最大连接数
	private int normalConnect = 10;// 保持连接数
	private String password = "";// 密码
	private String url = "";// 连接URL
	private String user = "";// 用户名
	private String driverName = "";// 驱动类
	Driver driver = null;// 驱动变量
	DBConnectionPool pool = null;// 连接池实例变量

	// 将构造函数私有,不允许外界访问
	private DBPool() {
//		initDbProperties();
		initDbPropertiesXML();
		loadDrivers(driverName);
		createPool();
	}

	private void initDbProperties() {
		try{
			ClassLoader classLoader = DBPool.class.getClassLoader();
			URL resUrl = classLoader.getResource("database.properties");
			InputStream in = resUrl.openStream();
			Properties p = new Properties();
			p.load(in);            // 读入属性
			in.close();
			this.driverName = p.getProperty("DB_DRIVENAME");
			this.url = p.getProperty("DB_URL");
			this.user = p.getProperty("DB_USER");
			this.password = p.getProperty("DB_PASSWORD");
			this.maxConnect = Integer.parseInt(p.getProperty("maxConnect"));
			this.normalConnect = Integer.parseInt(p.getProperty("normalConnect"));
		}catch(Exception e){
			System.out.println("Error of read database.properties");
		}
	}
	
	private void initDbPropertiesXML(){
		try {
			Property[] peops = XMLHelper.getInstance().getWebConfig().getDatasource().getPool().getProperty();
			HashMap map = new HashMap();
			for(int i=0;i<peops.length;i++){
				map.put(peops[i].getName(), peops[i].getValue());
			}
			this.driverName = (String) map.get("driver");
			this.url = (String) map.get("url");
			this.user = (String) map.get("username");
			this.password = (String) map.get("password");
			this.maxConnect = Integer.parseInt((String) map.get("maxActive"));
			this.normalConnect = Integer.parseInt((String) map.get("maxIdle"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 装载和注册所有JDBC驱动程序
	private void loadDrivers(String dri) {
		String driverClassName = dri;
		try {
			driver = (Driver) Class.forName(driverClassName).newInstance();
			DriverManager.registerDriver(driver);
			System.out.println("成功注册JDBC驱动程序" + driverClassName);
		} catch (Exception e) {
			System.out.println("无法注册JDBC驱动程序:" + driverClassName + ",错误:" + e);
		}
	}

	// 创建连接池
	private void createPool() {
		pool = new DBConnectionPool(password, url, user, normalConnect,
				maxConnect);
		if (pool != null) {
			System.out.println("创建连接池成功");
		} else {
			System.out.println("创建连接池失败");
		}
	}

	// 返回唯一实例
	public static synchronized DBPool getInstance() {
		if (instance == null) {
			instance = new DBPool();
		}
		return instance;
	}

	// 获得一个可用的连接,如果没有则创建一个连接,且小于最大连接限制
	public Connection getConnection() {
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}

	// 获得一个连接,有时间限制
	public Connection getConnection(long time) {
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	// 将连接对象返回给连接池
	public void freeConnection(Connection con) {
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	// 返回当前空闲连接数
	public int getnum() {
		return pool.getnum();
	}

	// 返回当前连接数
	public int getnumActive() {
		return pool.getnumActive();
	}

	// 关闭所有连接,撤销驱动注册
	public synchronized void release() {
		// /关闭连接
		pool.release();
		// /撤销驱动
		try {
			DriverManager.deregisterDriver(driver);
			System.out.println("撤销JDBC驱动程序 " + driver.getClass().getName());
		} catch (SQLException e) {
			System.out
			.println("无法撤销JDBC驱动程序的注册:" + driver.getClass().getName());
		}
	}
}


