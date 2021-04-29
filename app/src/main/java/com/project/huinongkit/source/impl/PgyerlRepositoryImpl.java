package com.project.huinongkit.source.impl;

import com.project.common_resource.requestModel.PgyerApkUpdateInfoModel;
import com.project.huinongkit.api.IPgyerlApiService;
import com.project.huinongkit.source.IPgyerDataSource;
import com.xxf.arch.XXF;

import io.reactivex.Observable;

public class PgyerlRepositoryImpl implements IPgyerDataSource {

    private static volatile PgyerlRepositoryImpl INSTANCE;

    public static IPgyerDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (PgyerlRepositoryImpl.class) {
                if (INSTANCE == null) {
                    return new PgyerlRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    private IPgyerlApiService mService;

    private PgyerlRepositoryImpl() {
        mService = XXF.getApiService(IPgyerlApiService.class);
    }


    @Override
    public Observable<PgyerApkUpdateInfoModel> checkUpdate(String _api_key, String appKey) {
        return mService.checkUpdate(_api_key, appKey);
    }

}
