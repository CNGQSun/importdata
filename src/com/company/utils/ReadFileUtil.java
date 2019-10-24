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
 /*           if (unzipPathArrayList.size() > 0) {
                //遍历数组
                for (Object path : unzipPathArrayList) {
                    String unzipPath = (String) path;
                    //拼接解压后的文件路径
                    File file1 = new File(unzipPath);
                    //对文件进行判断
                    if (file1.isFile()) {
                        ArrayList arrayList = new ArrayList();
                        //逐行读取文件
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(unzipPath), "utf-8"));//构造一个BufferedReader类来读取文件
                            String s = null;
                            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                                //添加到arraylist集合中
                                arrayList.add(s);
                            }
                            br.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //将每一个读取后的文件添加到fileArrayList中
                        fileArrayList.add(arrayList);
                    } else {
                        System.out.println("找不到该文件!");
                        return null;
                    }
                }
            } else {
                System.out.println("压缩包里没有文件!");
                return null;
            }*/
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
        if (zipFile.isFile() && unzipFile.isDirectory()) {
            return true;
        }
        return false;
    }

    /**
     * 读取参照文件，非配置文件
     * @return
     */
    /*public static String readFile() {//路径
        String file = "s.properties";
        File file1 = new File(file);
        ArrayList arrayList = new ArrayList();
        if (file1.isFile()) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));//构造一个BufferedReader类来读取文件
                String s = null;
                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                    arrayList.add(s);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("找不到参照文件");
            return null;
        }
        return (String) arrayList.get(0);
    }*/

    /**
     * 读取指定目录下的zip文件
     *
     * @param pathfile 传入zip文件所在文件夹
     * @return
     */
   /* public static String getFilePath(String pathfile) {

        String filePath = null;
        if (pathfile != null) {
            //读取输入路径的文件
            File[] list = new File(pathfile).listFiles();

            if (list != null) {
                for (File file : list) {
                    if (file.isFile()) {
                        if (file.getName().endsWith(".zip")) {
                            // 就输出该文件的绝对路径
                            filePath = file.getAbsolutePath();
                        } else {
                            filePath = null;
                        }
                    }
                }
            } else {
                System.out.println("请输入正确的目录");
            }
        }
        return filePath;
    }*/
}
