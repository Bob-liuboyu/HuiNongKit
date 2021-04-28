package com.project.common_resource.response;

import java.io.Serializable;
import java.util.List;

/**
 * @fileName: MeasureResponse
 * @author: liuboyu
 * @date: 2021/4/15 10:40 AM
 * @description: 测量返回结果
 */
public class MeasureResponse implements Serializable {
    public static final int CODE_SUCCESS = 1;
    public static final int CODE_WARNING = 2;
    public static final int CODE_ERROR = 0;
    private String id;            //Y	每头猪一个ID
    private int status;        //int	Y	1：成功；2：警告；0：异常；
    private float weight;        //float	Y
    private String msg;    //string		信息说明
    private float length;        //float	Y	耳根到尾根的长度
    private float area;    //float	Y	面积（平方厘米）
    private SimilarityEntity result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public SimilarityEntity getResult() {
        return result;
    }

    public void setResult(SimilarityEntity result) {
        this.result = result;
    }

    public static class SimilarityEntity implements Serializable {
        /**
         * res : 1：表示重复 0：表示不重复
         * similarity : ["3132:98%"]
         */
        private int res;
        private List<String> similarity;

        public int getRes() {
            return res;
        }

        public void setRes(int res) {
            this.res = res;
        }

        public List<String> getSimilarity() {
            return similarity;
        }

        public void setSimilarity(List<String> similarity) {
            this.similarity = similarity;
        }
    }
}
