package com.project.arch_repo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private static final String PRE_NAME = "config";
    private static SharedPreferences sp;

    /**
     * 设置 boolean 值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBooleanValue(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 返回 boolean 值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 设置 String 值
     *
     * @param context
     * @param key
     * @param
     */
    public static void setStringValue(Context context, String key, String Value) {
        if (sp == null) {
            sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, Value).commit();
    }

    /**
     * 返回 String 值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringValue(Context context, String key, String defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defaultValue);
    }

    /**
     * 设置Float值
     *
     * @param context
     * @param key
     * @param defaultValue
     */
    public static void setFloatValue(Context context, String key, float defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putFloat(key, defaultValue).commit();
    }

    /**
     * 返回Float值
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getFloatValue(Context context, String key, float defaultValue) {
        if (sp == null) {
            sp = context.getSharedPreferences(PRE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getFloat(key, defaultValue);
    }
}