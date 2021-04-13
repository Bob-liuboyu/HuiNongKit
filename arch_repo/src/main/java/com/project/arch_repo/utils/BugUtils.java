package com.project.arch_repo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 异常反馈工具类
 */
public class BugUtils {

    public interface UserInfoCallback {

        /**
         * 登陆信息
         *
         * @return
         * @throws Exception
         */
        String getUserInfo() throws Exception;
    }

    public static void init(@NonNull Context context, @NonNull final UserInfoCallback loginInfoCallback) {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context.getApplicationContext());
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
                                            @Override
                                            public synchronized Map<String, String> onCrashHandleStart(int i, String s, String s1, String s2) {
                                                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                                                if (loginInfoCallback != null) {
                                                    try {
                                                        map.put("userInfo", loginInfoCallback.getUserInfo());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                return map;
                                            }
                                        }
        );
    }

    private BugUtils() {
    }


    public static void feedException(String tag, Throwable e) {
        try {
            CrashReport.postCatchedException(new RuntimeException(String.format("%s_\n_%s", tag, Log.getStackTraceString(e))));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void feedLog(String tag, String log) {
        try {
            CrashReport.postCatchedException(new RuntimeException(String.format("%s_\n_%s", tag, log)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
