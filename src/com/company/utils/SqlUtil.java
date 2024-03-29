package com.company.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SqlUtil {
    private static Logger log=Logger.getLogger(ReadFileUtil.class);
    public static Boolean doSql(String sql) {
        Boolean flag = null;
        Connection conn = null;
        Statement st = null;
        try {
            conn = JDBCDruidUtil.getConnection();
            st = conn.createStatement();
            int i = st.executeUpdate(sql);
            if (i > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            JDBCDruidUtil.close(null, st, conn);
        }
        return flag;
    }


    public static String successFileName = "";
    public static String failFileName = "";

    public static void runInsert(String pathFile, String unzipFilePath) {
        //解压缩文件
        ArrayList fileArrayList = ReadFileUtil.readZipFile(pathFile, unzipFilePath);
        if (fileArrayList != null && fileArrayList.size() > 0) {
            File resultMkdir = new File(unzipFilePath + "/" + DateUtil.getMkdirName());
            resultMkdir.mkdirs();
            File successMkdir = new File(resultMkdir.getAbsolutePath() + "/success");
            File failMkdir = new File(resultMkdir.getAbsolutePath() + "/fail");
            successMkdir.mkdirs();
            failMkdir.mkdirs();
            //获取example模板文件表头
            String header = ReadFileUtil.getProperties();
            //根据tab分割
            String[] template = header.split("\t");
            String insertTemplate = ArrayUtil.field(template);
            //本次程序运行导入的数据总条数
            int allDataCount = 0;
            //遍历解压后的文件，分别进行数据库导入操作
            for (Object path : fileArrayList) {
                Instant start = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
                String unzipPath = (String) path;
                File file = new File(unzipPath);
                String fileName = file.getName();
                System.out.println();
                log.info(start + "\t" + fileName + "正在导入中...");
                System.out.println(start + "\t" + fileName + "正在导入中...");
                BufferedReader br = null;
                Connection conn = null;
                Statement st = null;
                final int batchSize = 1000;
                int count = 0;
                try {
                    conn = JDBCDruidUtil.getConnection();
                    st = conn.createStatement();
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(unzipPath), "utf-8"));//构造一个BufferedReader类来读取文件
                    //先读取字段名即第一行
                    String field = br.readLine();//使用readLine方法，一次读一行
                    String[] targetHead = field.split("\t");
                    log.info("目标文件：" + field);
                    System.out.println("目标文件：" + field);
                    //如果目标文件与模板文件表头一致
                    if (ArrayUtil.judgeArray(targetHead, template)) {
                        //单个文件中导入的条数记录
                        int dataCount = 0;
                        //将数据拷贝到主表的sql语句
                        //MOP.STG_CB_BASIC_DATA
                        String sql1 = "insert into MOP.STG_CB_BASIC_DATA select * from MOP.STG_CB_BASIC_DATA_BAK";
                        //插入之前先清空临时表
                        //doSql("truncate table MOP.STG_CB_BASIC_DATA");
                        //MOP.STG_CB_BASIC_DATA_BAK
                        doSql("truncate table MOP.STG_CB_BASIC_DATA_BAK");

                        String s = null;
                        Boolean flag = true;
                        while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                            dataCount++;
                            //添加到arraylist集合中
                            String replace = s;
                            if (s.contains("\'")) {
                                replace = s.replace("\'", "\'\'");
                            }
                            String[] targe = replace.split("\t");
                            //获取字段值的个数，如果个数与字段个数不一致，需要在最后补""空值，次操作在数组
                            //System.out.println(ArrayUtil.content(s1, s2));
                            String sql = "insert into MOP.STG_CB_BASIC_DATA_BAK (" + insertTemplate + ") values (" + ArrayUtil.content(targetHead, targe) + ")";
                            st.addBatch(sql);
                            count++;
                            if (count % batchSize == 0) {
                                int[] executeBatch = st.executeBatch();
                                if (executeBatch.length <= 0) {
                                    flag = false;
                                }
                            }
                        }
                        if (dataCount % batchSize != 0) {
                            int[] executeBatch = st.executeBatch();
                            if (executeBatch.length <= 0) {
                                flag = false;
                            }
                        }
                        allDataCount += dataCount;
                        if (flag) {
                            //拷贝数据到主表
                            if (doSql(sql1)) {
                                successFileName += fileName + ",";
                                ReadFileUtil.copyFile(unzipPath, successMkdir.getAbsolutePath() + "/" + fileName);
                                Instant end = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
                                long timeElapsed = Duration.between(start, end).toMillis(); // 单位为毫秒
                                log.info(end + "\t" + fileName + "导入数据库成功,共导入" + dataCount + "条,耗时:" + timeElapsed + "毫秒");
                                System.out.println(end + "\t" + fileName + "导入数据库成功,共导入" + dataCount + "条,耗时:" + timeElapsed + "毫秒");
                            } else {
                                ReadFileUtil.copyFile(unzipPath, failMkdir.getAbsolutePath() + "/" + fileName);
                                failFileName += fileName + ",";
                                log.info("导入数据库失败！");
                                System.out.println("导入数据库失败！");
                            }
                        }
                    } else {
                        failFileName += fileName + ",";
                        ReadFileUtil.copyFile(unzipPath, failMkdir.getAbsolutePath() + "/" + fileName);
                        log.info(fileName + "文件格式不正确,导入数据错误！");
                        System.out.println(fileName + "文件格式不正确,导入数据错误！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    JDBCDruidUtil.close(null, st, conn);
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println();
            log.info("成功:" + successFileName + "\n" + "失败:" + failFileName + "\n" + "共导入：" + allDataCount + "条数据");
            System.out.println("成功:" + successFileName + "\n" + "失败:" + failFileName + "\n" + "共导入：" + allDataCount + "条数据");
        } else {
            log.info("压缩包里没有文件！");
            System.out.println("压缩包里没有文件！");
        }
    }
}