package com.project.common_resource.response;

import java.io.Serializable;
import java.util.List;

public class InsureListResDTO implements Serializable {

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

    public static class ResultListBean implements Serializable {
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
}
