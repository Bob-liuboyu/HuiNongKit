package com.project.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseTitleBarActivity;
import com.project.config_repo.ArouterConfig;
import com.project.user.databinding.UserActivityResetPwdBinding;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.Action;


/**
 * @fileName: ResetPwdActivity
 * @author: liuboyu
 * @date: 2021/3/13 3:59 PM
 * @description: 重置密码
 */
@Route(path = ArouterConfig.User.RESET_PWD)
public class ResetPwdActivity extends BaseTitleBarActivity {

    private UserActivityResetPwdBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityResetPwdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        getTitleBar().setTitleBarTitle("慧农保", new Action() {
            @Override
            public void run() throws Exception {
                finish();
            }
        });
        //长度限制
        binding.etPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.etRePwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private boolean checkInputLegal() {
        if (TextUtils.isEmpty(binding.etPwd.getText()) || TextUtils.isEmpty(binding.etRePwd.getText())) {
            ToastUtils.showToast("请输入6-20位数字及字母!");
            return false;
        }
        if (!TextUtils.equals(binding.etRePwd.getText(), binding.etPwd.getText())) {
            ToastUtils.showToast("两次密码输入不一致!");
            return false;
        }
        if (!UserConfig.PATTERN_PWD.matcher(binding.etRePwd.getText()).matches()) {
            ToastUtils.showToast("请输入6-20位数字及字母!");
            return false;
        }
        return true;
    }

    @SuppressLint("CheckResult")
    private void submit() {
        if (!checkInputLegal()) {
            return;
        }
        String pwd = binding.etRePwd.getText().toString().trim();
        ToastUtils.showToast(pwd);
    }
}
