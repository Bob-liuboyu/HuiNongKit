package com.project.common_resource;

import java.io.Serializable;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-26
 * @Description
 */
public class TakePhotoButtonItem implements Serializable {
    private String name;
    private boolean select;
    private String url;

    public TakePhotoButtonItem(String name, boolean select) {
        this.name = name;
        this.select = select;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
