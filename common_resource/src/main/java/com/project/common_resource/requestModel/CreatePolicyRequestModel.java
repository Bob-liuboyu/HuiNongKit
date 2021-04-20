package com.project.common_resource.requestModel;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-04-07
 * @Description
 */
public class CreatePolicyRequestModel implements Serializable {

    private String claimId;
    private String claimType;
    private String insureId;
    private String phone;
    private String claimUserId;
    private String measureType;
    private String claimName;
    private String insureStartTime;
    private String insureEndTime;
    private String insureName;
    private String token;
    private List<PhotoInfoEntity> photoInfo;
    public String getInsureEndTime() {
        return insureEndTime;
    }

    public void setInsureEndTime(String insureEndTime) {
        this.insureEndTime = insureEndTime;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getInsureId() {
        return insureId;
    }

    public void setInsureId(String insureId) {
        this.insureId = insureId;
    }

    public String getPhone() {
        return phone;
    }

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClaimUserId() {
        return claimUserId;
    }

    public void setClaimUserId(String claimUserId) {
        this.claimUserId = claimUserId;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getClaimName() {
        return claimName;
    }

    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }

    public String getInsureStartTime() {
        return insureStartTime;
    }

    public void setInsureStartTime(String insureStartTime) {
        this.insureStartTime = insureStartTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<PhotoInfoEntity> getPhotoInfo() {
        return photoInfo;
    }

    public void setPhotoInfo(List<PhotoInfoEntity> photoInfo) {
        this.photoInfo = photoInfo;
    }

    public static class PhotoInfoEntity {
        /**
         * pigId : 12345
         * body_info : [{"column":"pigBody","imgBase64":"","results":{}},{"column":"pigFace","imgBase64":"","results":{}},{"column":"pigEar","imgBase64":"","results":{}},{"column":"pigEnvironmentOne","imgBase64":"","results":{}},{"column":"pigEnvironmentTwo","imgBase64":"","results":{}}]
         * siteType : 0
         * longitude :
         * latitude :
         * address :
         */

        private String pigId;
        private String longitude;
        private String latitude;
        private String address;
        private List<BodyInfoEntity> body_info;

        public String getPigId() {
            return pigId;
        }

        public void setPigId(String pigId) {
            this.pigId = pigId;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<BodyInfoEntity> getBody_info() {
            return body_info;
        }

        public void setBody_info(List<BodyInfoEntity> body_info) {
            this.body_info = body_info;
        }

        public static class BodyInfoEntity {
            /**
             * column : pigBody
             * imgBase64 :
             * results : {}
             */

            private String column;
            private String imgBase64;
            private String results;
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getColumn() {
                return column;
            }

            public void setColumn(String column) {
                this.column = column;
            }

            public String getImgBase64() {
                return imgBase64;
            }

            public void setImgBase64(String imgBase64) {
                this.imgBase64 = imgBase64;
            }


            public String getResults() {
                return results;
            }

            public void setResults(String results) {
                this.results = results;
            }

            @Override
            public String toString() {
                return "BodyInfoEntity{" +
                        "column='" + column + '\'' +
                        ", imgBase64='" + imgBase64 + '\'' +
                        ", results='" + results + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "PhotoInfoEntity{" +
                    "pigId='" + pigId + '\'' +
                    ", longitude='" + longitude + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", address='" + address + '\'' +
                    ", body_info=" + body_info +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CreatePolicyRequestModel{" +
                "claimId='" + claimId + '\'' +
                ", claimType='" + claimType + '\'' +
                ", insureId='" + insureId + '\'' +
                ", phone='" + phone + '\'' +
                ", claimUserId='" + claimUserId + '\'' +
                ", measureType='" + measureType + '\'' +
                ", claimName='" + claimName + '\'' +
                ", insureStartTime='" + insureStartTime + '\'' +
                ", insureEndTime='" + insureEndTime + '\'' +
                ", token='" + token + '\'' +
                ", photoInfo=" + photoInfo +
                '}';
    }
}
