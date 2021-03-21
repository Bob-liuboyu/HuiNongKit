package com.project.common_resource;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-21
 * @Description
 */
public class OrderPhotoListModel implements Serializable {
    private String result;
    private List<PhotoModel> photos;

    public OrderPhotoListModel(String result, List<PhotoModel> photos) {
        this.result = result;
        this.photos = photos;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public List<PhotoModel> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoModel> photos) {
        this.photos = photos;
    }
}
