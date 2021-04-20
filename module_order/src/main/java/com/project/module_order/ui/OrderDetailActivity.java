package com.project.module_order.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.adapter.OrderPhotosListAdapter;
import com.project.module_order.source.impl.OrderRepositoryImpl;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @fileName: OrderDetailActivity
 * @author: liuboyu
 * @date: 2021/3/24 2:32 PM
 * @description: 订单详情
 */
@Route(path = ArouterConfig.Order.ORDER_DETAIL)
public class OrderDetailActivity extends CreateOrderActivity {
    @Autowired
    String claimId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(claimId);
    }

    private void initData(String claimId) {
        if (TextUtils.isEmpty(claimId)) {
            ToastUtils.showToast("claimId 异常，请稍后再试～");
            return;
        }
        OrderRepositoryImpl.getInstance()
                .getClaimDetail(claimId)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<PolicyDetailResDTO>bindToLifecycle(this))
                .compose(XXF.<PolicyDetailResDTO>bindToErrorNotice())
                .compose(XXF.<PolicyDetailResDTO>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("努力加载中...")))
                .subscribe(new Consumer<PolicyDetailResDTO>() {
                    @Override
                    public void accept(PolicyDetailResDTO result) throws Exception {
                        updatePage(result);
                    }
                });
    }

    /**
     * 刷新页面
     *
     * @param result
     */
    private void updatePage(PolicyDetailResDTO result) {
        if (result == null) {
            return;
        }
        binding.setModel(result);
        binding.tvRightText.setVisibility(View.GONE);
        binding.llPolicyCategory.setVisibility(View.GONE);
        binding.tvPolicyCategory.setVisibility(View.VISIBLE);
        List<PolicyDetailResDTO.ClaimListBean> list = result.getClaimList();
        if (list == null || list.size() == 0) {
            binding.lineCount.setVisibility(View.GONE);
            binding.llCount.setVisibility(View.GONE);
        } else {
            binding.lineCount.setVisibility(View.VISIBLE);
            binding.llCount.setVisibility(View.VISIBLE);
            binding.tvCount.setText(list.size() + "头");
        }
        binding.llMeasureWay.setVisibility(View.GONE);
        binding.tvMeasureWay.setVisibility(View.VISIBLE);
        binding.lineCommitDate.setVisibility(View.VISIBLE);
        binding.llCommitDate.setVisibility(View.VISIBLE);
        binding.llButtons.setVisibility(View.GONE);
        binding.tvName.setEnabled(false);
        binding.tvName.setClickable(false);
        binding.tvDateStart.setClickable(false);
        binding.tvDateEnd.setClickable(false);
        binding.tvChoose.setVisibility(View.GONE);
        if(result.getClaimType() == 1){
            binding.layoutMeasure2.setVisibility(View.GONE);
            binding.lineCount.setVisibility(View.GONE);
        }
        binding.tvMaster.setText(result.getName());
        updatePhotoList(list);
    }

    private void updatePhotoList(List<PolicyDetailResDTO.ClaimListBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        mAdapter = new OrderPhotosListAdapter();
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.bindData(true, list);
        binding.tvTitlePhotos.setVisibility(View.VISIBLE);

    }
}
