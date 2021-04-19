package com.project.common_resource.response;

import java.io.Serializable;
import java.util.List;

/**
 * @fileName: PolicyDetailResDTO
 * @author: liuboyu
 * @date: 2021/4/7 6:32 PM
 * @description: 理赔单详情
 */
public class PolicyDetailResDTO implements Serializable {
    private String deptName;
    private String measureTypeName;
    private String deptId;
    private String submitDate;
    private String claimId;
    private String claimTypeName;
    private int claimType;
    private String phone;
    private String claimDate;
    private String name;
    private String insureName;
    private String insureId;
    private String id;
    private String claimStatusName;
    private int claimStatus;
    private int measureType;
    private String insureStartTime;
    private String insureEndTime;
    /**
     * simmilarity : 85%
     * address : 北京市朝阳区
     * is_repeat : 1
     * latitude : 20
     * submitDate : 2021-04-06
     * length :
     * weight : 60KG
     * pigInfo : [{"imgUrl":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.med126.com%2Fshouyi%2Fupload%2F201604%2Fvxlr2t5ivsr%28www.med126.com%29.jpg&refer=http%3A%2F%2Fwww.med126.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620288022&t=33c68901ae885c5dba12254bd7227b6a","no":1,"name":"猪身子","results":""},{"imgUrl":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20190125%2Ff2754f62b25c4ae485e397053a94ac29.jpeg&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620288135&t=0310f451d678cbc22bbafd0979505642","no":2,"name":"猪耳朵","results":""},{"imgUrl":"https://image.baidu.com/search/detail?ct=503316480&z=0&tn=baiduimagedetail&ipn=d&word=%E7%8C%AA%E5%A4%B4&step_word=&ie=utf-8&in=&cl=2&lm=-1&st=-1&hd=&latest=&copyright=&cs=3186929892,2086478071&os=1197611825,912795983&simid=3288948892,366169877&pn=78&rn=1&di=1760&ln=1799&fr=&fmq=1617696275668_R&ic=&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&is=0,0&istype=2&ist=&jit=&bdtype=0&spn=0&pi=0&gsm=1e&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%253A%252F%252Ftcsxps.com%252Fdata%252Fattachment%252Fproducts%252F1500480737_big_180620346.jpg%26refer%3Dhttp%253A%252F%252Ftcsxps.com%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Djpeg%3Fsec%3D1620288302%26t%3D2832af909d9d31fa9200070bc3e8797c&rpstart=0&rpnum=0&adpicid=0&force=undefined","no":3,"name":"猪脸","results":""}]
     * claimId : LP2131973719
     * pigEnvironmentTwoUrl :
     * pigEnvironmentOneUrl :
     * id : 123
     * measureType : 0
     * longitude : 30
     * status : 0
     */

    private List<ClaimListBean> claimList;

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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getMeasureTypeName() {
        return measureTypeName;
    }

    public String getInsureName() {
        return insureName;
    }

    public void setInsureName(String insureName) {
        this.insureName = insureName;
    }

    public void setMeasureTypeName(String measureTypeName) {
        this.measureTypeName = measureTypeName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getClaimTypeName() {
        return claimTypeName;
    }

    public void setClaimTypeName(String claimTypeName) {
        this.claimTypeName = claimTypeName;
    }

    public int getClaimType() {
        return claimType;
    }

    public void setClaimType(int claimType) {
        this.claimType = claimType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsureId() {
        return insureId;
    }

    public void setInsureId(String insureId) {
        this.insureId = insureId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClaimStatusName() {
        return claimStatusName;
    }

    public void setClaimStatusName(String claimStatusName) {
        this.claimStatusName = claimStatusName;
    }

    public int getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(int claimStatus) {
        this.claimStatus = claimStatus;
    }

    public int getMeasureType() {
        return measureType;
    }

    public void setMeasureType(int measureType) {
        this.measureType = measureType;
    }

    public List<ClaimListBean> getClaimList() {
        return claimList;
    }

    public void setClaimList(List<ClaimListBean> claimList) {
        this.claimList = claimList;
    }

    public static class ClaimListBean implements Serializable {
        private String simmilarity;
        private String address;
        private int is_repeat;
        private String latitude;
        private String submitDate;
        private String length;
        private String weight;
        private String claimId;
        private String pigEnvironmentTwoUrl;
        private String pigEnvironmentOneUrl;
        private String id;
        private String phone;
        private String deptName;
        private int measureType;
        private String longitude;
        private int status;
        private String name;
        /**
         * imgUrl : https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.med126.com%2Fshouyi%2Fupload%2F201604%2Fvxlr2t5ivsr%28www.med126.com%29.jpg&refer=http%3A%2F%2Fwww.med126.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1620288022&t=33c68901ae885c5dba12254bd7227b6a
         * no : 1
         * name : 猪身子
         * results :
         */

        private List<PigInfoBean> pigInfo;

        public String getSimmilarity() {
            return simmilarity;
        }

        public void setSimmilarity(String simmilarity) {
            this.simmilarity = simmilarity;
        }

        public String getAddress() {
            return address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIs_repeat() {
            return is_repeat;
        }

        public void setIs_repeat(int is_repeat) {
            this.is_repeat = is_repeat;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getSubmitDate() {
            return submitDate;
        }

        public void setSubmitDate(String submitDate) {
            this.submitDate = submitDate;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getClaimId() {
            return claimId;
        }

        public void setClaimId(String claimId) {
            this.claimId = claimId;
        }

        public String getPigEnvironmentTwoUrl() {
            return pigEnvironmentTwoUrl;
        }

        public void setPigEnvironmentTwoUrl(String pigEnvironmentTwoUrl) {
            this.pigEnvironmentTwoUrl = pigEnvironmentTwoUrl;
        }

        public String getPigEnvironmentOneUrl() {
            return pigEnvironmentOneUrl;
        }

        public void setPigEnvironmentOneUrl(String pigEnvironmentOneUrl) {
            this.pigEnvironmentOneUrl = pigEnvironmentOneUrl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getMeasureType() {
            return measureType;
        }

        public void setMeasureType(int measureType) {
            this.measureType = measureType;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<PigInfoBean> getPigInfo() {
            return pigInfo;
        }

        public void setPigInfo(List<PigInfoBean> pigInfo) {
            this.pigInfo = pigInfo;
        }

        public static class PigInfoBean implements Serializable {
            private String imgUrl;
            private int no;
            private String name;
            private String results;
            private String column;
            private boolean select;

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public String getColumn() {
                return column;
            }

            public void setColumn(String column) {
                this.column = column;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public int getNo() {
                return no;
            }

            public void setNo(int no) {
                this.no = no;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getResults() {
                return results;
            }

            public void setResults(String results) {
                this.results = results;
            }
        }
    }
}
