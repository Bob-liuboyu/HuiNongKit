package com.project.module_order.ui;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.common_resource.OrderModel;
import com.project.config_repo.ArouterConfig;

/**
 * @fileName: OrderDetailActivity
 * @author: liuboyu
 * @date: 2021/3/24 2:32 PM
 * @description: 订单详情
 */
@Route(path = ArouterConfig.Order.ORDER_DETAIL)
public class OrderDetailActivity extends CreateOrderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }

    public void setData() {
        binding.tvName.setText("刘伯羽");
        binding.llPolicyCategory.setVisibility(View.GONE);
        binding.tvPolicyCategory.setVisibility(View.VISIBLE);
        binding.tvPolicyCategory.setText("公猪");
        binding.tvChoose.setVisibility(View.GONE);
        binding.tvCode.setText("KJLAKJDFLKJASKJFAK");
        binding.tvDateStart.setText("2021-3-25");
        binding.tvDateEnd.setText("2021-3-26");
        binding.tvMaster.setText("刘伯羽2");
        binding.tvDateCommit.setText("2021-3-25 10:00");
        binding.llMeasureWay.setVisibility(View.GONE);
        binding.tvMeasureWay.setVisibility(View.VISIBLE);
        binding.tvMeasureWay.setText("测长度");
        binding.lineCount.setVisibility(View.VISIBLE);
        binding.llCount.setVisibility(View.VISIBLE);
        binding.tvCount.setText("11只");
        binding.lineCommitDate.setVisibility(View.VISIBLE);
        binding.llCommitDate.setVisibility(View.VISIBLE);
        binding.llButtons.setVisibility(View.GONE);
        binding.tvName.setEnabled(false);
        binding.tvName.setClickable(false);
        binding.tvDateStart.setClickable(false);
        binding.tvDateEnd.setClickable(false);
    }
}
