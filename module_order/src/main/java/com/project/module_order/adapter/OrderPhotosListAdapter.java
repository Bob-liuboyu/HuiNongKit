package com.project.module_order.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.common_resource.OrderPhotoListModel;
import com.project.module_order.databinding.OrderItemPolicyListPhotosBinding;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @fileName: OrderListAdater
 * @author: liuboyu
 * @date: 2021/3/13 2:34 PM
 * @description: 首页订单列表
 */
public class OrderPhotosListAdapter extends BaseBindableAdapter<OrderItemPolicyListPhotosBinding, OrderPhotoListModel> {
    @Override
    protected OrderItemPolicyListPhotosBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return OrderItemPolicyListPhotosBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, OrderItemPolicyListPhotosBinding binding, @Nullable OrderPhotoListModel model, int index) {
        if (model == null) {
            return;
        }
        OrderPhotoListAdapter adapter = new OrderPhotoListAdapter();
        adapter.bindData(true, model.getPhotos());
        binding.recyclerView.setAdapter(adapter);
        binding.tvResultNum.setText(model.getResult());
    }
}
