package com.project.huinongkit.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.fragment.BaseFragment;
import com.project.common_resource.OrderModel;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.PolicyListResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.adapter.OrderListAdapter;
import com.project.huinongkit.databinding.MainFragmentMainBinding;
import com.project.huinongkit.dialog.SelectFilterDialog;
import com.project.huinongkit.model.SelectFilterModel;
import com.project.huinongkit.source.impl.HomeRepositoryImpl;
import com.xxf.arch.XXF;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @fileName: MainFragment
 * @author: liuboyu
 * @date: 2021/3/13 2:18 PM
 * @description: 首页
 */
public class MainFragment extends BaseFragment {
    private MainFragmentMainBinding mBinding;
    private OrderListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = MainFragmentMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
        initData();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initData() {
        HomeRepositoryImpl.getInstance()
                .getPolicyOrderList("","","","","","","")
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<List<PolicyListResDTO>>bindToLifecycle(this))
                .compose(XXF.<List<PolicyListResDTO>>bindToErrorNotice())
                .compose(XXF.<List<PolicyListResDTO>>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("登陆中...")))
                .subscribe(new Consumer<List<PolicyListResDTO>>() {
                    @Override
                    public void accept(List<PolicyListResDTO> data) throws Exception {
                    }
                });
    }

    private void initView() {
        List<OrderModel> munes = new ArrayList<>();
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "评价医生"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "诊脉记录"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "医疗记录"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "购药记录"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "优惠信息"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "健康提示"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "同步查询"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "活动发布"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "经验交流"));
        munes.add(new OrderModel(new Random(10000).nextInt() + "", "联系客服"));

        mBinding.recyclerView.setAdapter(mAdapter = new OrderListAdapter());
        mAdapter.bindData(true, munes);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                ARouter.getInstance().build(ArouterConfig.Order.ORDER_DETAIL)
                        .navigation();
            }
        });

        mBinding.mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectFilterDialog(getActivity(), getActivity(), new IResultDialog.OnDialogClickListener<SelectFilterModel>() {
                    @Override
                    public boolean onCancel(@NonNull DialogInterface dialog, @Nullable SelectFilterModel cancelResult) {
                        ToastUtils.showToast(cancelResult.getStart());
                        return false;
                    }

                    @Override
                    public boolean onConfirm(@NonNull DialogInterface dialog, @Nullable SelectFilterModel confirmResult) {
                        ToastUtils.showToast(confirmResult.getStart());
                        return false;
                    }
                }).show();
            }
        });
    }
}
