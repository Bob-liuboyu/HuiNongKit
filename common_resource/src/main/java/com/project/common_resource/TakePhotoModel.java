package com.project.common_resource;

import android.graphics.Bitmap;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-28
 * @Description
 */
public class TakePhotoModel {
    private Bitmap mBitmap;
    private String path;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
