package com.project.arch_repo.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.project.arch_repo.widget.TokenProgressHUDImpl;
import com.xxf.arch.fragment.XXFDialogFragment;
import com.xxf.arch.widget.progresshud.ProgressHUD;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.0.1
 * @Description
 * @date createTime：2019/4/11
 */
public class BaseDialogFragment extends XXFDialogFragment {

    private TokenProgressHUDImpl tokenProgressHUD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenProgressHUD = new TokenProgressHUDImpl(this.getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tokenProgressHUD != null) {
            tokenProgressHUD.detachedContext();
        }
    }

    @Override
    public ProgressHUD progressHUD() {
        return tokenProgressHUD;
    }
}
