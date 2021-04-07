package com.project.module_order.source;

import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.PolicyDetailResDTO;

import java.util.List;

import io.reactivex.Observable;

public interface IOrderDataSource {

    /**
     * 创建订单
     *
     * @param phone
     * @param password
     * @return
     */
    Observable<String> submit(String phone, String password, String token);

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