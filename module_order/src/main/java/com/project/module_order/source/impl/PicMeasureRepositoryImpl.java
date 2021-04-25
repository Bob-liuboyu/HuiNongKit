package com.project.module_order.source.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.project.arch_repo.function.ResponseDTOSimpleFunction;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.requestModel.CreatePolicyRequestModel;
import com.project.common_resource.requestModel.MeasurePicResponse;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.MeasureResponse;
import com.project.common_resource.response.MeasureResponse2;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.module_order.api.OrderApiService;
import com.project.module_order.api.PicMeasureApiService;
import com.project.module_order.source.IOrderDataSource;
import com.project.module_order.source.IPicMeasureDataSource;
import com.xxf.arch.XXF;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.project.common_resource.global.ConstantData.PAGE_COUNT;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 登陆仓库
 */
public class PicMeasureRepositoryImpl implements IPicMeasureDataSource {
    private static IPicMeasureDataSource INSTANCE;

    public static IPicMeasureDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (PicMeasureRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PicMeasureRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    private PicMeasureRepositoryImpl() {
    }

    @Override
    public Observable<MeasureResponse> measure(MeasurePicResponse model) {
        if (model == null) {
            return null;
        }
        return XXF.getApiService(PicMeasureApiService.class)
                .measure(model.getDepthImage(), model.getImage(), model.getScope_dir(), model.getBaodan(), model.getId(), model.getLongitude(), model.getLatitude());
    }

    @Override
    public Observable<MeasureResponse2> measure2(String claimType,
                                                 String measureType,
                                                 String token,
                                                 String depthImage,
                                                 String longitude,
                                                 String latitude,
                                                 String insureId,
                                                 String imgBase64) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("claimType",claimType);
        jsonObject.addProperty("measureType",measureType);
        jsonObject.addProperty("token",token);
        jsonObject.addProperty("depthImage",depthImage);
        jsonObject.addProperty("longitude",longitude);
        jsonObject.addProperty("latitude",latitude);
        jsonObject.addProperty("insureId",insureId);
        jsonObject.addProperty("imgBase64",imgBase64);

        return XXF.getApiService(PicMeasureApiService.class)
                .measure2(jsonObject);
    }
}
