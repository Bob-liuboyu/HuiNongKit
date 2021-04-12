package com.project.common_resource;

import java.io.Serializable;

/**
 * @fileName: SelectFilterModel
 * @author: liuboyu
 * @date: 2021/3/16 5:49 PM
 * @description:
 */
public class SelectFilterModel implements Serializable {

    // 0:待处理 1：已理赔
    public static final String STATUS_DONE = "1";
    public static final String STATUS_NO = "0";

    /**
     * search : DH123131
     * claimStatus : as
     * claimType : as
     * submitStartDate : as
     * submitEndDate : as
     */

    private String search;
    private String claimStatus;
    private String claimType;
    private String submitEndDate;
    private String submitStartDate;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getSubmitStartDate() {
        return submitStartDate;
    }

    public void setSubmitStartDate(String submitStartDate) {
        this.submitStartDate = submitStartDate;
    }

    public String getSubmitEndDate() {
        return submitEndDate;
    }

    public void setSubmitEndDate(String submitEndDate) {
        this.submitEndDate = submitEndDate;
    }
}
