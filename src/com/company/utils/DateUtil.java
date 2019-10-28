package com.company.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getMkdirName(){
        Date nowDate=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String mkdirName=df.format(nowDate);
        return mkdirName;
    }

}
