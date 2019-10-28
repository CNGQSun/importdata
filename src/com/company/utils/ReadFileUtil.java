package com.company.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class ReadFileUtil {
    /**
     * 解压zip文件
     * @param zipFilePath 需要解压缩的文件
     * @param unzipFilePath 解压后的文件存储路径
     * @return 返回的是解压后的所有文件的全路径，存储在arraylist中
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
                unzipFile.mkdirs();
                return true;
            }
        }else {
            System.out.println("您输入的待解压文件不正确！");
            //待解压文件不存在，返回3
            return false;
        }
    }


    /**
     *  复制单个文件
     *  @param  oldPath  String  原文件路径  如：c:/fqf.txt
     *  @param  newPath  String  复制后路径  如：f:/fqf.txt
     *  @return  boolean
     */
    public static   void  copyFile(String  oldPath,  String  newPath)  {
        try  {
//           int  bytesum  =  0;
            int  byteread  =  0;
            File  oldfile  =  new  File(oldPath);
            if  (oldfile.exists())  {  //文件存在时
                InputStream  inStream  =  new  FileInputStream(oldPath);  //读入原文件
                FileOutputStream  fs  =  new  FileOutputStream(newPath);
                byte[]  buffer  =  new  byte[1444];
//               int  length;
                while  (  (byteread  =  inStream.read(buffer))  !=  -1)  {
//                   bytesum  +=  byteread;  //字节数  文件大小
//                   System.out.println(bytesum);
                    fs.write(buffer,  0,  byteread);
                }
                inStream.close();
            }
        }
        catch  (Exception  e)  {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

}
