package com.project.user;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.module.ChildBaseModuleInitialization;
import com.project.config_repo.ArouterConfig;

@Route(path = ArouterConfig.User.INIT)
public class INITIALIZATION extends ChildBaseModuleInitialization {
    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    public void onLogined(String id) throws Exception {
    }

    @Override
    public void onLogout(String id) throws Exception {
    }


}
