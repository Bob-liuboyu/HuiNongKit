package com.project.module_order.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.common_resource.response.MeasureResponse;
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
    private PolicyDetailResDTO.ClaimListBean model;
    private Gson gson;

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
        model = (PolicyDetailResDTO.ClaimListBean) getIntent().getSerializableExtra("model");
        if (list == null || list.get(index) == null) {
            return;
        }
        gson = new Gson();
        mAdapter = new OrderPhotoListAdapter();
        mBinding.ivPhoto.setAdapter(picViewPagerAdapter = new PicViewPagerAdapter(list.get(index).getPigInfo()));
        mBinding.tvTitle.setText("(" + (currentPhotoIndex + 1) + "/" + list.get(index).getPigInfo().size() + ")张");
        mBinding.tvCount.setText("(" + (index + 1) + "/" + list.size() + ")头");
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int i) {
                mBinding.ivPhoto.setCurrentItem(i);
                updatePhoto(i);
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
                    ToastUtils.showToast("已经是第一头了");
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
                    ToastUtils.showToast("已经是最后一头了");
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
                updatePhoto(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        initMeasureData(list.get(index).getPigInfo().get(0));
    }

    private void updatePhoto(int i) {
        for (PolicyDetailResDTO.ClaimListBean.PigInfoBean bean : list.get(index).getPigInfo()) {
            bean.setSelect(false);
        }
        currentPhotoIndex = i;
        list.get(index).getPigInfo().get(i).setSelect(true);
        mAdapter.notifyDataSetChanged();
        mBinding.tvTitle.setText("(" + (currentPhotoIndex + 1) + "/" + list.get(index).getPigInfo().size() + ")张");
        initMeasureData(list.get(index).getPigInfo().get(i));

    }

    /**
     * 刷新测量数据
     *
     * @param pigInfoBean
     */
    private void initMeasureData(PolicyDetailResDTO.ClaimListBean.PigInfoBean pigInfoBean) {
        if (pigInfoBean == null || TextUtils.isEmpty(pigInfoBean.getResults())) {
            return;
        }

        if ("pigBody".equals(pigInfoBean.getColumn())) {
            String name = pigInfoBean.getName();
            if (TextUtils.isEmpty(name)) {
                return;
            }

            mBinding.llDataRoot.setVisibility(View.VISIBLE);
            MeasureResponse measureResponse = gson.fromJson(pigInfoBean.getResults(), MeasureResponse.class);
            if (measureResponse == null) {
                return;
            }
            if (measureResponse.getStatus() != MeasureResponse.CODE_SUCCESS) {
                mBinding.llData.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(measureResponse.getMsg())) {
                    mBinding.llWarning.setVisibility(View.VISIBLE);
                    mBinding.tvWarning.setText(measureResponse.getMsg());
                }else{
                    mBinding.llWarning.setVisibility(View.INVISIBLE);
                }

            } else {
                mBinding.llWarning.setVisibility(View.INVISIBLE);
                mBinding.llData.setVisibility(View.VISIBLE);
                if (name.contains("长") || name.contains("猪身")) {
                    mBinding.tvMeasureResult.setVisibility(View.VISIBLE);
                    mBinding.tvMeasureResult.setText("长度：" + measureResponse.getLength());
                } else if (name.contains("重")) {
                    mBinding.tvMeasureResult.setVisibility(View.VISIBLE);
                    mBinding.tvMeasureResult.setText("重量：" + measureResponse.getWeight());
                } else {
                    mBinding.tvMeasureResult.setVisibility(View.GONE);
                }

                mBinding.tvData.setText(model.getSubmitDate());
                mBinding.tvAddr.setText(model.getAddress());
                mBinding.tvPos.setText(model.getLatitude() + "," + model.getLongitude());
                mBinding.tvCall.setText(model.getPhone());
                mBinding.tvCompany.setText(model.getDeptName());
                mBinding.tvName.setText(model.getName());
            }
        } else {
            mBinding.llDataRoot.setVisibility(View.INVISIBLE);
        }
    }


    private void updatePage(int index) {
        currentPhotoIndex = 0;
        mBinding.tvCount.setText("(" + (index + 1) + "/" + list.size() + ")头");
        mAdapter.bindData(true, list.get(index).getPigInfo());
        mBinding.setModel(mAdapter.getItem(0));
        mBinding.ivPhoto.setCurrentItem(index);
    }
}
