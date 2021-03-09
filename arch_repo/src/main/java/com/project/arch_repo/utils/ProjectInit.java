package com.project.arch_repo.utils;

import android.app.Application;
import android.util.Log;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.BuildConfig;
import com.project.arch_repo.base.module.AppModuleInitialization;
import com.project.arch_repo.common.ArouterAppInject;
import com.project.config_repo.ArouterConfig;
import com.xxf.arch.XXF;
import com.xxf.arch.core.Logger;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * FileName: ProjectInit
 * Author: liuboyu
 * Email: 545777678@qq.com
 * Date: 2019-10-30 17:11
 * Description: 项目初始化
 */
public class ProjectInit implements AppModuleInitialization, Logger,
        Consumer<Throwable>, Function<Throwable, String> {
    private static final String TAG = "=====TokenTm:";
    private static volatile ProjectInit INSTANCE;

    public static ProjectInit getInstance() {
        if (INSTANCE == null) {
            synchronized (ProjectInit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProjectInit();
                }
            }
        }
        return INSTANCE;
    }

    private boolean isINIT;

    private ProjectInit() {
        isINIT = false;
    }

    @Override
    public void init(Application context) {
        if (isINIT) {
            return;
        }

        d("TokenTm_start");
        WebView.setWebContentsDebuggingEnabled(true);

        /**
         * XXF初始化
         */
        XXF.init(context, this, this, this);


        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                d("========>error:" + Log.getStackTraceString(throwable));
            }
        });
        /**
         *  arouter
         */
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(context);
        //自动注入ARouter
        new ArouterAppInject(context);

        //初始化child module
        for (String initPath : ArouterConfig.MODULE_INIT_PATHS) {
            IProvider iProvider = (IProvider) ARouter.getInstance().build(initPath).navigation();
            d("TokenTmIN_init_:" + initPath + "___class:" + iProvider);
        }

        d("TokenTmIN_end");
        isINIT = true;
    }

    @Override
    public boolean isLoggable() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void d(String msg) {
        if (isLoggable()) {
            Log.d(TAG, msg);
        }
    }

    @Override
    public void d(String msg, Throwable tr) {
        if (isLoggable()) {
            Log.d(TAG, msg);
        }
    }

    @Override
    public void e(String msg) {
        if (isLoggable()) {
            Log.e(TAG, msg);
        }
    }

    @Override
    public void e(String msg, Throwable tr) {
        if (isLoggable()) {
            Log.e(TAG, msg, tr);
        }
    }

    @Override
    public void accept(Throwable throwable) throws Exception {
        ToastUtils.showToast(apply(throwable));
    }

    @Override
    public String apply(Throwable throwable) throws Exception {
        return ThrowableConvertUtils.convertThrowable2String(throwable);
    }

}
