package com.company.utils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ArrayUtil {
    /**
     * 传入表头（字段名）处理字段名
     *
     * @param field 传入的数组为表头
     * @return 返回a, b, c...格式的String
     * 只执行一轮
     */

    public static ArrayList sx = new ArrayList();

    public static String field(String[] template) {
        String s = "";
        for (int i = 0; i < template.length; i++) {
            String replace = template[i].replace("\'", "\"");
            s += replace;
            s += ",";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    /**
     * 判断模板文件所需要的字段是否都存在于目标文件中，存在返回true
     *
     * @param targetHead 目标头
     * @param template   模板
     * @return boolean类型
     * 一个文件只执行一轮
     */
    public static Boolean judgeArray(String[] targetHead, String[] template) {
        sx = new ArrayList();
        ArrayList arrayList = new ArrayList();
        Boolean b = true;
        for (int i = 0; i < targetHead.length; i++) {
            arrayList.add(targetHead[i]);
        }
        for (int i = 0; i < template.length; i++) {
            if (!arrayList.contains((String) template[i])) {
                b = false;
                break;
            } else {
                sx.add((int) arrayList.indexOf((String) template[i]));
            }
        }
        return b;
    }

    /**
     * @param targetHead 目标文件的字段名
     * @param targe      目标文件的字段值
     * @return 返回"'a','b','c',..."格式的String
     * 一行数据执行一轮
     */
    public static String content(String[] targetHead, String[] targe) {
        String[] strings1 = new String[targetHead.length];
        String regxpForHtml = "<([^>]*)>";
        Pattern pattern = Pattern.compile(regxpForHtml);
        int index = targe.length;
        if (targe.length < targetHead.length) {
            int i = targetHead.length - targe.length;
            for (int k = 0; k < targe.length; k++) {
                if (k==2||k==3||k==4||k==39){
                    targe[k]=targe[k].replaceAll("<([^>]*)>","");
                }
                strings1[k] = targe[k];
            }
            for (int j = 0; j < i; j++) {
                strings1[index] = "";
                index++;
            }
        }
        String s = "";
        for (Object o : sx) {
            int i = (int) o;
            s += "'";
            s += strings1[i];
            s += "'";
            s += ",";
        }
        s = s.substring(0, s.length() - 1);
        //s=CleanUtil.cleanAll(s);
        s = StringEscapeUtils.unescapeHtml4(s);
        HashMap<String, String> laDingTable = LaDingUtil.LaDingTable;
        for (String key : laDingTable.keySet()) {
            String value=laDingTable.get(key);
            if (s.contains(key)){
                s=s.replace(key,value);
            }
        }
        return s;
    }

    public static void Test() {
        String[] a = {"a", "b", "c"};
        String[] b = new String[4];
        b[3] = "d";
        b = a;

        System.out.println(b.length);
    }

    public static void main(String[] args) {
        ArrayUtil.Test();
    }
}


/**
 * 传入字段值 处理字段值
 *
 * @param
 * @return 返回'a','b','c'格式的String
 */
   /* public static String content(String[] field, String[] strings) {
        String s = "";
        for (int i = 0; i < strings.length; i++) {
            String replace = strings[i].replace("\'", "\"");
            s += "'";
            s += replace;
//            s+=strings[i];
            s += "'";
            s += ",";
        }
        //判断字段的个数与字段值的个数是否一致，如果不一致，说明在字段值的最后面有tab，则需添加空值
        if (strings.length < field.length) {
            int i = field.length - strings.length;
            for (int j = 0; j < i; j++) {
                s += "\'\',";
            }
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }*/
