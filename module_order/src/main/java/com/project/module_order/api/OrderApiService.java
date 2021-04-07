package com.project.module_order.api;

import com.google.gson.JsonObject;
import com.project.arch_repo.http.GlobalGsonConvertInterceptor;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.PolicyDetailResDTO;
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
public interface OrderApiService {

    /**
     * 创建订单
     *
     * @param body
     * @return
     */
    @POST("claim/submit")
    Observable<ResponseDTO> submit(@Body JsonObject body);

    /**
     * 获取保单列表
     *
     * @param body
     * @return
     */
    @POST("claim/getInsureList")
    Observable<ResponseDTO<InsureListResDTO>> getInsureList(@Body JsonObject body);

    /**
     * 获取理赔单详情
     *
     * @param body
     * @return
     */
    @POST("claim/claimDetail")
    Observable<ResponseDTO<PolicyDetailResDTO>> getClaimDetail(@Body JsonObject body);
}
