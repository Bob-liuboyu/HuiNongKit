package com.project.module_order.source;

import com.project.common_resource.requestModel.MeasurePicResponse;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.MeasureResponse;
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
}