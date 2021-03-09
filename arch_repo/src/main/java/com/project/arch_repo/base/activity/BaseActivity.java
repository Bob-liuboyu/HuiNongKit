package com.project.arch_repo.base.activity;

import android.os.Build;
import android.os.Bundle;
import com.project.arch_repo.widget.TokenProgressHUDImpl;
import com.xxf.arch.activity.XXFActivity;
import com.xxf.arch.widget.progresshud.ProgressHUD;
import com.xxf.view.utils.StatusBarUtils;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @version 2.0.1
 * @Description 如果需要自带标题 {@link BaseTitleBarActivity}
 * @date createTime：2019/4/11
 */
public class BaseActivity extends XXFActivity {
    private TokenProgressHUDImpl tokenProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenProgressHUD = new TokenProgressHUDImpl(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int blueColor = 0xFF00C1CE;
            StatusBarUtils.compatStatusBarForM(this, false, blueColor);
        }
    }

    @Override
    public ProgressHUD progressHUD() {
        return tokenProgressHUD;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tokenProgressHUD != null) {
            tokenProgressHUD.detachedContext();
        }
    }
}
