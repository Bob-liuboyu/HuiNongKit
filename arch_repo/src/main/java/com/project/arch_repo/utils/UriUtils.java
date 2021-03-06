package com.project.arch_repo.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import static android.content.Context.STORAGE_SERVICE;

/**
 * Description
 * Company Beijing icourt
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：2017/4/8
 * version 1.0.0
 */
public class UriUtils {

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     * 注意先 获取文件读写权限
     */
    public static String getPath(final Context context, final Uri uri) {
        try {
            // DocumentProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                    && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    } else {// handle non-primary volumes
                        String sdCardPath = getExternalSdCardPath(context);
                        if (sdCardPath != null) {
                            return sdCardPath + "/" + split[1];
                        }
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                try {
                    return getDataColumn(context, uri, null, null);
                } catch (Exception e) {
                    return uri.toString();
                }
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.MediaColumns.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    /**
     * 获取android 7.0文件共享目录下文件描述
     * 用完之后 请close
     *
     * @param context
     * @param uri
     */
    public static ParcelFileDescriptor get_N_FileDescriptor(Context context, Uri uri)
            throws FileNotFoundException {
        return context.getContentResolver().openFileDescriptor(uri, "r");
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 获取外部SD卡的路径
     *
     * @param context
     * @return 获取成功返回外部SD卡的路径，否则返回null
     */
    public static String getExternalSdCardPath(Context context) {
        if (context == null) {
            return null;
        }
        try {
            StorageManager sm = (StorageManager) context.getSystemService(STORAGE_SERVICE);
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", new Class[0]);
            String[] paths = (String[]) getVolumePathsMethod.invoke(sm, new Object[0]);
            return (paths == null || paths.length <= 1) ? null : paths[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 转换id->uri
     *
     * @param resId
     * @param context
     * @return
     */
    public static final Uri getResourceUri(@IdRes int resId, @NonNull Context context) {
        if (context == null) {
            return Uri.parse("");
        }
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
    }

    /**
     * 转换id->uri
     *
     * @param resId
     * @param context
     * @return
     */
    public static final String getResourceUriString(@AnyRes int resId, @NonNull Context context) {
        return getResourceUri(resId, context).toString();
    }
}
