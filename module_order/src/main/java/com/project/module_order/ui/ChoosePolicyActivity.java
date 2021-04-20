package com.project.module_order.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseTitleBarActivity;
import com.project.common_resource.response.InsureListResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.adapter.ChoosePolicyListAdapter;
import com.project.module_order.databinding.OrderActivityChoosePolicyBinding;
import com.project.module_order.source.impl.OrderRepositoryImpl;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.utils.StatusBarUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-21
 * @Description
 */
@Route(path = ArouterConfig.Order.ORDER_CHOOSE)
public class ChoosePolicyActivity extends BaseTitleBarActivity {
    private OrderActivityChoosePolicyBinding mBinding;
    private ChoosePolicyListAdapter mAdapter;
    private int currentPageIndex = 1;
    private boolean hasNext;
    @Autowired
    String words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitleBarTitle("理赔登记");
        int color = 0xFFFFFFFF;
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityChoosePolicyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
        initData(words);
    }

    private void initView() {
        mBinding.mEditText.setText(words);
        TextView emptyTextView = mBinding.layoutEmpty.findViewById(R.id.tv_empty_text);
        emptyTextView.setText("没有可用的理赔登记信息");
        mBinding.recyclerView.setLinearLayout();
        mBinding.recyclerView.setAdapter(mAdapter = new ChoosePolicyListAdapter());
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                if (mAdapter == null || mAdapter.getItemCount() <= 0) {
                    return;
                }
                setResult(RESULT_OK, new Intent().putExtra("result", mAdapter.getItem(index)));
                finish();
            }
        });

        mBinding.mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    words = mBinding.mEditText.getText().toString();
                    initData(words);
                    return true;
                }
                return false;
            }
        });

        mBinding.recyclerView.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                currentPageIndex = 1;
                initData(words);
            }

            @Override
            public void onLoadMore() {
                currentPageIndex = currentPageIndex + 1;
                initData(words);
            }
        });
    }

    private void initData(String search) {
        OrderRepositoryImpl.getInstance()
                .getInsureList(currentPageIndex, search)
                .compose(XXF.<InsureListResDTO>bindToLifecycle(this))
                .compose(XXF.<InsureListResDTO>bindToErrorNotice())
                .compose(XXF.<InsureListResDTO>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("努力加载中...")))
                .subscribe(new Consumer<InsureListResDTO>() {
                    @Override
                    public void accept(InsureListResDTO result) throws Exception {
                        if (result == null || result.getResultList() == null || result.getResultList().size() == 0) {
                            mBinding.recyclerView.setPullLoadMoreCompleted();
                            if (currentPageIndex == 1) {
                                mBinding.layoutEmpty.setVisibility(View.VISIBLE);
                                mBinding.recyclerView.setVisibility(View.GONE);
                            }
                            return;
                        }
                        hasNext = result.isHasNext();
                        List<InsureListResDTO.ResultListBean> data = result.getResultList();
                        if (currentPageIndex == 1) {
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
                        mBinding.recyclerView.setPushRefreshEnable(hasNext);
                    }
                });
    }
}
