package com.project.module_order.api;

import com.google.gson.JsonObject;
import com.project.arch_repo.http.GlobalGsonConvertInterceptor;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.MeasureResponse;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.config_repo.ApiConfig;
import com.xxf.arch.annotation.BaseUrl;
import com.xxf.arch.annotation.GsonInterceptor;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * @fileName: PictureApiService
 * @author: liuboyu
 * @date: 2021/3/9 5:05 PM
 * @description:
 */
@BaseUrl(ApiConfig.PicBaseUrl)
@GsonInterceptor(GlobalGsonConvertInterceptor.class)
public interface PicMeasureApiService {
    /**
     * 照片测量
     * @param DepthImage
     * @param Image
     * @param scope_dir
     * @param baodan
     * @param id
     * @param longitude
     * @param latitude
     * @return
     */
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded;charset=utf-8")
    @POST("measure")
    Observable<MeasureResponse> measure(@Field("DepthImage") String DepthImage,
                                        @Field("Image") String Image,
                                        @Field("scope_dir") JsonObject scope_dir,
                                        @Field("baodan") String baodan,
                                        @Field("id") long id,
                                        @Field("longitude") String longitude,
                                        @Field("latitude") String latitude);
}
