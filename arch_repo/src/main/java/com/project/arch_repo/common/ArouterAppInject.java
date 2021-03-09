package com.project.arch_repo.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xxf.arch.core.SimpleActivityLifecycleCallbacks;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class ArouterAppInject extends SimpleActivityLifecycleCallbacks {
    public ArouterAppInject(Application application) {
        super(application);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);
        ARouter.getInstance().inject(activity);
        if (activity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity.getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                            super.onFragmentPreCreated(fm, f, savedInstanceState);
                            ////自动注入ARouter
                            ARouter.getInstance().inject(f);
                        }
                    }, true);
        }
    }
}
