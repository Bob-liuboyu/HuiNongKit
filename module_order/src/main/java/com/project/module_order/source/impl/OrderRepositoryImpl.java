package com.project.module_order.source.impl;

import com.project.module_order.source.IOrderDataSource;

import io.reactivex.Observable;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 登陆仓库
 */
public class OrderRepositoryImpl implements IOrderDataSource {
    private static IOrderDataSource INSTANCE;

    public static IOrderDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (OrderRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OrderRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    private OrderRepositoryImpl() {
    }

    @Override
    public Observable<String> submit(String phone, String password, String token) {
        return null;
    }
}
