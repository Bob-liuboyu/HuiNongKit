package com.project.config_repo;

import java.util.Arrays;
import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ArouterConfig {

    /**
     * 初始化模块路径
     */
    public static final List<String> MODULE_INIT_PATHS = Arrays.asList(
            Main.INIT,
            User.INIT
    );


    /**
     * 壳子模块
     */
    public static class Main {
        public static final String INIT = "/main/init";
        public static final String RESULT = "/main/result";

    }

    /**
     * 用户模块
     */
    public static class User {
        public static final String INIT = "/user/init";
        public static final String LOGIN = "/user/login";
    }

    /**
     * 订单模块
     */
    public static class Order {
        public static final String INIT = "/order/init";
    }
}
