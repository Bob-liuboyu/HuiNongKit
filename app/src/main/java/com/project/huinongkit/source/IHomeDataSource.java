package com.project.huinongkit.source;

import com.project.common_resource.SelectFilterModel;
import com.project.common_resource.response.PolicyListResDTO;

import java.util.List;

import io.reactivex.Observable;

public interface IHomeDataSource {
    /**
     * 首页理赔单列表
     *
     * @param model
     * @return
     */
    Observable<PolicyListResDTO> getPolicyOrderList(int index, SelectFilterModel model);
}