package com.zb.dalisi.db;

//���ݳ��ļ�
import java.sql.*;
import java.util.*;
import java.util.Date;

public class DBConnectionPool {
    private int checkedOut;

    private Vector<Connection> freeConnections = new Vector<Connection>();

    private int maxConn;

    // private int normalConn;

    private String password;

    private String url;

    private String user;

    private static int num = 0;// ���е�������

    private static int numActive = 0;// ��ǰ��������

    public DBConnectionPool(String password, String url, String user,
            int normalConn, int maxConn) {
        this.password = password;
        this.url = url;
        this.user = user;
        this.maxConn = maxConn;
        // this.normalConn = normalConn;
        for (int i = 0; i < normalConn; i++) { // ��ʼnormalConn������
            Connection c = newConnection();
            if (c != null) {
                freeConnections.addElement(c);
                num++;
            }
        }
    }

    // �ͷŲ��õ����ӵ����ӳ�
    public synchronized void freeConnection(Connection con) {
        freeConnections.addElement(con);
        num++;
        checkedOut--;
        numActive--;
        notifyAll();
    }

    // ����һ��������
    private Connection newConnection() {
        Connection con = null;
        try {
            if (user == null) { // �û�,���붼Ϊ��
            	DriverManager.setLoginTimeout(2);
                con = DriverManager.getConnection(url);
            } else {
                con = DriverManager.getConnection(url, user, password);
            }
            System.out.println("���ӳش���һ���µ�����");
        } catch (SQLException e) {
            System.out.println("�޷��������URL������" + url);
            return null;
        }
        return con;
    }

    // ���ص�ǰ����������
    public int getnum() {
        return num;
    }

    // ���ص�ǰ������
    public int getnumActive() {
        return numActive;
    }

    // ��ȡһ����������
    public synchronized Connection getConnection() {
        Connection con = null;
        if (freeConnections.size() > 0) { // ���п��е�����
            num--;
            con = (Connection) freeConnections.firstElement();
            freeConnections.removeElementAt(0);
            try {
                if (con.isClosed()) {
                    System.out.println("�����ӳ�ɾ��һ����Ч����");
                    con = getConnection();
                }
            } catch (SQLException e) {
                System.out.println("�����ӳ�ɾ��һ����Ч����");
                con = getConnection();
            }
        } else if (maxConn == 0 || checkedOut < maxConn) { // û�п��������ҵ�ǰ����С���������ֵ,���ֵΪ0������
            con = newConnection();
        }
        if (con != null) { // ��ǰ��������1
            checkedOut++;
        }
        numActive++;
        return con;
    }

    // ��ȡһ������,�����ϵȴ�ʱ������,ʱ��Ϊ����
    public synchronized Connection getConnection(long timeout) {
        long startTime = new Date().getTime();
        Connection con;
        while ((con = getConnection()) == null) {
            try {
                wait(timeout);
            } catch (InterruptedException e) {
            }
            if ((new Date().getTime() - startTime) >= timeout) {
                return null; // ��ʱ����
            }
        }
        return con;
    }

    // �ر���������
    public synchronized void release() {
        Enumeration<Connection> allConnections = freeConnections.elements();
        while (allConnections.hasMoreElements()) {
            Connection con = (Connection) allConnections.nextElement();
            try {
                con.close();
                num--;
            } catch (SQLException e) {
                System.out.println("�޷��ر����ӳ��е�����");
            }
        }
        freeConnections.removeAllElements();
        numActive = 0;
    }
}

