package com.project.common_resource.response;

import java.io.Serializable;

public class InsureListResDTO implements Serializable {

    /**
     * insureName : 李国栋
     * insureStartTime : 2021-04-01 12:00:00
     * insureEndTime : 2021-04-30 12:00:00
     * insureId : DH756899312113
     */

    private String insureName;
    private String insureStartTime;
    private String insureEndTime;
    private String insureId;

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public String getInsureStartTime() {
        return insureStartTime;
    }

    public void setInsureStartTime(String insureStartTime) {
        this.insureStartTime = insureStartTime;
    }

    public String getInsureEndTime() {
        return insureEndTime;
    }

    public void setInsureEndTime(String insureEndTime) {
        this.insureEndTime = insureEndTime;
    }

    public String getInsureId() {
        return insureId;
    }

    public void setInsureId(String insureId) {
        this.insureId = insureId;
    }
}
