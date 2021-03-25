package com.project.module_order.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.utils.GlideUtils;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.databinding.OrderActivityTakePhotoBinding;
import com.xxf.view.utils.StatusBarUtils;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-25
 * @Description
 */
@Route(path = ArouterConfig.Order.ORDER_TAKE_PHOTO)
public class TakePhotoActivity extends BaseActivity {
    private OrderActivityTakePhotoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = getResources().getColor(R.color.arch_black);
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityTakePhotoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
        GlideUtils.loadImage(mBinding.ivPhoto,"https://pics2.baidu.com/feed/8d5494eef01f3a29e78cea677be85c345d607c22.jpeg?token=1a816f5c4652a5e59ec004141f57f51f&s=F0F630C5CC1263DC8A3001DB03005097");
    }

    private void initView() {
        for (int i = 0; i < 3; i++) {
            View view = View.inflate(this, R.layout.order_item_take_photo, null);
            ImageView icon = view.findViewById(R.id.iv_icon);
            TextView text = view.findViewById(R.id.tv_text);
            icon.setImageResource(R.mipmap.icon_add_white);
            text.setText("猪耳标签");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtils.dip2px(this, 75), DisplayUtils.dip2px(this, 75));
            if (i != 0) {
                params.leftMargin = DisplayUtils.dip2px(this, 14);
            }
            view.setLayoutParams(params);

            mBinding.llPhotos.addView(view);
        }
    }
}
