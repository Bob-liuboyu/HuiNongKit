package com.project.arch_repo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 获取设备唯一id
 */
public class AndroidUniqueIdUtils {

    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {

            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }


            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
        return deviceId.toString();

    }


    /**
     * 得到全局唯一UUID
     */
    private static String getUUID(Context context) {
        SharedPreferences mShare = context.getSharedPreferences("UUIDS_SharedPreferences", Context.MODE_PRIVATE);
        String uuid = mShare.getString("uuid", "");
        if (TextUtils.isEmpty(uuid)) {
            mShare.edit()
                    .putString("uuid", uuid = UUID.randomUUID().toString())
                    .commit();
        }
        return uuid;
    }
}

