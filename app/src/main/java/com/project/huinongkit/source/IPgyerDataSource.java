package com.project.huinongkit.source;


import com.project.common_resource.requestModel.PgyerApkUpdateInfoModel;

import io.reactivex.Observable;

/**
 * Created by liuboyu on 2019/4/26.
 */
public interface IPgyerDataSource {

    /**
     * 获取App详细信息
     *
     * @param api_key
     * @param appKey
     * @return
     */
    Observable<PgyerApkUpdateInfoModel> checkUpdate(String api_key, String appKey);

}
