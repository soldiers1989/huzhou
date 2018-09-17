package com.huzhou.gjj.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Toast统一管理类
 */
public class DateUtils {
    //检查时间是否超过三年；
    public static boolean CheckDate(Date start, Date end) {
        if (end.getYear() - start.getYear() == 3 && end.getMonth() == start.getMonth() && start.getDay() == end.getDay())
            return false;
        if (end.getYear() - start.getYear() > 2) {
            if (end.getYear() - start.getYear() == 3) {
                if (end.getMonth() < start.getMonth()) {
                    return false;
                }
                if (end.getMonth() == start.getMonth()) {
                    return end.getDate() > start.getDate();
                }
                return true;
            }
            return true;
        }
        return false;
    }

    public static List<String> StringDuan(String start) {
        List<String> list = new ArrayList<String>();
        String[] str = start.split("-");

        DecimalFormat df = new DecimalFormat("######0.00");

        Double a1 = Double.parseDouble(str[0].replace(":", "."));
        Double b1 = Double.parseDouble(str[1].replace(":", "."));

        String string = null;
        String a3, b3;
        Double a2;
        for (Double j = a1; j < b1; j++) {
            a2 = j + 1;
            if (a2 <= b1) {
                a3 = df.format(j).replace(".", ":");
                b3 = df.format(a2).replace(".", ":");
                string = a3 + "-" + b3;
                list.add(string);
            }
        }
        return list;
    }


    public static String setFormatDate(String date) {
        if (date.length() != 8)
            return date;
        String year = date.substring(0, 3);
        String mouth = date.substring(4, 5);
        String day = date.substring(6, 7);
        return year + "/" + mouth + "/" + day;
    }

    public static String CurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Long now = System.currentTimeMillis();
        Date date = new Date(now);
        return formatter.format(date);

    }


    /**
     * 返回自1970年1月1日  至今的毫秒数
     */
    public static Date getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date();
        date.setTime(currentTime);
        return date;
    }

    /**
     * 时间格式转换
     * String ->Date
     */
    public static Date strToDate(String str) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            date = format.parse(str);
        } catch (Exception e) {
        }
        return date;

    }
}
