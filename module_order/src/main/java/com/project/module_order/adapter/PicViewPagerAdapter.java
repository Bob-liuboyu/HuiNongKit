package com.project.module_order.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.arch_repo.utils.GlideUtils;
import com.project.common_resource.response.PolicyDetailResDTO;

import java.util.List;

/**
 * @fileName: PicViewPagerAdapter
 * @author: liuboyu
 * @date: 2021/4/15 2:15 PM
 * @description:
 */
public class PicViewPagerAdapter extends PagerAdapter {
    private List<PolicyDetailResDTO.ClaimListBean.PigInfoBean> mData;

    public PicViewPagerAdapter(List<PolicyDetailResDTO.ClaimListBean.PigInfoBean> list) {
        mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        GlideUtils.loadImage(imageView,mData.get(position).getImgUrl());
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        container.addView(imageView, layoutParams);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
