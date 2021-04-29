package com.project.huinongkit.api;

import com.project.common_resource.requestModel.PgyerApkUpdateInfoModel;
import com.xxf.arch.annotation.BaseUrl;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by liuboyu on 2019/4/26.
 * 名片详情页 相关api
 */
@BaseUrl("https://www.pgyer.com/apiv2/")
public interface IPgyerlApiService {

    /**
     * 获取App详细信息
     * @param _api_key
     * @param appKey
     * @return
     */
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=utf-8")
    @POST("https://www.pgyer.com/apiv2/app/check")
    Observable<PgyerApkUpdateInfoModel> checkUpdate(@Field("_api_key") String _api_key, @Field("appKey") String appKey);

}
