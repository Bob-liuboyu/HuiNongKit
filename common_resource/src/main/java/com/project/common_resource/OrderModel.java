package com.project.common_resource;

import java.io.Serializable;

/**
 * @fileName: Order
 * @author: liuboyu
 * @date: 2021/3/13 2:38 PM
 * @description: 订单
 */
public class OrderModel implements Serializable {
    private String id;
    private String title;

    public OrderModel(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
