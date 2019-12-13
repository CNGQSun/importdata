package com.company;

import com.company.utils.ReadFileUtil;
import com.company.utils.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Test {

    public static final Properties p = new Properties();
    public static Boolean flag = true;
    private static Logger log=Logger.getLogger(ReadFileUtil.class);



    //E:/test1/basic_data.zip E:/test
    public static void main(String[] args) {
        if (args.length == 0 || args == null) {
            log.info("您没有输入配置文件的路径！");
            System.out.println("您没有输入配置文件的路径！");
        } else if (args.length == 1) {
            getParamDetails(args);
            if (flag) {
                String zipfilepath = null;
                String unzippath = null;
                try {
                    zipfilepath = p.getProperty("zipfilepath");
                    unzippath = p.getProperty("unzippath");
                } catch (Exception e) {
                    log.info("请检查配置文件的内容是否正确！");
                    System.out.println("请检查配置文件的内容是否正确！");
                    e.printStackTrace();
                }
                if (zipfilepath != null && unzippath != null) {
                    Instant start = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
                    log.info(start + "\t开始运行");
                    System.out.println(start + "\t开始运行");
                    log.info("待解压文件路径：" + zipfilepath + "\t解压后的路径：" + unzippath);
                    System.out.println("待解压文件路径：" + zipfilepath + "\t解压后的路径：" + unzippath);
                    //文件判断
                    if (ReadFileUtil.judgeFilePath(zipfilepath, unzippath)) {
                        SqlUtil.runInsert(zipfilepath, unzippath);
                    }

                    Instant end = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
                    long timeElapsed = Duration.between(start, end).toMillis(); // 单位为毫秒
                    log.info(end + "\t运行结束,共耗时：" + timeElapsed + "毫秒");
                    System.out.println(end + "\t运行结束,共耗时：" + timeElapsed + "毫秒");
                } else {
                    log.info("请检查配置文件的内容是否正确！");
                    System.out.println("请检查配置文件的内容是否正确！");
                }

            }
        } else {
            log.info("您输入的参数个数不正确！参数只需要config.properties的路径！");
            System.out.println("您输入的参数个数不正确！参数只需要config.properties的路径！");
        }
    }

    public static void getParamDetails(String[] args) {
        try {
            String path = Test.class.getClassLoader().getResource("config.properties").getPath();
            if (args != null && args.length != 0 && StringUtils.isNoneBlank(args[0])) {
                path = args[0];
            }
            File file = new File(path);
            String fileName = file.getName();
            if ((!fileName.equals("config.properties")) || (!file.isFile())) {
                flag = false;
                log.info("配置文件不正确！");
                System.out.println("配置文件不正确！");
                return;
            }
            InputStream resourceAsStream = new FileInputStream(new File(path));
            p.load(new InputStreamReader(resourceAsStream, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("参数启动异常");
            System.out.println("参数启动异常");
        }
    }
}
