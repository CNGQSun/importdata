package com.company;

import com.company.utils.ReadFileUtil;
import com.company.utils.SqlUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Test {
    //E:/test1/basic_data.zip E:/test
    public static void main(String[] args) {
        Instant start = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
        System.out.println( start+"\t开始运行");
        if (args.length == 2) {
            System.out.println("待解压文件路径：" + args[0] + "\t解压后的路径：" + args[1]);
            //文件判断
            if (ReadFileUtil.judgeFilePath(args[0], args[1])) {
                SqlUtil.runInsert(args[0], args[1]);
            }
        } else {
            System.out.println("您输入的参数个数不正确");
        }
        Instant end = Instant.now().plusMillis(TimeUnit.HOURS.toMillis(8));
        long timeElapsed = Duration.between(start, end).toMillis(); // 单位为毫秒
        System.out.println(end+"\t运行结束,共耗时：" + timeElapsed + "毫秒");
    }
}
