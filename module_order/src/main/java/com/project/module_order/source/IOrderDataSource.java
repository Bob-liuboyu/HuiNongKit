package com.project.module_order.source;

import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.requestModel.CreatePolicyRequestModel;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.PolicyDetailResDTO;

import java.util.List;

import io.reactivex.Observable;

public interface IOrderDataSource {

    /**
     * 创建订单
     *
     * @param model
     * @return
     */
    Observable<Boolean> submit(CreatePolicyRequestModel model);

    /**
     * 获取保单列表
     *
     * @param search
     * @return
     */
    Observable<InsureListResDTO> getInsureList(int index, String search);

    /**
     * 获取理赔单详情
     *
     * @param claimId
     * @return
     */
    Observable<PolicyDetailResDTO> getClaimDetail(String claimId);
}