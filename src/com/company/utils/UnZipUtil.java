package com.company.utils;


import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip压缩/解压缩工具类
 * 实现对目标路径及其子路径下的所有文件及空目录的压缩
 * <p>Title: ZipUtil1</p>
 * <p>Description: </p>
 *
 * @author fuxinbao
 * @date 2018年5月23日下午3:50:01
 */
public class UnZipUtil {

    /**
     * 缓冲器大小
     */
    private static final int BUFFER = 512;
    //将解压后的文件存储在fileNames中，如有多个文件就以“,”分割进行拼接
    public static ArrayList<String> fileNames = new ArrayList<>();

    /**
     * 取的给定源目录下的所有文件及空的子目录
     * 递归实现
     *
     * @param srcFile
     * @return
     */
    private static List<File> getAllFiles(File srcFile) {
        List<File> fileList = new ArrayList<File>();
        File[] tmp = srcFile.listFiles();

        for (int i = 0; i < tmp.length; i++) {

            if (tmp[i].isFile()) {
                fileList.add(tmp[i]);
                System.out.println("add file: " + tmp[i].getName());
            }

            if (tmp[i].isDirectory()) {
                if (tmp[i].listFiles().length != 0) {//若不是空目录，则递归添加其下的目录和文件
                    fileList.addAll(getAllFiles(tmp[i]));
                } else {//若是空目录，则添加这个目录到fileList
                    fileList.add(tmp[i]);
                    System.out.println("add empty dir: " + tmp[i].getName());
                }
            }
        }    // end for

        return fileList;
    }

    /**
     * 取相对路径
     * 依据文件名和压缩源路径得到文件在压缩源路径下的相对路径
     *
     * @param dirPath 压缩源路径
     * @param file
     * @return 相对路径
     */
    private static String getRelativePath(String dirPath, File file) {
        File dir = new File(dirPath);
        String relativePath = file.getName();

        while (true) {
            file = file.getParentFile();

            if (file == null) {
                break;
            }

            if (file.equals(dir)) {
                break;
            } else {
                relativePath = file.getName() + "/" + relativePath;
            }
        }    // end while

        return relativePath;
    }

    /**
     * 创建文件
     * 根据压缩包内文件名和解压缩目的路径，创建解压缩目标文件，
     * 生成中间目录
     *
     * @param dstPath  解压缩目的路径
     * @param fileName 压缩包内文件名
     * @return 解压缩目标文件
     * @throws IOException
     */
    private static File createFile(String dstPath, String fileName) throws IOException {
        String[] dirs = fileName.split("/");//将文件名的各级目录分解
        File file = new File(dstPath);
        if (dirs.length > 1) {//文件有上级目录
            for (int i = 0; i < dirs.length - 1; i++) {
                file = new File(file, dirs[i]);//依次创建文件对象知道文件的上一级目录
            }
            if (!file.exists()) {
                file.mkdirs();//文件对应目录若不存在，则创建
                System.out.println("mkdirs: " + file.getCanonicalPath());
            }
            file = new File(file, dirs[dirs.length - 1]);//创建文件
            return file;
        } else {
            if (!file.exists()) {
                file.mkdirs();//若目标路径的目录不存在，则创建
                System.out.println("mkdirs: " + file.getCanonicalPath());
            }
            file = new File(file, dirs[0]);//创建文件
            return file;
        }
    }

    /**
     * 解压缩方法
     * @param zipFileName 压缩文件名
     * @param dstPath     解压目标路径
     * @return
     */
    public static boolean unzip(String zipFileName, String dstPath) {
        Instant start = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
        System.out.println(start+"\t文件正在解压缩...");
        int countFileNum=0;
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileName));
            ZipEntry zipEntry = null;
            byte[] buffer = new byte[BUFFER];//缓冲器
            int readLength = 0;//每次读出来的长度
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    File dir = new File(dstPath + "/" + zipEntry.getName());
                        dir.mkdirs();
                        //System.out.println("mkdirs: " + dir.getCanonicalPath());
                        continue;
                } else {
                    File file = createFile(dstPath, zipEntry.getName());
                    OutputStream outputStream = new FileOutputStream(file);
                    while ((readLength = zipInputStream.read(buffer, 0, BUFFER)) != -1) {
                        outputStream.write(buffer, 0, readLength);
                    }
                    outputStream.close();
                    System.out.println("解压后的路径: " + file.getCanonicalPath());
                    countFileNum++;
                    fileNames.add(file.getCanonicalPath());
                }
            }    // end while
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        Instant end = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
        long timeElapsed = Duration.between(start, end).toMillis(); // 单位为毫秒
        System.out.println(end+"\t文件解压缩成功,共"+countFileNum+"个文件,耗时"+timeElapsed + "毫秒");
        return true;
    }

    /**
     * 压缩方法
     * （可以压缩空的子目录）
     *
     * @param srcPath     压缩源路径
     * @param zipFileName 目标压缩文件
     * @return
     */
    public static boolean zip(String srcPath, String zipFileName) {
        System.out.println("zip compressing...");
        File srcFile = new File(srcPath);
        List<File> fileList = getAllFiles(srcFile);//所有要压缩的文件
        byte[] buffer = new byte[BUFFER];//缓冲器
        ZipEntry zipEntry = null;
        int readLength = 0;//每次读出来的长度
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            for (File file : fileList) {
                if (file.isFile()) {//若是文件，则压缩这个文件
                    zipEntry = new ZipEntry(getRelativePath(srcPath, file));
                    zipEntry.setSize(file.length());
                    zipEntry.setTime(file.lastModified());
                    zipOutputStream.putNextEntry(zipEntry);
                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                    while ((readLength = inputStream.read(buffer, 0, BUFFER)) != -1) {
                        zipOutputStream.write(buffer, 0, readLength);
                    }
                    inputStream.close();
                    System.out.println("file compressed: " + file.getCanonicalPath());
                } else {//若是目录（即空目录）则将这个目录写入zip条目
                    zipEntry = new ZipEntry(getRelativePath(srcPath, file) + "/");
                    zipOutputStream.putNextEntry(zipEntry);
                    System.out.println("dir compressed: " + file.getCanonicalPath() + "/");
                }
            }    // end for
            zipOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("zip fail!");
            return false;
        } catch (IOException e) {
        }
        System.out.println("zip success!");
        return true;
    }
}
