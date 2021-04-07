package com.project.huinongkit.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.fragment.BaseFragment;
import com.project.common_resource.OrderModel;
import com.project.common_resource.SelectFilterModel;
import com.project.common_resource.response.PolicyListResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.R;
import com.project.huinongkit.adapter.OrderListAdapter;
import com.project.huinongkit.databinding.MainFragmentMainBinding;
import com.project.huinongkit.dialog.SelectFilterDialog;
import com.project.huinongkit.source.impl.HomeRepositoryImpl;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
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
    private SelectFilterModel mFilterModel;
    private int currentPageIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = MainFragmentMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
        initData(mFilterModel = new SelectFilterModel());
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initData(SelectFilterModel model) {
        if (model == null) {
            return;
        }
        HomeRepositoryImpl.getInstance()
                .getPolicyOrderList(currentPageIndex, model)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<PolicyListResDTO>bindToLifecycle(this))
                .compose(XXF.<PolicyListResDTO>bindToErrorNotice())
                .compose(XXF.<PolicyListResDTO>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("努力加载中...")))
                .subscribe(new Consumer<PolicyListResDTO>() {
                    @Override
                    public void accept(PolicyListResDTO result) throws Exception {
                        if (result == null || result.getResultList() == null || result.getResultList().size() == 0) {
                            return;
                        }
                        List<PolicyListResDTO.ResultListBean> data = result.getResultList();
                        if (currentPageIndex == 0) {
                            mAdapter.bindData(true, data);
                        } else {
                            mAdapter.addItems(data);
                        }
                        if (mAdapter.getDataSize() <= 0) {
                            mBinding.layoutEmpty.setVisibility(View.VISIBLE);
                            mBinding.recyclerView.setVisibility(View.GONE);
                        } else {
                            mBinding.layoutEmpty.setVisibility(View.GONE);
                            mBinding.recyclerView.setVisibility(View.VISIBLE);
                        }
                        mBinding.recyclerView.setPullLoadMoreCompleted();
                    }
                });
    }

    private void initView() {
        mBinding.recyclerView.setAdapter(mAdapter = new OrderListAdapter());
        mBinding.recyclerView.setLinearLayout();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                ARouter.getInstance().build(ArouterConfig.Order.ORDER_DETAIL)
                        .navigation();
            }
        });
        TextView emptyTextView = mBinding.layoutEmpty.findViewById(R.id.tv_empty_text);
        SpannableString spannableString = new SpannableString("暂无数据，点击 \"+\" 开始理赔登记");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0073BC")), 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(20, true), 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        emptyTextView.setText(spannableString);
        mBinding.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SelectFilterDialog(getActivity(), getActivity(), mFilterModel, new IResultDialog.OnDialogClickListener<SelectFilterModel>() {
                    @Override
                    public boolean onCancel(@NonNull DialogInterface dialog, @Nullable SelectFilterModel cancelResult) {
                        mFilterModel = cancelResult;
                        return false;
                    }

                    @Override
                    public boolean onConfirm(@NonNull DialogInterface dialog, @Nullable SelectFilterModel confirmResult) {
                        mFilterModel = confirmResult;
                        initData(mFilterModel);
                        return false;
                    }
                }).show();
            }
        });
        mBinding.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mFilterModel.setSearch(mBinding.mEditText.getText().toString());
                    initData(mFilterModel);
                    return true;
                }
                return false;
            }
        });
        mBinding.recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                currentPageIndex = 0;
                initData(mFilterModel);
            }

            @Override
            public void onLoadMore() {
                currentPageIndex = currentPageIndex + 1;
                initData(mFilterModel);
            }
        });
    }
}
