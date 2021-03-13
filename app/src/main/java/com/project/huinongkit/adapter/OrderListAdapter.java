package com.project.huinongkit.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.common_resource.OrderModel;
import com.project.huinongkit.databinding.MainItemOrderBinding;
import com.project.huinongkit.databinding.MainItemOrderBindingImpl;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @fileName: OrderListAdater
 * @author: liuboyu
 * @date: 2021/3/13 2:34 PM
 * @description: 首页订单列表
 */
public class OrderListAdapter extends BaseBindableAdapter<MainItemOrderBinding, OrderModel> {
    @Override
    protected MainItemOrderBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return MainItemOrderBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, MainItemOrderBinding binding, @Nullable OrderModel orderModel, int index) {
        if (orderModel == null) {
            return;
        }
        binding.setModel(orderModel);
    }
}
