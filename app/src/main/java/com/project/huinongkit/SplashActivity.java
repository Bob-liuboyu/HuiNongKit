package com.project.huinongkit;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.SharedPreferencesUtils;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.response.LoginResDTO;
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
        int color = Color.parseColor("#1d3077");
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
        boolean login = SharedPreferencesUtils.getBooleanValue(getActivity(), "login", false);
        Gson gson = new Gson();
        String user_info = SharedPreferencesUtils.getStringValue(getApplicationContext(), "user_info", "");
        String settings = SharedPreferencesUtils.getStringValue(getApplicationContext(), "settings", "");
        String token = SharedPreferencesUtils.getStringValue(getApplicationContext(), "token", "");

        LoginResDTO.UserInfoBean userinfoBean = gson.fromJson(user_info, LoginResDTO.UserInfoBean.class);
        LoginResDTO.SettingsBean settingsBean = gson.fromJson(settings, LoginResDTO.SettingsBean.class);
        if (login && userinfoBean != null && settingsBean != null && !TextUtils.isEmpty(token)) {
            ARouter.getInstance().build(ArouterConfig.Main.MAIN)
                    .navigation();
            GlobalDataManager.getInstance().updateSettings(settingsBean);
            GlobalDataManager.getInstance().updateUserInfo(userinfoBean);
            GlobalDataManager.getInstance().updateUserToken(token);
        } else {
            ARouter.getInstance().build(ArouterConfig.User.LOGIN)
                    .navigation();
        }
        finish();
    }
}
