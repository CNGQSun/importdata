package com.company;

import com.company.utils.ReadFileUtil;
import com.company.utils.SqlUtil;

public class Test {

    public static void main(String[] args) {
        if (args.length == 1) {
            if (ReadFileUtil.judgeFilePath(args[0], "E:/test")) {
                SqlUtil.runInsert(args[0], "E:/test");
            } else {
                System.out.println("请检查您输入的文件路径是否正确");
            }
        } else if (args.length == 2) {
            //文件判断
            if (ReadFileUtil.judgeFilePath(args[0], args[1])) {
                SqlUtil.runInsert(args[0], args[1]);
            } else {
                System.out.println("请检查您输入的文件路径是否正确");
            }
        } else {
            System.out.println("您输入的参数个数不正确");
        }

        //E:/test1/test7.zip E:/test
    }
}
