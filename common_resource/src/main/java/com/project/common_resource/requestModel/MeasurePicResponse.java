package com.project.common_resource.requestModel;

import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * @fileName: MeasurePicResponse
 * @author: liuboyu
 * @date: 2021/4/15 10:50 AM
 * @description:
 */
public class MeasurePicResponse implements Serializable {
    private String DepthImage;//	string	Y	图像深度数据
    private String Image;//		Strng		RGB图像数据，base64格式
    private JsonObject scope_dir;//		{id:path;id2:path}		后台通过时间与距离，获取这个scope，比如7天内，10KM范围内
    private String baodan;//	保单号	string	Y
    private long id;//			Y	每头猪一个ID
    private String longitude;//			Y	经度
    private String latitude;//		Y	纬度

    public String getDepthImage() {
        return DepthImage;
    }

    public void setDepthImage(String depthImage) {
        DepthImage = depthImage;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public JsonObject getScope_dir() {
        return scope_dir;
    }

    public void setScope_dir(JsonObject scope_dir) {
        this.scope_dir = scope_dir;
    }

    public String getBaodan() {
        return baodan;
    }

    public void setBaodan(String baodan) {
        this.baodan = baodan;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
