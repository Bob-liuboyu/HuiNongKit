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


    /**
     * phone : 13051680882
     * insureId : as
     * claimStatus : as
     * claimType : as
     * submitStartDate : as
     * submitEndDate : as
     */

    private String phone;
    private String insureId;
    private String claimStatus;
    private String claimType;
    private String submitStartDate;
    private String submitEndDate;
    private String word;

    /**
     * 是否都选了
     *
     * @return
     */
    public boolean fill() {
        return TextUtils.isEmpty(getClaimStatus()) && TextUtils.isEmpty(getSubmitStartDate()) && TextUtils.isEmpty(getSubmitEndDate());
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInsureId() {
        return insureId;
    }

    public void setInsureId(String insureId) {
        this.insureId = insureId;
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
