package com.project.module_order.source.impl;

import com.google.gson.JsonObject;
import com.project.arch_repo.function.ResponseDTOSimpleFunction;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.PolicyListResDTO;
import com.project.module_order.api.OrderApiService;
import com.project.module_order.source.IOrderDataSource;
import com.xxf.arch.XXF;

import java.util.List;

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

    @Override
    public Observable<List<InsureListResDTO>> getInsureList(String search) {
        String token = GlobalDataManager.getInstance().getToken();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("search", search);
        return XXF.getApiService(OrderApiService.class)
                .getInsureList(jsonObject)
                .map(new ResponseDTOSimpleFunction<List<InsureListResDTO>>());
    }
}
