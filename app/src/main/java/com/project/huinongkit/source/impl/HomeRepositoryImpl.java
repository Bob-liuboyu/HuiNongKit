package com.project.huinongkit.source.impl;

import com.google.gson.JsonObject;
import com.project.arch_repo.function.ResponseDTOSimpleFunction;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.PolicyListResDTO;
import com.project.huinongkit.api.HomeApiService;
import com.project.huinongkit.source.IHomeDataSource;
import com.project.user.api.UserApiService;
import com.project.user.source.ILoginDataSource;
import com.xxf.arch.XXF;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class HomeRepositoryImpl implements IHomeDataSource {
    private static IHomeDataSource INSTANCE;

    public static IHomeDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (HomeRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HomeRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    private HomeRepositoryImpl() {
    }


    @Override
    public Observable<List<PolicyListResDTO>> getPolicyOrderList(String name, String phone, String insureId, String claimStatus, String claimType, String submitStartDate, String submitEndDate) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("insureId", insureId);
        jsonObject.addProperty("claimStatus", claimStatus);
        jsonObject.addProperty("claimType", claimType);
        jsonObject.addProperty("submitStartDate", submitStartDate);
        jsonObject.addProperty("submitEndDate", submitEndDate);
        return XXF.getApiService(HomeApiService.class)
                .getPolicyOrderList(jsonObject)
                .map(new ResponseDTOSimpleFunction<List<PolicyListResDTO>>());
    }
}
