package com.project.user;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.SharedPreferencesUtils;
import com.project.arch_repo.utils.StringUtils;
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.response.LoginResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.user.databinding.UserActivityLoginBinding;
import com.project.user.source.impl.LoginRepositoryImpl;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.StatusBarUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @fileName: LoginActivity
 * @author: liuboyu
 * @date: 2021/3/9 5:05 PM
 * @description:
 */
@Route(path = ArouterConfig.User.LOGIN)
public class LoginActivity extends BaseActivity {

    UserActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = Color.parseColor("#22a8e1");
        StatusBarUtils.compatStatusBarForM(this, false, color);
        binding = UserActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String phone = SharedPreferencesUtils.getStringValue(getContext(), "phone", "");
        if (!TextUtils.isEmpty(phone)) {
            binding.etPhone.setText(phone);
        }
        setListener();
    }

    public void setListener() {
        if (binding == null) {
            return;
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        binding.tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrDialogUtils.createCommonDialog(LoginActivity.this, "????????????", "??????????????????????????????????????????", new CommonDialog.OnDialogClickListener() {
                    @Override
                    public void onClickConfirm(View view) {

                    }

                    @Override
                    public void onClickCancel(View view) {

                    }
                }).show();
            }
        });
    }

    private void login() {
        String phone = binding.etPhone.getText().toString();
        String pwd = binding.etPwd.getText().toString();
        if (TextUtils.isEmpty(phone) || !StringUtils.isMobilPhoneNumber(phone)) {
            ToastUtils.showToast("???????????????????????????");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showToast("???????????????");
            return;
        }

        LoginRepositoryImpl.getInstance()
                .login(phone, pwd)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<LoginResDTO>bindToLifecycle(this))
                .compose(XXF.<LoginResDTO>bindToErrorNotice())
                .compose(XXF.<LoginResDTO>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("?????????...")))
                .subscribe(new Consumer<LoginResDTO>() {
                    @Override
                    public void accept(LoginResDTO loginUserInfoDTO) throws Exception {
                        if (loginUserInfoDTO == null || loginUserInfoDTO.getSettings() == null || loginUserInfoDTO.getUserInfo() == null || TextUtils.isEmpty(loginUserInfoDTO.getToken())) {
                            return;
                        }
                        saveData(loginUserInfoDTO);
                        if (loginUserInfoDTO.getSettings().isNeedResetPwd()) {
                            ARouter.getInstance().build(ArouterConfig.User.RESET_PWD)
                                    .withString("phone", binding.etPhone.getText().toString())
                                    .navigation();
                        } else {
                            ARouter.getInstance().build(ArouterConfig.Main.MAIN)
                                    .navigation();
                        }
                        SharedPreferencesUtils.setStringValue(getContext(), "phone", binding.etPhone.getText().toString());
                        finish();
                    }
                });
    }

    /**
     * ?????????????????????
     *
     * @param loginUserInfoDTO
     */
    private void saveData(LoginResDTO loginUserInfoDTO) {
        GlobalDataManager.getInstance().updateInfo(loginUserInfoDTO);
        SharedPreferencesUtils.setBooleanValue(getActivity(), "login", true);
        Gson gson = new Gson();
        SharedPreferencesUtils.setStringValue(getApplicationContext(), "user_info", gson.toJson(loginUserInfoDTO.getUserInfo()));
        SharedPreferencesUtils.setStringValue(getApplicationContext(), "settings", gson.toJson(loginUserInfoDTO.getSettings()));
        SharedPreferencesUtils.setStringValue(getApplicationContext(), "token", loginUserInfoDTO.getToken());
    }
}
