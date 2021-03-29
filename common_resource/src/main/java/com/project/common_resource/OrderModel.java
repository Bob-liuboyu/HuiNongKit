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
    private String name;
    private String startData;
    private String endData;

    public OrderModel(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public OrderModel(String id, String title, String name, String startData, String endData) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.startData = startData;
        this.endData = endData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartData() {
        return startData;
    }

    public void setStartData(String startData) {
        this.startData = startData;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
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
