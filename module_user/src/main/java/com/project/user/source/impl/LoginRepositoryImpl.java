package com.project.user.source.impl;

import com.google.gson.JsonObject;
import com.project.arch_repo.function.ResponseDTOSimpleFunction;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.common_resource.response.LoginResDTO;
import com.project.user.source.ILoginDataSource;
import com.project.user.api.UserApiService;
import com.xxf.arch.XXF;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 登陆仓库
 */
public class LoginRepositoryImpl implements ILoginDataSource {
    private static ILoginDataSource INSTANCE;

    public static ILoginDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (LoginRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoginRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }

    private LoginRepositoryImpl() {
    }

    @Override
    public Observable<LoginResDTO> login(String phone, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("password", password);
        return XXF.getApiService(UserApiService.class)
                .login(jsonObject)
                .map(new ResponseDTOSimpleFunction<LoginResDTO>());
    }

    @Override
    public Observable<Boolean> updatePwd(String phone, String password, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("token", token);
        return XXF.getApiService(UserApiService.class)
                .updatePwd(jsonObject)
                .map(new Function<ResponseDTO, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull ResponseDTO responseDTO) throws Exception {
                        return responseDTO.success;
                    }
                });
    }
}
