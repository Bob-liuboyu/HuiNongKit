package com.project.module_order.ui;

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

    public void setData(OrderModel model) {

    }
}
