package com.project.arch_repo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String formatUtc(long timeMillis) {
        if (timeMillis < 0) {
            return "Ignore time!";
        }
        //标准格式:yyy-MM-dd HH:mm,不要修改
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    public static String formatYMD(long timeMillis) {
        if (timeMillis < 0) {
            return "Ignore time!";
        }
        //标准格式:yyy-MM-dd HH:mm,不要修改
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }
}
