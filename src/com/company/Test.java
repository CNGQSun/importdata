package com.company;

import com.company.utils.ReadFileUtil;
import com.company.utils.SqlUtil;

public class Test {

    public static void main(String[] args) {
        if (args.length == 2) {
            //文件判断
            if (ReadFileUtil.judgeFilePath(args[0], args[1])==0) {
                SqlUtil.runInsert(args[0], args[1]);
            } else if (ReadFileUtil.judgeFilePath(args[0], args[1])==1){
                System.out.println("请检查您输入的解压后路径不是文件夹，已将文件解压至E:/test");
                SqlUtil.runInsert(args[0], "E:/test");
            }else if (ReadFileUtil.judgeFilePath(args[0], args[1])==2){
                System.out.println("请检查您输入的解压后路径不存在，已将文件解压至E:/test");
                SqlUtil.runInsert(args[0], "E:/test");
            }else {
                System.out.println("您输入的待解压文件不正确！");
            }
        } else {
            System.out.println("您输入的参数个数不正确");
        }
        //E:/test1/test7.zip E:/test
    }
}
