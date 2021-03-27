package com.project.module_order.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.PixelCopy;

import com.project.arch_repo.utils.ImageMaskUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-27
 * @Description SurfaceView 拍照utils
 */
public class SurfaceCameraUtils {
    public static final String TAG = "SurfaceCameraUtils";

    public static Bitmap takePhoto(final Context context, final GLSurfaceView surfaceView, final Bitmap maskBitmap) {
        final String filename = generateFilename();

        /*ArSceneView view = fragment.getArSceneView();*/
        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(surfaceView.getWidth(), surfaceView.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        final Bitmap[] result = new Bitmap[1];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PixelCopy.request(surfaceView, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
                @Override
                public void onPixelCopyFinished(int copyResult) {
                    if (copyResult == PixelCopy.SUCCESS) {
                        try {
                            result[0] = ImageMaskUtil.createWaterMaskLeftTop(context, bitmap, maskBitmap, 100, 100);
                            saveBitmapToDisk(bitmap, filename);
                            return;
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                            return;
                        }
                    } else {
                        Log.e(TAG, "Failed to copyPixels: " + copyResult);
                    }
                    handlerThread.quitSafely();
                }
            }, new Handler(handlerThread.getLooper()));
        }
        return bitmap;
    }

    /**
     * 生成文件名
     *
     * @return
     */
    private static String generateFilename() {
        return Environment.getExternalStorageDirectory() + "/DCIM/HuiNongKit/"
                + System.currentTimeMillis() + ".jpg";
    }

    private static void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            Log.e(TAG, "saveBitmapToDisk = " + filename);
        } catch (IOException ex) {
            Log.e(TAG, "IOException = " + ex);
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }
}
