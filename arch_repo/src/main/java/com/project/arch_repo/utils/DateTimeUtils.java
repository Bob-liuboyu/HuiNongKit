package com.project.arch_repo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {
    private static final Locale DEFAULT_LOCALE;

    public DateTimeUtils() {
    }

    public static String formatCurTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String formatTime(long millis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public static String formatTime(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String formatDateSimple(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String formatTimeSimple(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("aa HH:mm");
        return sdf.format(date);
    }

    public static String getStampDate2Str(String unixDate) {
        SimpleDateFormat fm1 = new SimpleDateFormat("MM月dd日");
        long unixLong = 0L;
        String date = "";

        try {
            unixLong = Long.parseLong(unixDate) * 1000L;
            date = fm1.format(unixLong);
            date = remainToday(date, unixLong);
            if (date.startsWith("0")) {
                date = date.substring(1, date.length());
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return date;
    }

    public static String getStampDateStrDay(String unixDate) {
        SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
        long unixLong = 0L;
        String date = "";

        try {
            unixLong = Long.parseLong(unixDate) * 1000L;
            date = fm1.format(unixLong);
            date = remainToday(date, unixLong);
        } catch (Exception var6) {
        }

        return date;
    }

    public static String getStampDateStrSuffix(String unixDate) {
        SimpleDateFormat fm1 = new SimpleDateFormat("HH:mm");
        long unixLong = 0L;
        String date = "";

        try {
            unixLong = Long.parseLong(unixDate) * 1000L;
            date = fm1.format(unixLong);
        } catch (Exception var6) {
        }

        return date;
    }

    public static String makeMilliSecondsTimeString(long time) {
        return makeSecTimeString(time / 1000L);
    }

    public static String makeSecTimeString(long secs) {
        int min = (int)secs / 60;
        int sec = (int)secs % 60;
        if (min == 0 && sec == 0) {
            return "00:00";
        } else {
            String time1;
            if (sec < 10) {
                time1 = "0" + sec;
            } else {
                time1 = "" + sec;
            }

            String time2;
            if (min < 10) {
                time2 = "0" + min;
            } else {
                time2 = "" + min;
            }

            return time2 + ":" + time1;
        }
    }

    private static String remainToday(String date, long time) {
        Date gmt = null;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.getDefault());
        Calendar day = Calendar.getInstance();
        day.set(1, cal.get(1));
        day.set(2, cal.get(2));
        day.set(5, cal.get(5));
        day.set(11, cal.get(11));
        day.set(12, cal.get(12));
        day.set(13, cal.get(13));
        Date gmt8 = day.getTime();
        gmt = new Date(time);
        int y1 = gmt8.getYear();
        int y2 = gmt.getYear();
        if (Math.abs(y1 - y2) == 0) {
            int m1 = gmt8.getMonth();
            int m2 = gmt.getMonth();
            if (Math.abs(m1 - m2) == 0) {
                int d1 = gmt8.getDate();
                int d2 = gmt.getDate();
                int dayRemain = Math.abs(d1 - d2);
                if (dayRemain == 0) {
                    date = "今天";
                }
            }
        }

        return date;
    }

    public static String formatDate(String date) {
        Date gmt = null;

        Date gmt8;
        try {
            label133: {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.getDefault());
                Calendar day = Calendar.getInstance();
                day.set(1, cal.get(1));
                day.set(2, cal.get(2));
                day.set(5, cal.get(5));
                day.set(11, cal.get(11));
                day.set(12, cal.get(12));
                day.set(13, cal.get(13));
                gmt8 = day.getTime();
                long time = Long.parseLong(date) * 1000L;
                gmt = new Date(time);
                int y1 = gmt8.getYear();
                int y2 = gmt.getYear();
                if (Math.abs(y1 - y2) == 0) {
                    int m1 = gmt8.getMonth();
                    int m2 = gmt.getMonth();
                    if (Math.abs(m1 - m2) != 0) {
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                        String strs = format.format(gmt);
                        return strs;
                    }

                    int d1 = gmt8.getDate();
                    int d2 = gmt.getDate();
                    int h1 = gmt8.getHours();
                    int h2 = gmt.getHours();
                    int dayRemain = Math.abs(d1 - d2);
                    if (dayRemain == 0) {
                        int resultH = Math.abs(h2 - h1);
                        int min2;
                        int resultMin;
                        int min1;
                        if (resultH < 1) {
                            min1 = gmt8.getMinutes();
                            min2 = gmt.getMinutes();
                            resultMin = Math.abs(min2 - min1);
                            if (resultMin < 1) {
                                int s1 = gmt8.getSeconds();
                                int s2 = gmt.getSeconds();
                                int s = Math.abs(s1 - s2);
                                if (s <= 30) {
                                    return "刚刚";
                                }

                                return "1分钟前";
                            }

                            if (resultMin >= 1 && resultMin < 15) {
                                return resultMin + "分钟前";
                            }

                            if (resultMin >= 15 && resultMin < 30) {
                                return "一刻钟前";
                            }

                            return "半小时前";
                        }

                        if (resultH >= 1 && resultH <= 24) {
                            if (resultH == 1) {
                                min1 = gmt8.getMinutes();
                                min2 = gmt.getMinutes();
                                resultMin = Math.abs(min1 + 60 - min2);
                                if (resultMin >= 1 && resultMin < 15) {
                                    return resultMin + "分钟前";
                                }

                                if (resultMin >= 15 && resultMin < 30) {
                                    return "一刻钟前";
                                }

                                if (resultMin > 30 && resultMin < 60) {
                                    return "半小时前";
                                }

                                return "1小时前";
                            }

                            min1 = gmt8.getMinutes();
                            min2 = gmt.getMinutes();
                            if (min1 - min2 > 0) {
                                return resultH + "小时前";
                            }

                            return resultH - 1 + "小时前";
                        }
                        break label133;
                    }

                    switch(dayRemain) {
                    case 1:
                        return "1天前";
                    case 2:
                        return "2天前";
                    case 3:
                        return "3天前";
                    default:
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
                        String strs = format.format(gmt);
                        return strs;
                    }
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String strs = format.format(gmt);
                return strs;
            }
        } catch (Exception var23) {
            var23.printStackTrace();
            gmt = null;
            gmt8 = null;
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            String strs = format.format(new Date());
            return strs;
        }

        gmt = null;
        gmt8 = null;
        return null;
    }

    public static String getTimeZoneString() {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", DEFAULT_LOCALE)).format(new Date());
    }

    static {
        DEFAULT_LOCALE = Locale.CHINA;
    }
}