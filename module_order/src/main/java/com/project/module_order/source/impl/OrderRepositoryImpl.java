package com.project.module_order.source.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.project.arch_repo.function.ResponseDTOSimpleFunction;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.requestModel.CreatePolicyRequestModel;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.common_resource.response.PolicyListResDTO;
import com.project.module_order.api.OrderApiService;
import com.project.module_order.source.IOrderDataSource;
import com.xxf.arch.XXF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import static com.project.common_resource.global.ConstantData.PAGE_COUNT;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 登陆仓库
 */
public class OrderRepositoryImpl implements IOrderDataSource {
    private static IOrderDataSource INSTANCE;

    public static IOrderDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (OrderRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OrderRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    private OrderRepositoryImpl() {
    }

    @Override
    public Observable<Boolean> submit(CreatePolicyRequestModel model) {
        JsonObject jsonObject = new Gson().toJsonTree(model).getAsJsonObject();
        return XXF.getApiService(OrderApiService.class)
                .submit(jsonObject)
                .map(new Function<ResponseDTO, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull ResponseDTO responseDTO) throws Exception {
                        return responseDTO.success;
                    }
                });
    }

    @Override
    public Observable<InsureListResDTO> getInsureList(int index, String search) {
        String token = GlobalDataManager.getInstance().getToken();
        JsonObject jsonObject = new JsonObject();

        JsonObject params = new JsonObject();
        params.addProperty("token", token);
        params.addProperty("search", search);

        jsonObject.add("params", params);
        jsonObject.addProperty("pageNo", index);
        jsonObject.addProperty("pageRows", PAGE_COUNT);

        return XXF.getApiService(OrderApiService.class)
                .getInsureList(jsonObject)
                .map(new ResponseDTOSimpleFunction<InsureListResDTO>());
    }

    @Override
    public Observable<PolicyDetailResDTO> getClaimDetail(String claimId) {
        String token = GlobalDataManager.getInstance().getToken();

        JsonObject params = new JsonObject();
        params.addProperty("token", token);
        params.addProperty("claimId", claimId);

        return XXF.getApiService(OrderApiService.class)
                .getClaimDetail(params)
                .map(new ResponseDTOSimpleFunction<PolicyDetailResDTO>());
    }
}
