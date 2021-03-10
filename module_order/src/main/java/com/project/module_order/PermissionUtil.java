package com.project.module_order;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PermissionUtil {
    private static PermissionUtil ourInstance = new PermissionUtil();
    public static int UNCHECK = 0;
    public static int CHECK_HAS_PERMISSION = 1;
    public static int CHECK_NO_PERMISSION = 2;
    private static boolean useBlackList = true;
    private List<String> recordBlackList = Arrays.asList("Xiaomi");

    public static final String[] storagePermissions = new String[] {
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static PermissionUtil getInstance() {
        return ourInstance;
    }

    private PermissionUtil() {
    }

    /**
     * 检查是否已经获取到指定权限
     *
     * @param context
     * @param permission
     * @return
     */
    public boolean checkSelfPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(context, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 检查是否获取到指定权限列表里的权限
     *
     * @param context
     * @param permissions
     * @return 未获取的权限列表
     */
    public List<String> checkSelfPermission(Context context, String[] permissions) {
        // NOTE Android M 以下系统避免ContextCompat兼容问题，直接给权限通过。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return null;
        }

        List<String> needGrantPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            boolean hasPermission = checkSelfPermission(context, permissions[i]);
            if (!hasPermission) {
                needGrantPermissionList.add(permissions[i]);
            }
        }

        return needGrantPermissionList;
    }

    /**
     * 是否需要展示权限获取说明
     *
     * @param context
     * @param permission
     * @return 第一次请求或者用户关闭权限时返回true
     */
    public boolean shouldShowRequestPermissionRetionale(Activity context, String permission) {
        boolean isNeedShow = ActivityCompat.shouldShowRequestPermissionRationale(context, permission);

        return isNeedShow;
    }

    /**
     * 请求指定权限列表
     *
     * @param context
     * @param permissions 权限列表
     * @param requestCode
     */
    public void requestPermission(Activity context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(context, permissions, requestCode);
    }

    /**
     * 请求指定单个权限
     *
     * @param context
     * @param permission
     * @param requestCode
     */
    public void requestPermission(Activity context, String permission, int requestCode) {
        requestPermission(context, new String[]{permission}, requestCode);
    }

    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return returns true if all permissions have been granted.
     */
    public boolean verifyPermissions(int... grantResults) {
        if (grantResults == null || grantResults.length == 0) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public int recordPermissionState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return UNCHECK;
        } else {
            if (useBlackList) {
                String brand = Build.BRAND;
                if (!recordBlackList.contains(brand)) {
                    return UNCHECK;
                }
            }
            AudioRecord record = null;
            try {
                record = new AudioRecord(MediaRecorder.AudioSource.MIC, 22050,
                        AudioFormat.CHANNEL_CONFIGURATION_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        AudioRecord.getMinBufferSize(22050,
                                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                AudioFormat.ENCODING_PCM_16BIT));
                record.startRecording();
                int recordingState = record.getRecordingState();
                if (recordingState == AudioRecord.RECORDSTATE_STOPPED) {
                    return CHECK_NO_PERMISSION;
                }
                return CHECK_HAS_PERMISSION;
            } catch (Exception e) {
                return CHECK_NO_PERMISSION;
            } finally {
                if (record != null) {
                    try {
                        record.release();
                    } catch (Exception ee) {
                    }
                }
            }
        }
    }
}