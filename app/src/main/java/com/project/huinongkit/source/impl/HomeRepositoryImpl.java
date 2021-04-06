package com.project.huinongkit.source.impl;

import com.google.gson.JsonObject;
import com.project.arch_repo.function.ResponseDTOSimpleFunction;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.SelectFilterModel;
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
    public Observable<List<PolicyListResDTO>> getPolicyOrderList(SelectFilterModel model) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("search", model.getSearch());
        jsonObject.addProperty("claimStatus", model.getClaimStatus());
        jsonObject.addProperty("claimType", model.getClaimType());
        jsonObject.addProperty("submitEndDate", model.getSubmitEndDate());
        jsonObject.addProperty("submitStartDate", model.getSubmitStartDate());
        return XXF.getApiService(HomeApiService.class)
                .getPolicyOrderList(jsonObject)
                .map(new ResponseDTOSimpleFunction<List<PolicyListResDTO>>());
    }
}
