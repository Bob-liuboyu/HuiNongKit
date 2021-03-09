package com.project.arch_repo.permissions;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xxf.arch.core.permission.RxPermissionTransformer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 拨打电话权限提示
 */
public class CallPhonePermissionTransformer extends RxPermissionTransformer {
    public CallPhonePermissionTransformer(@NonNull Context context, boolean rejecctJumpPermissionSetting) {
        super(context, "我们需要拨打电话的权限!", rejecctJumpPermissionSetting);
    }
}
