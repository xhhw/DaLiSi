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
java���ݿ����ӳ�ʵ�֣�ת�أ�

����:
�����ݿ��ȡ��,���ݿ����ӳ��ǲ���ȱ�ٵ�,�������������������
�������ӵȴ�,����������ݴ洢���ܻ��в�С������.
ԭ��:
���ӳ��൱�����ӵļ���,�����ӳس�ʼ��ʱ��ʵ����һ�������Ŀ�������
�ȴ��û�ʹ��,�û�ʹ���������ٽ��䷵�����ӳ�,��������ȥ�����ʱ��
�������ӵĿ�����û�п������ӵ������,���ӳؽ��Զ����������ٷ����
�û�����.
ʵ��:
 */
/*
 * �ҵ����ݿ����ӳ�
 * 
 * **********ģ��˵��**************
 * 
 * getInstance()����POOLΨһʵ��,��һ�ε���ʱ��ִ�й��캯��
 * ���캯��Pool()��������װ��loadDrivers()����;���ӳش���createPool()���� loadDrivers()װ������
 * createPool()�����ӳ� getConnection()����һ������ʵ�� getConnection(long time)���ʱ������
 * freeConnection(Connection con)��con����ʵ�����ص����ӳ� getnum()���ؿ���������
 * getnumActive()���ص�ǰʹ�õ�������
 * 
 */
public class DBPool {
	private static DBPool instance = null; // ����Ψһʵ��
	private int maxConnect = 100;// ���������
	private int normalConnect = 10;// ����������
	private String password = "";// ����
	private String url = "";// ����URL
	private String user = "";// �û���
	private String driverName = "";// ������
	Driver driver = null;// ��������
	DBConnectionPool pool = null;// ���ӳ�ʵ������

	// �����캯��˽��,������������
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
			p.load(in);            // ��������
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

	// װ�غ�ע������JDBC��������
	private void loadDrivers(String dri) {
		String driverClassName = dri;
		try {
			driver = (Driver) Class.forName(driverClassName).newInstance();
			DriverManager.registerDriver(driver);
			System.out.println("�ɹ�ע��JDBC��������" + driverClassName);
		} catch (Exception e) {
			System.out.println("�޷�ע��JDBC��������:" + driverClassName + ",����:" + e);
		}
	}

	// �������ӳ�
	private void createPool() {
		pool = new DBConnectionPool(password, url, user, normalConnect,
				maxConnect);
		if (pool != null) {
			System.out.println("�������ӳسɹ�");
		} else {
			System.out.println("�������ӳ�ʧ��");
		}
	}

	// ����Ψһʵ��
	public static synchronized DBPool getInstance() {
		if (instance == null) {
			instance = new DBPool();
		}
		return instance;
	}

	// ���һ�����õ�����,���û���򴴽�һ������,��С�������������
	public Connection getConnection() {
		if (pool != null) {
			return pool.getConnection();
		}
		return null;
	}

	// ���һ������,��ʱ������
	public Connection getConnection(long time) {
		if (pool != null) {
			return pool.getConnection(time);
		}
		return null;
	}

	// �����Ӷ��󷵻ظ����ӳ�
	public void freeConnection(Connection con) {
		if (pool != null) {
			pool.freeConnection(con);
		}
	}

	// ���ص�ǰ����������
	public int getnum() {
		return pool.getnum();
	}

	// ���ص�ǰ������
	public int getnumActive() {
		return pool.getnumActive();
	}

	// �ر���������,��������ע��
	public synchronized void release() {
		// /�ر�����
		pool.release();
		// /��������
		try {
			DriverManager.deregisterDriver(driver);
			System.out.println("����JDBC�������� " + driver.getClass().getName());
		} catch (SQLException e) {
			System.out
			.println("�޷�����JDBC���������ע��:" + driver.getClass().getName());
		}
	}
}


