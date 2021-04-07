package com.project.common_resource.response;

import java.io.Serializable;
import java.util.List;

public class PolicyListResDTO implements Serializable {

    // 0:育肥猪 1：能繁母猪
    public static final int TYPE_0 = 0;
    public static final int TYPE_1 = 1;

    // 0:待处理 1：已理赔
    public static final int STATUS_0 = 0;
    public static final int STATUS_1 = 1;

    private String pageFlag;
    private int pageNo;
    private int pageRows;
    private int totalCount;
    private int totalPages;
    private int last;
    private boolean hasNext;
    private int nextPage;
    private boolean hasPre;
    private int prePage;
    private int beginCount;
    private int first;

    private List<ResultListBean> resultList;
    private Params params;

    public String getPageFlag() {
        return pageFlag;
    }

    public void setPageFlag(String pageFlag) {
        this.pageFlag = pageFlag;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageRows() {
        return pageRows;
    }

    public void setPageRows(int pageRows) {
        this.pageRows = pageRows;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getBeginCount() {
        return beginCount;
    }

    public void setBeginCount(int beginCount) {
        this.beginCount = beginCount;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public List<ResultListBean> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultListBean> resultList) {
        this.resultList = resultList;
    }

    public static class ResultListBean {
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

    public static class Params {

        /**
         * phone : 13051680882
         * insureId : as
         * claimStatus : as
         * claimType : as
         * submitStartDate : as
         * submitEndDate : as
         * search : 123
         * claimId : 123
         * token : 12345
         */

        private String phone;
        private String insureId;
        private String claimStatus;
        private String claimType;
        private String submitStartDate;
        private String submitEndDate;
        private String search;
        private String claimId;
        private String token;

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

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public String getClaimId() {
            return claimId;
        }

        public void setClaimId(String claimId) {
            this.claimId = claimId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
