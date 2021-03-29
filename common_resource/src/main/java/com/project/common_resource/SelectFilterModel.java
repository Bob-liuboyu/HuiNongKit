package com.project.common_resource;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * @fileName: SelectFilterModel
 * @author: liuboyu
 * @date: 2021/3/16 5:49 PM
 * @description:
 */
public class SelectFilterModel implements Serializable {
    private String status;
    private String startDate;
    private String endDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 是否都选了
     * @return
     */
    public boolean fill() {
        return TextUtils.isEmpty(getStatus()) && TextUtils.isEmpty(getStartDate()) && TextUtils.isEmpty(getEndDate());
    }

    @Override
    public String toString() {
        return "FilterModel{" +
                "status='" + status + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
