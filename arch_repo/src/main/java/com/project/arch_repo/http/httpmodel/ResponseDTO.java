package com.project.arch_repo.http.httpmodel;

import com.google.gson.annotations.SerializedName;


/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-06-01 16:59
 * <p>
 * 不要轻易修改
 * 不要轻易修改
 */

public class ResponseDTO<T> {
    private static final String FIELD_CODE = "error";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_RESULT = "data";


    @SerializedName(value = FIELD_MESSAGE)
    public String message;

    @SerializedName(value = FIELD_CODE)
    public int error;

    //注意 空
    @SerializedName(value = FIELD_RESULT)
    public T data;

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "message='" + message + '\'' +
                ", error=" + error +
                ", data=" + data +
                '}';
    }
}
