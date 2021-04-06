package com.project.common_resource.response;

import java.io.Serializable;

public class PolicyListResDTO implements Serializable {

    // 0:育肥猪 1：能繁母猪
    public static final int TYPE_0 = 0;
    public static final int TYPE_1 = 1;

    // 0:待处理 1：已理赔
    public static final int STATUS_0 = 0;
    public static final int STATUS_1 = 1;


    private int claimType;
    private String insureName;
    private String orgName;
    private String claimId;
    private String claimSubmitDate;
    private String claimStatusName;
    private String claimTypeName;
    private int claimStatus;

    public int getClaimType() {
        return claimType;
    }

    public void setClaimType(int claimType) {
        this.claimType = claimType;
    }

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getClaimSubmitDate() {
        return claimSubmitDate;
    }

    public void setClaimSubmitDate(String claimSubmitDate) {
        this.claimSubmitDate = claimSubmitDate;
    }

    public String getClaimStatusName() {
        return claimStatusName;
    }

    public void setClaimStatusName(String claimStatusName) {
        this.claimStatusName = claimStatusName;
    }

    public String getClaimTypeName() {
        return claimTypeName;
    }

    public void setClaimTypeName(String claimTypeName) {
        this.claimTypeName = claimTypeName;
    }

    public int getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(int claimStatus) {
        this.claimStatus = claimStatus;
    }
}
