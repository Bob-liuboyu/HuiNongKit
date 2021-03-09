package com.project.arch_repo.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fanjb
 * @name 文本与字符处理工具类
 * @date 2014-5-10
 */
public class StringUtils {

    /**
     * 空值判断
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        str = str.trim();
        return str.length() == 0 || str.equalsIgnoreCase("null");
    }

    /**
     * 获取字符串的长度，中文占一个字符,英文数字占半个字符
     *
     * @param value
     * @return
     */
    public static double getChineseLength(String value) {
        if (isEmpty(value))
            return 0;
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 1;
            } else {
                valueLength += 0.5;
            }
        }
        return Math.ceil(valueLength);
    }

    /**
     * 获得最大长度的汉字字串
     *
     * @param source
     * @param maxLength
     * @return
     */
    public static String getChineseStringByMaxLength(String source, int maxLength) {
        if (source == null) {
            return null;
        }

        String cutString = "";
        double tempLength = 0;
        for (int i = 0; i < source.length(); i++) {
            String temp = source.substring(i, i + 1);
            String chinese = "[\u4e00-\u9fa5]";
            if (temp.matches(chinese)) {
                tempLength += 1;
            } else {
                tempLength += 0.5;
            }
            cutString += temp;
            if (Math.ceil(tempLength) == maxLength) {
                break;
            }
        }
        return cutString;
    }

    /**
     * 去掉文件名称中的非法字符
     *
     * @param str
     * @return
     */
    public static String escapeFileName(String str) {
        if (str == null) {
            return null;
        }
        /** 非法字符包括：/\:*?"<>| */
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '/' || c == '\\' || c == ':' || c == '*' || c == '?' || c == '"' || c == '<'
                    || c == '>' || c == '|') {
                continue;
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * 从url获取当前图片的id，如果url以ignoreTag开头则直接返回该url
     *
     * @param ignoreTag
     * @param url
     * @return
     */
    public static String getIdFromUrl(String url, String ignoreTag) {
        if (isEmpty(url) || (!TextUtils.isEmpty(ignoreTag)) && url.startsWith(ignoreTag)) {
            return url;
        }
        int lastIndex = url.lastIndexOf(".jpg");
        if (lastIndex < 0) {
            lastIndex = url.length() - 1;
        }
        int beginIndex = url.lastIndexOf("/") + 1;
        int slashIndex = url.lastIndexOf("%2F") + 3;
        int finalSlashIndex = url.lastIndexOf("%252F") + 5;
        beginIndex = Math.max(Math.max(beginIndex, slashIndex), finalSlashIndex);
        return url.substring(beginIndex, lastIndex);
    }

    public static String getIdFromUrl(String url) {
        return getIdFromUrl(url, null);
    }

    /**
     * 检测合法的电子邮件格式
     * <p>
     * [A-z0-9]{2,}[@][A-z0-9]{2,} [.]任意字母和数字组合，并长度大于等于2，必选
     * <p>
     * \p{Lower}{2,} 任意小写字母，并长度大于等于2，必选
     *
     * @param emailString
     * @return
     */
    public static boolean isEmail(String emailString) {
        String format = "^[0-9a-z]+\\w*@([0-9a-z]+\\.)+[0-9a-z]+$";
        if (emailString.matches(format)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测合法的电话号码(手机、固话、分机)
     * <p>
     * 匹配格式： 11位手机号码，3-4位区号，7-8位直播号码，1－4位分机号 如：12345678901、1234-12345678-1234
     *
     * @param phoneString
     * @return
     */
    public static boolean isPhoneNumber(String phoneString) {
        String format = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
        if (phoneString.matches(format)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 合法的手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isMobilPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * 5-12位的数字
     *
     * @param qqString
     * @return
     */
    public static boolean isQQNumber(String qqString) {
        String format = "^\\d{5,12}$";
        if (qqString.matches(format)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测合法密码
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        String format = "[A-z0-9]{6,15}";
        if (password.matches(format)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 合法的邮政编码
     *
     * @param zipCode
     * @return
     */
    public static boolean isZipCode(String zipCode) {
        String regularExpression = "^[1-9][0-9]{5}$";
        return Pattern.compile(regularExpression).matcher(zipCode).matches();
    }

    /**
     * 检测年龄的合法性
     *
     * @param age
     * @return
     */
    public static boolean isAge(int age) {
        if (age >= 0 && age <= 100) {
            return true;
        }
        return false;
    }

    /**
     * 替换
     *
     * @param text
     * @param searchString
     * @param replacement
     * @return
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * 校验两个串是否相等
     *
     * @param pathA
     * @param pathB
     * @return
     */
    public static final boolean isPathChange(String pathA, String pathB) {
        boolean isPathChange = false;
        if (StringUtils.isEmpty(pathA)) {
            isPathChange = !StringUtils.isEmpty(pathB);
        } else {
            isPathChange = !pathA.equals(pathB);
        }
        return isPathChange;
    }

    /**
     * 是否为字符
     *
     * @param c
     * @return
     */
    public static boolean isABC(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            return true;
        }
        return false;
    }

    /**
     * 将首字母转换为合法的排序字母
     *
     * @param c 输入字符
     * @return 大写字母或"|"
     */
    public static char formatFirstLetter(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c;
        }
        if (c >= 'a' && c <= 'z') {
            return (char) (c + ('A' - 'a'));
        }
        return '|';
    }

    /**
     * 提取串中连续的数字
     *
     * @param content
     * @return
     */
    public static String parseNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
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

    /**
     * 校验银行卡卡号
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static void main(String[] args) {
        System.out.println(isEmail("123@staff.token.tm"));
        System.out.println(isIdCard("510105199010017999"));
        System.out.println(isIdCard("35012219830922891X"));
        System.out.println(isIdCard("320113197303282935"));
        System.out.println(isIdCard("640324199009164417"));
        System.out.println(isIdCard("390324183389764497"));
    }
}
