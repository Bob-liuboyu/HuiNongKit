package com.project.common_resource.response;

import java.io.Serializable;

/**
 * @fileName: MeasureResponse
 * @author: liuboyu
 * @date: 2021/4/15 10:40 AM
 * @description: 测量返回结果
 */
public class MeasureResponse2 implements Serializable {
    private int code;
    private boolean success;
    private String message;
    private ResultEntry data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultEntry getData() {
        return data;
    }

    public void setData(ResultEntry data) {
        this.data = data;
    }

    public static class ResultEntry{
        private int pigId;
        private MeasureResponse results;

        public int getPigId() {
            return pigId;
        }

        public void setPigId(int pigId) {
            this.pigId = pigId;
        }

        public MeasureResponse getResults() {
            return results;
        }

        public void setResults(MeasureResponse results) {
            this.results = results;
        }
    }
}
