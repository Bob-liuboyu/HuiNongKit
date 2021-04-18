package com.project.module_order.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.module_order.R;
import com.project.module_order.adapter.OrderPhotoListAdapter;
import com.project.module_order.adapter.PicViewPagerAdapter;
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
    private List<PolicyDetailResDTO.ClaimListBean> list;
    /**
     * 第几支猪
     */
    private int index;
    /**
     * 第几张照片
     */
    private int currentPhotoIndex;
    private OrderPhotoListAdapter mAdapter;
    private PicViewPagerAdapter picViewPagerAdapter;

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
        list = (List<PolicyDetailResDTO.ClaimListBean>) getIntent().getSerializableExtra("list");
        index = getIntent().getIntExtra("index", 0);
        if (list == null || list.get(index) == null) {
            return;
        }
        mAdapter = new OrderPhotoListAdapter();
        mBinding.ivPhoto.setAdapter(picViewPagerAdapter = new PicViewPagerAdapter(list.get(index).getPigInfo()));
        mBinding.tvTitle.setText("(" + (currentPhotoIndex + 1) + "/" + list.get(index).getPigInfo().size() + ")张");
        mBinding.tvCount.setText("(" + (index + 1) + "/" + list.size() + ")只");
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int i) {
                mBinding.ivPhoto.setCurrentItem(i);
                for (PolicyDetailResDTO.ClaimListBean.PigInfoBean bean : list.get(index).getPigInfo()) {
                    bean.setSelect(false);
                }
                list.get(index).getPigInfo().get(i).setSelect(true);
                mAdapter.notifyDataSetChanged();
                currentPhotoIndex = i;
                mBinding.tvTitle.setText("(" + (currentPhotoIndex + 1) + "/" + list.get(index).getPigInfo().size() + ")张");

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
                if (list.get(index).getPigInfo() != null) {
                    list.get(index).getPigInfo().get(0).setSelect(true);
                }
                mAdapter.bindData(true, list.get(index).getPigInfo());
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
                updatePage(index);
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
                updatePage(index);
            }
        });
        mBinding.ivPhoto.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (PolicyDetailResDTO.ClaimListBean.PigInfoBean bean : list.get(index).getPigInfo()) {
                    bean.setSelect(false);
                }
                currentPhotoIndex = i;
                list.get(index).getPigInfo().get(i).setSelect(true);
                mAdapter.notifyDataSetChanged();
                mBinding.tvTitle.setText("(" + (currentPhotoIndex + 1) + "/" + list.get(index).getPigInfo().size() + ")张");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void updatePage(int index) {
        currentPhotoIndex = 0;
        mBinding.tvCount.setText("(" + (index + 1) + "/" + list.size() + ")只");
        mAdapter.bindData(true, list.get(index).getPigInfo());
        mBinding.setModel(mAdapter.getItem(0));
        mBinding.ivPhoto.setCurrentItem(index);
    }
}
