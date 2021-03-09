package com.project.arch_repo.base.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.CallSuper;

import java.util.HashSet;
import java.util.Set;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description module初始化
 */
public abstract class ChildBaseModuleInitialization
        implements IChildModuleInitialization {
    private static Application application;

    public static Application getApplication() {
        return application;
    }

    /**
     * 子 modules
     */
    private static Set<IChildModuleInitialization> childModules = new HashSet<>();

    public static Set<IChildModuleInitialization> getChildModules() {
        return childModules;
    }

    @CallSuper
    @Override
    public void init(Context context) {
        childModules.add(this);
        application = (Application) context.getApplicationContext();
    }
}