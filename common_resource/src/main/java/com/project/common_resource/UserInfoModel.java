package com.project.common_resource;

import java.io.Serializable;

/**
 * @fileName: UserInfoModel
 * @author: liuboyu
 * @date: 2021/3/13 2:58 PM
 * @description:
 */
public class UserInfoModel implements Serializable {
    private String id;
    private String name;
    private String company;
    private String title;
    private String avatar;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
