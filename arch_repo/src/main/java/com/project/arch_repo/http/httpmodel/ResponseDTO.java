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

    public static int CODE_SUCCESS = 200;
    public static final int CODE_TOKEN_OVER_TIME = 200004;
    public static final int CODE_BODY_NULL = -1;


    private static final String FIELD_CODE = "code";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_RESULT = "data";
    private static final String FIELD_SUCCESS = "success";


    @SerializedName(value = FIELD_MESSAGE)
    public String message;

    @SerializedName(value = FIELD_CODE)
    public int code;

    @SerializedName(value = FIELD_SUCCESS)
    public boolean success;

    //注意 空
    @SerializedName(value = FIELD_RESULT)
    public T data;

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}
