package com.project.arch_repo.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuboyu on 2019/1/11.
 */

public class VertifyInputDataUtils {

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串中是否包含英文
     *
     * @param str 待校验字符串
     * @return 是否包含英文
     */
    public static boolean isContainEnglish(String str) {
        Pattern p = Pattern.compile("[a-zA-z]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 是否为合法的身份证号码
     *
     * @param input
     * @return
     */
    public static final boolean isIdCard(String input) {
        if (input == null || input.length() != 18) {
            return false;
        }
        // 校验位
        final int[] PARITY_BIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        final int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] cs = input.toUpperCase().toCharArray();

        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            if (i < cs.length - 1) {
                if (cs[i] < '0' || cs[i] > '9') {
                    return false;
                }
                power += (cs[i] - '0') * POWER_LIST[i];
            }
        }
        if (cs[cs.length - 1] != PARITY_BIT[power % 11]) {
            return false;
        }

        // 地区编码
        final Map<String, String> ZONE_NUM = new HashMap<String, String>();
        ZONE_NUM.put("11", "北京");
        ZONE_NUM.put("12", "天津");
        ZONE_NUM.put("13", "河北");
        ZONE_NUM.put("14", "山西");
        ZONE_NUM.put("15", "内蒙古");
        ZONE_NUM.put("21", "辽宁");
        ZONE_NUM.put("22", "吉林");
        ZONE_NUM.put("23", "黑龙江");
        ZONE_NUM.put("31", "上海");
        ZONE_NUM.put("32", "江苏");
        ZONE_NUM.put("33", "浙江");
        ZONE_NUM.put("34", "安徽");
        ZONE_NUM.put("35", "福建");
        ZONE_NUM.put("36", "江西");
        ZONE_NUM.put("37", "山东");
        ZONE_NUM.put("41", "河南");
        ZONE_NUM.put("42", "湖北");
        ZONE_NUM.put("43", "湖南");
        ZONE_NUM.put("44", "广东");
        ZONE_NUM.put("45", "广西");
        ZONE_NUM.put("46", "海南");
        ZONE_NUM.put("50", "重庆");
        ZONE_NUM.put("51", "四川");
        ZONE_NUM.put("52", "贵州");
        ZONE_NUM.put("53", "云南");
        ZONE_NUM.put("54", "西藏");
        ZONE_NUM.put("61", "陕西");
        ZONE_NUM.put("62", "甘肃");
        ZONE_NUM.put("63", "青海");
        ZONE_NUM.put("64", "宁夏");
        ZONE_NUM.put("65", "新疆");
        ZONE_NUM.put("71", "台湾");
        ZONE_NUM.put("81", "香港");
        ZONE_NUM.put("82", "澳门");
        ZONE_NUM.put("91", "国外");
        String addressCode = input.substring(0, 2);
        if (!ZONE_NUM.containsKey(addressCode)) {
            return false;
        }

        // 出生日期
        String birthday = input.substring(6, 14);
        if (!isBirthday(birthday)) {
            return false;
        }
        return true;
    }

    /**
     * 是否为合法的出生年月日
     *
     * @param input
     * @return
     */
    public static boolean isBirthday(String input) {
        if (input == null || input.length() != 8) {
            return false;
        }

        // 校验年份
        String year = input.substring(0, 4);
        int yearNum = 0;
        try {
            yearNum = Integer.parseInt(year);
            if (yearNum < 1900 || yearNum > 2017 /*Calendar.getInstance().get(Calendar.YEAR)*/) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // 校验月份
        String month = input.substring(4, 6);
        int monthNum = 0;
        try {
            monthNum = Integer.parseInt(month);
            if (monthNum < 1 || monthNum > 12) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // 校验日期
        String day = input.substring(6, 8);
        try {
            int dayNum = Integer.parseInt(day);
            // 1、3、5、7、8、10、12，31天永不差，其它月份30天(2月闰年29天，平年28天)
            if (monthNum == 1 || monthNum == 3 || monthNum == 5 || monthNum == 7
                    || monthNum == 8 || monthNum == 10 || monthNum == 12) {
                if (dayNum < 1 || dayNum > 31) {
                    return false;
                }
            } else {
                if (monthNum == 2) {
                    if (isLeapYear(yearNum)) {
                        if (dayNum < 1 || dayNum > 29) {
                            return false;
                        }
                    } else {
                        if (dayNum < 1 || dayNum > 28) {
                            return false;
                        }
                    }
                } else {
                    if (dayNum < 1 || dayNum > 30) {
                        return false;
                    }
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 是否为闰年
     * <p>
     * 1、年份能被4整除；
     * 2、年份若是100的整数倍的话,需被400整除,否则是平年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

     /*
    校验过程：
    1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
    2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
    3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
    */
    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if(bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard){
        if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
}
