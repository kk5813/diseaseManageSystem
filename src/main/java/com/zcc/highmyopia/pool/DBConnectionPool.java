package com.zcc.highmyopia.pool;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class DBConnectionPool implements ConnectionBean{

    public String propertiesName = "";      //配置文件的名称

    private List<Connection> freeConnections = new ArrayList<>();   //空闲连接池

    private String password = null; // 密码

    private String driver = null;

    private String url = null; // 连接字符串

    private String username = null; // 用户名

    private static int num = 0;// 空闲连接数

    private int useNum = 0;

    private static int numActive = 0;// 当前可用的连接数

    //有参构造
    public DBConnectionPool(String propertiesName) {
        this.propertiesName = propertiesName;
        try {
            InputStream is = DBConnectionPool.class.getResourceAsStream(propertiesName);
            Properties p = new Properties();
            p.load(is);
            this.driver = p.getProperty("driver");
            this.url = p.getProperty("url");
            this.username = p.getProperty("username");
            this.password = p.getProperty("password");
            for(int i = 0; i < normalConnect; i++) {
                Connection c = newConnection();
                if(c != null) {
                    freeConnections.add(c);
                    num++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Connection newConnection() {
        Connection con = null;
        try {
            if (username == null) {     // 用户,密码都为空
                return con;
            } else {
                Class.forName(driver);
                con = DriverManager.getConnection(url, username, password);
            }
            log.info("连接池创建一个新的连接");
        } catch (SQLException | ClassNotFoundException e) {
            log.warn("无法创建这个URL的连接" + url);
            return null;
        }
        return con;
    }

    @Override
    public synchronized Connection getConnection() {
        Connection con = null;
        if (!freeConnections.isEmpty()) { // 还有空闲的连接
            num--;
            con = (Connection) freeConnections.get(0);
            freeConnections.remove(0);
            try {
                if (con.isClosed()) {
                    log.info("从连接池删除一个无效连接");
                    con = getConnection();
                }
            } catch (SQLException e) {
                log.info("从连接池删除一个无效连接");
                con = getConnection();
            }
        } else if (maxConnect == 0 || useNum < maxConnect) { // 没有空闲连接且当前连接小于最大允许值,最大值为0则不限制
            con = newConnection();
        }
        if (con != null) { // 当前连接数加1
            useNum++;
        }
        numActive++;
        return con;
    }

    @Override
    public synchronized Connection getConnection(long timeout) {
        long startTime = new Date().getTime();
        Connection con;
        while ((con = getConnection()) == null) {
            try {
                wait(timeout); //线程等待
            } catch (InterruptedException e) {
            }
            if ((new Date().getTime() - startTime) >= timeout) {
                return null; // 如果超时,则返回
            }
        }
        return con;
    }

    @Override
    public synchronized void closeConnection(Connection con) {
        freeConnections.add(con);
        num++;
        useNum--;
        numActive--;
        notifyAll(); //解锁
    }

    /**
     * 关闭所有连接
     */
    @Override
    public synchronized void release() {
        try {
            //将当前连接赋值到 枚举中
            Iterator<Connection> iterator = freeConnections.iterator();
            //使用循环关闭所用连接
            while (iterator.hasNext()) {
                //如果此枚举对象至少还有一个可提供的元素，则返回此枚举的下一个元素
                Connection con = (Connection) iterator.next();
                try {
                    con.close();
                    num--;
                } catch (SQLException e) {
                    log.warn("无法关闭连接池中的连接");
                }
            }
            freeConnections = new ArrayList<>();
            numActive = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
