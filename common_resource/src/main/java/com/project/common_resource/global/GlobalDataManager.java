package com.project.common_resource.global;

import com.project.common_resource.response.LoginResDTO;

/**
 * @fileName: GlobalDataManager
 * @author: liuboyu
 * @date: 2021/3/31 11:07 AM
 * @description: 全局数据
 */
public class GlobalDataManager {

    private LoginResDTO.SettingsBean settings;
    private LoginResDTO.UserInfoBean userInfo;
    private String token;

    private GlobalDataManager() {
    }

    public static GlobalDataManager getInstance() {
        return Inner.instance;
    }

    private static class Inner {
        private static final GlobalDataManager instance = new GlobalDataManager();
    }

    public void updateUserInfo(LoginResDTO.UserInfoBean userinfo) {
        this.userInfo = userinfo;
    }

    public void updateUserToken(String token) {
        this.token = token;
    }

    public void updateSettings(LoginResDTO.SettingsBean settings) {
        this.settings = settings;
    }

    public LoginResDTO.SettingsBean getSettings() {
        return settings;
    }

    public void updateInfo(LoginResDTO resp) {
        if (resp == null) {
            updateSettings(null);
            updateUserInfo(null);
            updateUserToken(null);
            return;
        }
        updateSettings(resp.getSettings());
        updateUserInfo(resp.getUserInfo());
        updateUserToken(resp.getToken());
    }

    public LoginResDTO.UserInfoBean getUserInfo() {
        return userInfo;
    }

    public String getToken() {
        return token;
    }
}
