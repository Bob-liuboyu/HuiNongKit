package com.project.user.api;

import com.google.gson.JsonObject;
import com.project.arch_repo.http.GlobalGsonConvertInterceptor;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.response.LoginResDTO;
import com.project.config_repo.ApiConfig;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;

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
public interface UserApiService {

    /**
     * 登陆
     *
     * @param body
     * @return
     */
    @POST("user/login")
    Observable<ResponseDTO<LoginResDTO>> login(@Body JsonObject body);

    /**
     * 修改密码
     *
     * @param body
     * @return
     */
    @POST("user/updatePwd")
    Observable<ResponseDTO> updatePwd(@Body JsonObject body);
}
