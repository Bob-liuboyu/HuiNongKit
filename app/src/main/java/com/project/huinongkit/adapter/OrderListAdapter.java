package com.project.huinongkit.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.common_resource.OrderModel;
import com.project.common_resource.response.PolicyListResDTO;
import com.project.huinongkit.R;
import com.project.huinongkit.databinding.MainItemOrderBinding;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @fileName: OrderListAdater
 * @author: liuboyu
 * @date: 2021/3/13 2:34 PM
 * @description: 首页订单列表
 */
public class OrderListAdapter extends BaseBindableAdapter<MainItemOrderBinding, PolicyListResDTO.ResultListBean> {
    @Override
    protected MainItemOrderBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return MainItemOrderBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, MainItemOrderBinding binding, @Nullable PolicyListResDTO.ResultListBean orderModel, int index) {
        if (orderModel == null) {
            return;
        }
        binding.setModel(orderModel);

        Drawable drawable = null;
        // 0:育肥猪 1：能繁母猪
        if (orderModel.getClaimType() == PolicyListResDTO.TYPE_0) {
            drawable = getContext().getResources().getDrawable(R.mipmap.icon_pig_boy);
        } else {
            drawable = getContext().getResources().getDrawable(R.mipmap.icon_pig_girl);

        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        binding.tvCategory.setCompoundDrawables(drawable, null, null, null);


        // 0:待处理 1：已理赔
        if (orderModel.getClaimStatus() == PolicyListResDTO.STATUS_0) {
            binding.tvStatus.setSelected(true);
            binding.tvStatus.setText("待处理");
        } else {
            binding.tvStatus.setSelected(false);
            binding.tvStatus.setText("已理赔");
        }
    }
}
