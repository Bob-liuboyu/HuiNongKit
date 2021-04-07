package com.project.huinongkit.source.impl;

import com.google.gson.JsonObject;
import com.project.arch_repo.function.ResponseDTOSimpleFunction;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.SelectFilterModel;
import com.project.common_resource.global.GlobalDataManager;
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

import static com.project.common_resource.global.ConstantData.PAGE_COUNT;


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
    public Observable<PolicyListResDTO> getPolicyOrderList(int index,SelectFilterModel model) {
        String token = GlobalDataManager.getInstance().getToken();
        JsonObject jsonObject = new JsonObject();

        JsonObject params = new JsonObject();
        params.addProperty("search", model.getSearch());
        params.addProperty("claimStatus", model.getClaimStatus());
        params.addProperty("claimType", model.getClaimType());
        params.addProperty("submitEndDate", model.getSubmitEndDate());
        params.addProperty("submitStartDate", model.getSubmitStartDate());
        params.addProperty("token", token);

        jsonObject.add("params",params);
        jsonObject.addProperty("pageNo",index);
        jsonObject.addProperty("pageRows",PAGE_COUNT);

        return XXF.getApiService(HomeApiService.class)
                .getPolicyOrderList(jsonObject)
                .map(new ResponseDTOSimpleFunction<PolicyListResDTO>());
    }
}
