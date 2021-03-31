package com.project.user.source;

import com.project.common_resource.response.LoginResDTO;

import io.reactivex.Observable;

public interface ILoginDataSource {
    /**
     * 登陆
     *
     * @param phone
     * @param password
     * @return
     */
    Observable<LoginResDTO> login(String phone, String password);

    /**
     * 修改密码
     *
     * @param phone
     * @param password
     * @return
     */
    Observable<Boolean> updatePwd(String phone, String password, String token);
}