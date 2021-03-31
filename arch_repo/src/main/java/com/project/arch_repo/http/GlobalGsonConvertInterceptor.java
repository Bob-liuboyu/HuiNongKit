package com.project.arch_repo.http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.project.arch_repo.annotation.RuntimeRouter;
import com.project.arch_repo.http.httpmodel.ResponseDTO;
import com.project.config_repo.ArouterConfig;
import com.xxf.arch.XXF;
import com.xxf.arch.http.ResponseException;
import com.xxf.arch.http.converter.gson.GsonConvertInterceptor;

import java.io.IOException;

import okhttp3.ResponseBody;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 全局处理转换结果
 */
public class GlobalGsonConvertInterceptor implements GsonConvertInterceptor {
    public GlobalGsonConvertInterceptor() {

    }

    @SuppressLint("WrongConstant")
    @Override
    public <T> T onResponseBodyIntercept(Gson gson, TypeAdapter<T> adapter, ResponseBody value, T convertedBody) throws IOException {
        if (convertedBody != null
                && convertedBody instanceof ResponseDTO) {
            ResponseDTO responseDTO = (ResponseDTO) convertedBody;
            //token过期
            if (responseDTO.code == ResponseDTO.CODE_TOKEN_OVER_TIME) {
                synchronized (GlobalGsonConvertInterceptor.class) {
                    try {
                        Activity topActivity = XXF.getActivityStackProvider().getTopActivity();
                        String routerPath = null;
                        if (topActivity.getClass().isAnnotationPresent(RuntimeRouter.class)) {
                            routerPath = topActivity.getClass().getAnnotation(RuntimeRouter.class).path();
                        }
                        if (!TextUtils.equals(routerPath, ArouterConfig.User.LOGIN)) {
                            //登陆activity singletop 重置堆栈
                            ARouter.getInstance().build(ArouterConfig.User.LOGIN)
                                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .navigation();
                            topActivity.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (responseDTO.code != ResponseDTO.CODE_SUCCESS) {
                throw new ResponseException(responseDTO.code, responseDTO.message);
            } else if (responseDTO.data == null) {
                throw new ResponseException(ResponseDTO.CODE_BODY_NULL, "返回data字段null");
            }
        }
        return convertedBody;
    }
}
