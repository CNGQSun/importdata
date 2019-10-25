package com.company.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class ReadFileUtil {
    /**
     *
     *
     * @param zipFilePath 文件名
     * @return ArrayList
     */
    /**
     * 解压zip文件，将文件集合存储在ArrayList中
     *
     * @param zipFilePath
     * @param unzipFilePath
     * @return
     */
    public static ArrayList readZipFile(String zipFilePath, String unzipFilePath) {//路径
        ArrayList unzipPathArrayList = null;
        Boolean unZip = false;
        try {
            //调用解压工具类
            unZip = UnZipUtil.unzip(zipFilePath, unzipFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (unZip) {
            unzipPathArrayList = UnZipUtil.fileNames;
        } else {
            System.out.println("文件解压缩失败!");
        }
        return unzipPathArrayList;
    }

    /**
     * 读取配置文件
     *
     * @return
     */
    public static String getProperties() {
        String filePathName = "s.properties";
        Properties props = new Properties();
        ClassLoader classLoader = ReadFileUtil.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePathName)) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filed = props.getProperty("filed");
        return filed;

    }

    /**
     * 判断输入的zip和解压后的文件是否正确，前者为文件，后者为文件夹
     *
     * @param pathfile 传入zip文件的路径
     * @return zip文件的绝对路径
     */
    public static Boolean judgeFilePath(String pathfile, String unzipFilePath) {
        //读取输入路径的文件
        File zipFile = new File(pathfile);
        File unzipFile = new File(unzipFilePath);
        if (zipFile.isFile()){
            if (unzipFile.exists()){
                if (unzipFile.isDirectory()){
                    //待解压文件和解压后路径都正确，返回0
                    return true;
                }else {
                    System.out.println("您输入的解压后路径不是文件夹");
                    //待解压文件正确，解压后的路径是文件，返回1
                    return false;
                }
            }else {
                //待解压文件正确，解压后路径不存在，返回2
                System.out.println("您输入的解压后路径不存在，已帮您新建该文件夹");
                unzipFile.mkdir();
                return true;
            }
        }else {
            System.out.println("您输入的待解压文件不正确！");
            //待解压文件不存在，返回3
            return false;
        }
    }
}
