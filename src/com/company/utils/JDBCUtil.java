package com.company.utils;


import java.sql.*;

public class JDBCUtil {
    //设置数据库名称
    private final static String URL = "jdbc:sqlserver://123.59.87.196:1433;DatabaseName=wechat_db";
    private static final String USER="user1";
    private static final String PASSWORD="help7575";

    private static Connection conn=null;
    //静态代码块（将加载驱动、连接数据库放入静态块中）
    static{
        try {
            //1.加载驱动程序
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //2.获得数据库的连接
            conn=(Connection) DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //对外提供一个方法来获取数据库连接
    public static Connection getConnection(){
        return conn;
    }

    //关闭资源的方法
    public static void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}