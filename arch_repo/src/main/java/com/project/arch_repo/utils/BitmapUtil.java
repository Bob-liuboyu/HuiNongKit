//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.project.arch_repo.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build.VERSION;
import android.widget.ImageView.ScaleType;

import java.io.ByteArrayOutputStream;

public final class BitmapUtil {
    public BitmapUtil() {
    }

    public static byte[] compressionByte(byte[] buffer, int dstWidth, int dstHeight) {
        if (buffer != null && buffer.length != 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            Bitmap sendBitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
            bitmap.recycle();
            return bmpToByteArray(sendBitmap, true);
        } else {
            return null;
        }
    }

    public static byte[] bmpToByteArray(Bitmap bmp, boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();

        try {
            output.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return result;
    }

    public static Bitmap scale(Bitmap sourceBitmap, float targetWidth, float targetHeight, ScaleType scaleType, boolean recycleSource) {
        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
            Bitmap scaledBitmap = null;
            float sourceWidth = (float)sourceBitmap.getWidth();
            float sourceHeight = (float)sourceBitmap.getHeight();
            float sourceRatio = sourceWidth / sourceHeight;
            float targetRatio = targetWidth / targetHeight;
            float scale;
            if (scaleType == ScaleType.CENTER_CROP) {
                scale = sourceRatio > targetRatio ? targetHeight / sourceHeight : targetWidth / sourceWidth;
            } else {
                scale = sourceRatio < targetRatio ? targetHeight / sourceHeight : targetWidth / sourceWidth;
            }

            if (scale == 1.0F) {
                return sourceBitmap;
            } else {
                Matrix matrix = new Matrix();
                matrix.setScale(scale, scale);

                try {
                    scaledBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
                } catch (IllegalArgumentException var13) {
                } catch (OutOfMemoryError var14) {
                }

                if (recycleSource && sourceBitmap != scaledBitmap && sourceBitmap != null && VERSION.SDK_INT < 14) {
                    sourceBitmap.recycle();
                }

                return scaledBitmap;
            }
        } else {
            return null;
        }
    }
}
