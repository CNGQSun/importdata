package com.company.utils;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SqlUtil {

    /**
     * 处理数据
     */
    public static void runInsert(String pathFile, String unzipFilePath) {
        //解压缩文件
        ArrayList fileArrayList = ReadFileUtil.readZipFile(pathFile, unzipFilePath);
        if (fileArrayList.size() > 0) {
            //获取example模板文件表头
            String header = ReadFileUtil.getProperties();
            //根据tab分割
            String[] template = header.split("\t");
            //遍历解压后的文件，分别进行数据库导入操作
            for (Object path : fileArrayList) {
                System.out.println();
                String unzipPath = (String) path;
                File file=new File(unzipPath);
                String fileName = file.getName();
                System.out.println(fileName + "正在导入中...");
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(unzipPath), "utf-8"));//构造一个BufferedReader类来读取文件
                    //先读取字段名即第一行
                    String field = br.readLine();//使用readLine方法，一次读一行
                    String[] targetHead = field.split("\t");
                    System.out.println("目标文件：" + field);
                    System.out.println("模板文件：" + header);
                    //如果目标文件与模板文件表头一致
                    if (ArrayUtil.judgeArray(targetHead, template)) {
                        int count = 0;
                        //将数据拷贝到主表的sql语句
                        String sql1 = "insert into CB_BASIC_DATA select * from CB_BASIC_DATA_BAK";
                        //插入之前先清空临时表
                        doSql("truncate table CB_BASIC_DATA_BAK");
                        String s = null;
                        Boolean flag = true;
                        while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                            count++;
                            //添加到arraylist集合中
                            String[] targe = s.split("\t");
                            //获取字段值的个数，如果个数与字段个数不一致，需要在最后补""空值，次操作在数组
                            //System.out.println(ArrayUtil.content(s1, s2));
                            String sql = "insert into CB_BASIC_DATA_BAK (" + ArrayUtil.field(template) + ") values (" + ArrayUtil.content(targetHead, targe) + ")";
                            //执行插入操作，存在插入异常即终止操作
                            if (!doSql(sql)) {
                                flag = false;
                                System.out.println("插入存在异常，已终止！");
                                return;
                            }
                        }
                        if (flag) {
                            //拷贝数据到主表
                            doSql(sql1);
                            System.out.println(fileName + "导入数据库成功,共导入" + count + "条！");
                        }
                    } else {
                        System.out.println(fileName + "文件格式不正确,导入数据错误！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("压缩包里没有文件！");
        }
    }


    /**
     * 执行数据库操作
     *
     * @param sql
     */
    public static Boolean doSql(String sql) {
        Boolean flag = null;
        Connection conn = null;
        Statement st = null;
        try {
            conn = JDBCC3P0Util.getConnection();
            st = conn.createStatement();
            int i = st.executeUpdate(sql);
            if (i > 0) {
                flag = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            JDBCC3P0Util.close(null, st, conn);
        }
        return flag;
    }
    /**
     * 处理数据
     */
    /*public static void runInsert(String file) {
        Connection conn = JDBCC3P0Util.getConnection();
        Statement st = null;
        //调用ReadFileUtils工具类，获取读取文件后的集合
        ArrayList arrayList = ReadFileUtil.readFile(file);
        if (arrayList != null) {
            //获取字段行，分割字符串
            String field = (String) arrayList.get(0);
            String[] s1 = field.split("\t");
            System.out.println("字段个数：" + s1.length);
            //移除第一行表投行
            arrayList.remove(0);
            //对剩余集合进行遍历
            try {
                //开启事务
                conn.setAutoCommit(false);
                for (Object o : arrayList) {
                    String s = (String) o;
                    //获取每一行的字段值，根据tab分割
                    String[] s2 = s.split("\t");
                    //获取字段值的个数，如果个数与字段个数不一致，需要在最后补""空值，次操作在数组
                    //工具类里实现
                    System.out.println("字段值个数：" + s2.length);
                    System.out.println(ArrayUtil.content(s1, s2));
                    //sql语句
                    String sql = "insert into CB_BASIC_DATA_BAK (" + ArrayUtil.field(s1) + ") values (" + ArrayUtil.content(s1, s2) + ")";
//                    String sql = "delete from CB_BASIC_DATA where CAT_NO='" + s2[0] + "'";
                    //执行插入操作
                    st = conn.createStatement();
                    st.executeUpdate(sql);
                }
                //提交事务
                conn.commit();
            } catch (SQLException e) {
                try {
                    //事务回滚
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }finally {
                JDBCC3P0Util.close(null, st, conn);
            }
        }

    }*/
}
