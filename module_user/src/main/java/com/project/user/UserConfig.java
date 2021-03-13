package com.project.user;

import java.util.regex.Pattern;

public interface UserConfig {
    /**
     * 密码最多20位
     */
    int MAX_LENTH_PWD = 20;

    /**
     * 密码少6位
     */
    int MINI_LENTH_PWD = 6;


    /**
     * 密码正则表达式
     */
    Pattern PATTERN_PWD = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
}
