package com.project.arch_repo.permissions;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xxf.arch.core.permission.RxPermissionTransformer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 通讯录权限提示
 */
public class ReadContactPermissionTransformer extends RxPermissionTransformer {
    public ReadContactPermissionTransformer(@NonNull Context context, boolean rejecctJumpPermissionSetting) {
        super(context, "我们需要读取通讯录权限!", rejecctJumpPermissionSetting);
    }
}
