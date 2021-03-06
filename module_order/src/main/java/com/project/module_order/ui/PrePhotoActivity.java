package com.project.module_order.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.databinding.OrderActivityPrePhotoBinding;
import com.xxf.view.utils.StatusBarUtils;

import java.io.File;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-21
 * @Description 图片预览
 */
@Route(path = ArouterConfig.Order.ORDER_PHOTO_PRE)
public class PrePhotoActivity extends BaseActivity {
    private OrderActivityPrePhotoBinding mBinding;
    @Autowired
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int color = getResources().getColor(R.color.arch_black);
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityPrePhotoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView() {
        mBinding.ivPhoto.setImageURI(Uri.fromFile(new File(url)));
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
