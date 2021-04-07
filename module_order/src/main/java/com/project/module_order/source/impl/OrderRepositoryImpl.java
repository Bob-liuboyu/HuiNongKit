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

import static com.project.common_resource.global.ConstantData.PAGE_COUNT;


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
    public Observable<InsureListResDTO> getInsureList(int index, String search) {
        String token = GlobalDataManager.getInstance().getToken();
        JsonObject jsonObject = new JsonObject();

        JsonObject params = new JsonObject();
        params.addProperty("token", token);
        params.addProperty("search", search);

        jsonObject.add("params",params);
        jsonObject.addProperty("pageNo",index);
        jsonObject.addProperty("pageRows",PAGE_COUNT);

        return XXF.getApiService(OrderApiService.class)
                .getInsureList(jsonObject)
                .map(new ResponseDTOSimpleFunction<InsureListResDTO>());
    }
}
