package com.project.module_order.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.common_resource.TakePhotoButtonItem;
import com.project.module_order.R;
import com.project.module_order.databinding.OrderItemTakePhotoBinding;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-26
 * @Description
 */
public class TakePhotoBtnAdapter extends BaseBindableAdapter<OrderItemTakePhotoBinding, TakePhotoButtonItem> {
    private DeletePhotoListener mListener;

    @Override
    protected OrderItemTakePhotoBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return OrderItemTakePhotoBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, final OrderItemTakePhotoBinding binding, @Nullable final TakePhotoButtonItem orderModel, final int index) {
        if (orderModel == null) {
            return;
        }
        binding.ivIcon.setImageResource(R.mipmap.icon_add_white);
        binding.tvText.setText(orderModel.getName());
        binding.llRootView.setSelected(orderModel.isSelect());
        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDelete(index);
                }
            }
        });
    }

    public void setDeletePhotoListener(DeletePhotoListener listener) {
        mListener = listener;
    }

    public interface DeletePhotoListener {
        void onDelete(int pos);
    }
}