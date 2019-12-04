package com.company.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.company.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCDruidUtil {private static DataSource ds;

    static {
        Properties pp = Test.p;
        //InputStream in = JDBCDruidUtil.class.getResourceAsStream("/druid.properties");
        try {
            ds = DruidDataSourceFactory.createDataSource(pp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 定义得到数据源的/连接池的方法
    public static DataSource getDataSource(){
        return ds;
    }

    // 定义获取连接的方法
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
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
