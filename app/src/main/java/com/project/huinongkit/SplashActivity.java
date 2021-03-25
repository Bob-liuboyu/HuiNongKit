package com.project.huinongkit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.databinding.MainActivitySplashBinding;
import com.xxf.view.utils.StatusBarUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @ClassName: SplashActivity
 * @Description: 引导页面
 * @Author: liuboyu  E-mail:545777678@qq.com
 * @CreateDate: 2019-10-30 14:10
 */
public class SplashActivity extends BaseActivity {
    MainActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = 0xFFFFFFFF;
        StatusBarUtils.compatStatusBarForM(this, false, color);
        binding = MainActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    @SuppressLint("CheckResult")
    private void initView() {
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (aLong <= 0) {
                            goLogin();
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 去登陆
     */
    @SuppressLint("WrongConstant")
    private void goLogin() {
        ARouter.getInstance().build(ArouterConfig.User.LOGIN)
                .navigation();
        finish();
    }
}
