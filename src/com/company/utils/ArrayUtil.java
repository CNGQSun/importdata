package com.company.utils;

import java.util.ArrayList;

public class ArrayUtil {
    /**
     * 传入表头（字段名）处理字段名
     *
     * @param field 传入的数组为表头
     * @return 返回a, b, c...格式的String
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
    public static Boolean judgeArray(String[] targetHead, String[] template) {
        sx=new ArrayList();
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

    public static String content(String[] targetHead, String[] targe) {
        String[] strings1 = new String[targetHead.length];
        int index = targe.length;
        if (targe.length < targetHead.length) {
            int i = targetHead.length - targe.length;
            for (int k = 0; k < targe.length; k++) {
                strings1[k] = targe[k];
            }
            for (int j = 0; j < i; j++) {
                strings1[index] = "";
                index++;
            }
        }
        String s = "";
            for (Object o : sx) {
                   ;
                    s += "'";
                    s += strings1[(int) o];
                    s += "'";
                    s += ",";
            }
        s = s.substring(0, s.length() - 1);
        return s;
    }
}
