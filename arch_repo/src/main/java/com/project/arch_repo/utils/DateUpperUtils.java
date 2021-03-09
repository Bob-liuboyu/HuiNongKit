package com.project.arch_repo.utils;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2019-08-16
 * @Description 大写日期工具类
 */
public class DateUpperUtils {

    public final static char[] upper = "零一二三四五六七八九十".toCharArray();

    /**
     * 根据小写数字格式的日期转换成大写格式的日期
     *
     * @param date
     * @return
     */
    public static String getUpperDate(String date) {
        //支持yyyy-MM-dd、yyyy/MM/dd、yyyyMMdd等格式
        if (date == null) return null;
        //非数字的都去掉
        date = date.replaceAll("\\D", "");
        if (date.length() != 8) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {//年
            sb.append(upper[Integer.parseInt(date.substring(i, i + 1))]);
        }
        sb.append("年");//拼接年
        int month = Integer.parseInt(date.substring(4, 6));
        if (month <= 10) {
            sb.append(upper[month]);
        } else {
            sb.append("十").append(upper[month % 10]);
        }
        sb.append("月");//拼接月

        int day = Integer.parseInt(date.substring(6));
        if (day <= 10) {
            sb.append(upper[day]);
        } else if (day < 20) {
            sb.append("十").append(upper[day % 10]);
        } else {
            sb.append(upper[day / 10]).append("十");
            int tmp = day % 10;
            if (tmp != 0) sb.append(upper[tmp]);
        }
        sb.append("日");//拼接日
        return sb.toString();
    }
}
