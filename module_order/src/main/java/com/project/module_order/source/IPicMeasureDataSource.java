package com.project.module_order.source;

import com.project.common_resource.requestModel.MeasurePicResponse;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.MeasureResponse;
import com.project.common_resource.response.MeasureResponse2;
import com.project.common_resource.response.PolicyDetailResDTO;

import io.reactivex.Observable;

public interface IPicMeasureDataSource {

    /**
     * 照片测量
     *
     * @param model
     * @return
     */
    Observable<MeasureResponse> measure(MeasurePicResponse model);

    /**
     * 照片测量
     * @param claimType
     * @param measureType
     * @param token
     * @param depthImage
     * @param longitude
     * @param latitude
     * @param insureId
     * @param imgBase64
     * @return
     */
    Observable<MeasureResponse2> measure2(String claimType,String measureType,String token,String depthImage,String longitude,String latitude,String insureId,String imgBase64);
}