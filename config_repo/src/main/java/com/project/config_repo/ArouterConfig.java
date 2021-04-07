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
            User.INIT,
            Order.INIT
    );


    /**
     * 壳子模块
     */
    public static class Main {
        public static final String INIT = "/main/init";
        public static final String MAIN = "/main/main";

    }

    /**
     * 用户模块
     */
    public static class User {
        public static final String INIT = "/user/init";
        public static final String LOGIN = "/user/login";
        public static final String RESET_PWD = "/user/reset_pwd";
    }

    /**
     * 订单模块
     */
    public static class Order {
        public static final String INIT = "/order/init";
        public static final String ORDER_DETAIL = "/order/detail";
        public static final String ORDER_BODY_3D = "/order/body_3d";
        public static final String ORDER_CREATE = "/order/create";
        public static final String ORDER_CHOOSE = "/order/choose";
        public static final String ORDER_PHOTO_PRE = "/order/photo_pre";
    }
}
