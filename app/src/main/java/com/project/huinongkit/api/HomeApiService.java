package com.project.huinongkit.api;

import com.google.gson.JsonObject;
import com.project.arch_repo.http.GlobalGsonConvertInterceptor;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.PolicyListResDTO;
import com.project.config_repo.ApiConfig;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @fileName: LoginActivity
 * @author: liuboyu
 * @date: 2021/3/9 5:05 PM
 * @description:
 */
@BaseUrl(ApiConfig.BaseUrl)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
public interface HomeApiService {

    /**
     * 首页订单列表
     *
     * @param body
     * @return
     */
    @POST("claim/list")
    Observable<ResponseDTO<List<PolicyListResDTO>>> getPolicyOrderList(@Body JsonObject body);
}
