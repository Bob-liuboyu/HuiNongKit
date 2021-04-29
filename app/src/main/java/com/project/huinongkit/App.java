package com.project.huinongkit;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.pgyersdk.crash.PgyCrashManager;
import com.project.arch_repo.utils.ProjectInit;
import com.tencent.bugly.crashreport.CrashReport;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * FileName: App
 * Author: liuboyu
 * Email: 545777678@qq.com
 * Date: 2019-10-30 17:15
 * Description:
 */
public class App extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this) ;
        closeAndroidPDialog();
        //组件化初始化
        ProjectInit.getInstance().init(this);
        // bugly 初始化配置
        CrashReport.initCrashReport(getApplicationContext(), "66be8446b7", false);

        // 蒲公英
        PgyCrashManager.register();
    }

    /**
     * 适配P
     */
    private static void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}