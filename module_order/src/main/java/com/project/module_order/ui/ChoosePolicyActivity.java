package com.project.module_order.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseTitleBarActivity;
import com.project.common_resource.OrderModel;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.adapter.ChoosePolicyListAdapter;
import com.project.module_order.databinding.OrderActivityChoosePolicyBinding;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-21
 * @Description
 */
@Route(path = ArouterConfig.Order.ORDER_CHOOSE)
public class ChoosePolicyActivity extends BaseTitleBarActivity {
    private OrderActivityChoosePolicyBinding mBinding;
    private ChoosePolicyListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitleBarTitle("理赔登记");
        int color = 0xFFFFFFFF;
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityChoosePolicyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView() {
        final List<OrderModel> munes = new ArrayList<>();
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "评价医生","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "诊脉记录","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "医疗记录","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "购药记录","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "优惠信息","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "健康提示","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "同步查询","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "活动发布","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "经验交流","刘伯羽","2021-3-29","2021-3-1"));
        munes.add(new OrderModel("LKAJLKDFALKJSDLFJALKFJAKLS", "联系客服","刘伯羽","2021-3-29","2021-3-1"));

        mBinding.recyclerView.setAdapter(mAdapter = new ChoosePolicyListAdapter());
        mAdapter.bindData(true, munes);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                setResult(RESULT_OK, new Intent().putExtra("result", munes.get(index)));
                finish();
            }
        });
    }
}
