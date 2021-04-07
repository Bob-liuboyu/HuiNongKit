package com.project.module_order.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.common_resource.response.LoginResDTO;
import com.project.module_order.R;
import com.project.module_order.databinding.OrderItemTakePhotoBinding;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-26
 * @Description
 */
public class TakePhotoBtnAdapter extends BaseBindableAdapter<OrderItemTakePhotoBinding, LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean> {
    private DeletePhotoListener mListener;

    @Override
    protected OrderItemTakePhotoBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return OrderItemTakePhotoBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, final OrderItemTakePhotoBinding binding, @Nullable final LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean orderModel, final int index) {
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
        binding.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickPhoto(index);
                }
            }
        });

        if (TextUtils.isEmpty(orderModel.getUrl())) {
            binding.ivPhoto.setVisibility(View.GONE);
            binding.llButton.setVisibility(View.VISIBLE);
        } else {
            binding.ivPhoto.setVisibility(View.VISIBLE);
            binding.llButton.setVisibility(View.GONE);
        }
    }

    public void setDeletePhotoListener(DeletePhotoListener listener) {
        mListener = listener;
    }

    public interface DeletePhotoListener {
        void onDelete(int pos);

        void onClickPhoto(int pos);
    }
}