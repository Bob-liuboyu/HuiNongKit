package com.project.module_order.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.project.arch_repo.base.recyclerview.BaseBindableAdapter;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.module_order.databinding.OrderItemPolicyListPhotoBinding;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-21
 * @Description
 */
public class OrderPhotoListAdapter extends BaseBindableAdapter<OrderItemPolicyListPhotoBinding, PolicyDetailResDTO.ClaimListBean.PigInfoBean> {
    private int width;

    @Override
    protected OrderItemPolicyListPhotoBinding onCreateBinding(LayoutInflater inflater, ViewGroup viewGroup, int viewType) {
        return OrderItemPolicyListPhotoBinding.inflate(inflater, viewGroup, false);
    }

    @Override
    public void onBindHolder(BaseViewHolder holder, OrderItemPolicyListPhotoBinding binding, @Nullable PolicyDetailResDTO.ClaimListBean.PigInfoBean model, int index) {
        if (model == null) {
            return;
        }
        ViewGroup.LayoutParams params = binding.mRootView.getLayoutParams();
        params.width = width;
        params.height = width;
        binding.mRootView.setLayoutParams(params);
        RoundedCorners roundedCorners = new RoundedCorners(5);
        RequestOptions coverRequestOptions = new RequestOptions()
                .transforms(new CenterCrop(), roundedCorners)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false);
        Glide.with(getContext())
                .asBitmap()
                .load(model.getImgUrl())
                .apply(coverRequestOptions)
                .into(binding.ivPhoto);
        if(model.isSelect()){
           binding.viewShadow.setVisibility(View.VISIBLE);
        }else {
            binding.viewShadow.setVisibility(View.GONE);
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }
}