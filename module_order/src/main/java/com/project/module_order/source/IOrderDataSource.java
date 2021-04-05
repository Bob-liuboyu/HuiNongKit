package com.project.module_order.source;

import io.reactivex.Observable;

public interface IOrderDataSource {

    /**
     * 创建订单
     *
     * @param phone
     * @param password
     * @return
     */
    Observable<String> submit(String phone, String password, String token);
}