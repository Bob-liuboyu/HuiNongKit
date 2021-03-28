package com.project.module_order.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.arch_repo.utils.GlideUtils;
import com.project.common_resource.PhotoModel;
import com.project.module_order.databinding.OrderItemPolicyListPhotoBinding;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-21
 * @Description
 */
public class OrderPhotoListAdapter extends BaseBindableAdapter<OrderItemPolicyListPhotoBinding, PhotoModel> {
    private int width;

    @Override
    protected OrderItemPolicyListPhotoBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return OrderItemPolicyListPhotoBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, OrderItemPolicyListPhotoBinding binding, @Nullable PhotoModel model, int index) {
        if (model == null) {
            return;
        }
        ViewGroup.LayoutParams params = binding.ivPhoto.getLayoutParams();
        params.width = width;
        params.height = width;
        binding.ivPhoto.setLayoutParams(params);
        GlideUtils.loadImage(binding.ivPhoto, model.getUrl());
    }

    public void setWidth(int width) {
        this.width = width;
    }
}