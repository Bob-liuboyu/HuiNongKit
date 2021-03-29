package com.project.module_order.ui;

import android.os.Bundle;
import android.view.View;

import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.utils.GlideUtils;
import com.project.common_resource.OrderPhotoListModel;
import com.project.module_order.R;
import com.project.module_order.adapter.OrderPhotoListAdapter;
import com.project.module_order.databinding.OrderActivityPrePhotosBinding;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.utils.StatusBarUtils;

import java.util.List;


/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-21
 * @Description 图片预览
 */
public class PrePhotosActivity extends BaseActivity {
    private OrderActivityPrePhotosBinding mBinding;
    private List<OrderPhotoListModel> list;
    private int index;
    private OrderPhotoListAdapter mAdapter;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = getResources().getColor(R.color.arch_black);
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityPrePhotosBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView() {
        list = (List<OrderPhotoListModel>) getIntent().getSerializableExtra("list");
        index = getIntent().getIntExtra("index", 0);
        if (list == null || list.get(index) == null) {
            return;
        }
        mBinding.tvTitle.setText("图片详情" + "(" + (index + 1) + "/" + list.size() + ")");
        mAdapter = new OrderPhotoListAdapter();

        updataPage(index);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                GlideUtils.loadImage(mBinding.ivPhoto, mAdapter.getItem(index).getUrl());
            }
        });
        mBinding.recyclerView.post(new Runnable() {
            @Override
            public void run() {
                int screenWidth = DisplayUtils.getRealScreenSize(mBinding.recyclerView.getContext()).x;
                int leftWidth = mBinding.tvPre.getWidth();
                int rightWidth = mBinding.tvNext.getWidth();
                int width = (screenWidth - DisplayUtils.dip2px(mBinding.recyclerView.getContext(), 15 * 2) - leftWidth - rightWidth) / 3;
                mAdapter.setWidth(width);
                mAdapter.bindData(true, list.get(index).getPhotos());
                mBinding.recyclerView.setAdapter(mAdapter);
            }
        });
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.tvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index - 1 < 0) {
                    ToastUtils.showToast("已经是第一只了");
                    return;
                }
                index--;
                updataPage(index);
            }
        });
        mBinding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index + 1 >= list.size()) {
                    ToastUtils.showToast("已经是最后一只了");
                    return;
                }
                index++;
                updataPage(index);
            }
        });
    }


    private void updataPage(int index) {
        mBinding.tvTitle.setText("(" + (index + 1) + "/" + list.size() + ")");
        mAdapter.bindData(true, list.get(index).getPhotos());
        mBinding.setModel(mAdapter.getItem(0));
        GlideUtils.loadImage(mBinding.ivPhoto, mAdapter.getItem(0).getUrl());
    }
}
