package com.project.module_order.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.common_resource.OrderPhotoListModel;
import com.project.module_order.databinding.OrderItemPolicyListPhotosBinding;
import com.project.module_order.ui.PrePhotosActivity;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @fileName: OrderListAdater
 * @author: liuboyu
 * @date: 2021/3/13 2:34 PM
 * @description: 首页订单列表
 */
public class OrderPhotosListAdapter extends BaseBindableAdapter<OrderItemPolicyListPhotosBinding, OrderPhotoListModel> {
    private int width;

    @Override
    protected OrderItemPolicyListPhotosBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return OrderItemPolicyListPhotosBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, final OrderItemPolicyListPhotosBinding binding, @Nullable final OrderPhotoListModel model, final int index) {
        if (model == null) {
            return;
        }
        final OrderPhotoListAdapter adapter = new OrderPhotoListAdapter();
        binding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int screenWidth = DisplayUtils.getRealScreenSize(binding.recyclerView.getContext()).x;
                int leftWidth = binding.llLeft.getWidth();
                int width = (screenWidth - DisplayUtils.dip2px(binding.recyclerView.getContext(), 15 * 6) - leftWidth) / 3;
                adapter.setWidth(width);
                adapter.bindData(true, model.getPhotos());
                binding.recyclerView.setAdapter(adapter);
                binding.tvResult.setText(model.getResult());
                binding.tvIndex.setText((index + 1) + "");
            }
        });
        binding.shadowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", model);
                Intent intent = new Intent(getContext(), PrePhotosActivity.class);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
