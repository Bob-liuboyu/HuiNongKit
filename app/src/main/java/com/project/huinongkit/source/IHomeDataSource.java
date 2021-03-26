package com.project.huinongkit.source;

import com.project.common_resource.response.PolicyListResDTO;

import java.util.List;

import io.reactivex.Observable;

public interface IHomeDataSource {
    /**
     * 首页订单列表
     * @param name
     * @param phone
     * @param insureId
     * @param claimStatus
     * @param claimType
     * @param submitStartDate
     * @param submitEndDate
     * @return
     */
    Observable<List<PolicyListResDTO>> getPolicyOrderList(String name , String phone, String insureId, String claimStatus, String claimType, String submitStartDate, String submitEndDate);
}