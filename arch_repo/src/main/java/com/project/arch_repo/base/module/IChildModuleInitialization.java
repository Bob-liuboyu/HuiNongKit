package com.project.arch_repo.base.module;

import android.support.annotation.MainThread;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface IChildModuleInitialization extends IProvider {

    /**
     * 已经登陆
     */
    @MainThread
    void onLogined(String id) throws Exception;

    /**
     * 已经登出
     */
    @MainThread
    void onLogout(String id) throws Exception;
}
