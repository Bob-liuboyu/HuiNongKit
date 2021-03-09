package com.project.user;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.config_repo.ArouterConfig;
import com.project.user.databinding.UserActivityLoginBinding;

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
        binding = UserActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
