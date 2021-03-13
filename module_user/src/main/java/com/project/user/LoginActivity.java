package com.project.user;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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
        setListener();
    }

    public void setListener(){
        if(binding == null){
            return;
        }
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ArouterConfig.Main.MAIN)
                        .navigation();
//                finish();
            }
        });
        binding.btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ArouterConfig.User.RESET_PWD)
                        .navigation();
//                finish();
            }
        });
    }
}
