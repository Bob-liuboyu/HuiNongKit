package com.project.arch_repo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuboyu on 2019/3/19.
 */
public class GrPatternUtils {

    /**
     * 验证邮箱是否合法
     *
     * @param string
     * @return
     */
    public static boolean isEmail(String string) {
        if (string == null) {
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }
}
