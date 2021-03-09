//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.project.arch_repo.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

public class ImageUtils {
    private static final int MAX_SIZE = 400;

    public static File getTempImageDir(Context context) {
        File file = new File(new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                .append(File.separator)
                .append(context.getPackageName())
                .append(File.separator)
                .append("temp")
                .toString());
        if (!file.exists()) {
            file.mkdirs();
        } else if (!file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }
        return file;
    }

    public ImageUtils() {
    }

    public static Bitmap createBitmap(Bitmap src, int x, int y, int width, int height, Matrix m, boolean filter) {
        Bitmap bitmap = null;
        if (src == null) {
            return bitmap;
        } else {
            try {
                bitmap = Bitmap.createBitmap(src, x, y, width, height, m, filter);
            } catch (IllegalArgumentException var9) {
                var9.printStackTrace();
            } catch (OutOfMemoryError var10) {
                var10.printStackTrace();
            }

            return bitmap;
        }
    }

    public static Bitmap createBitmap(String filename) {
        return createBitmap((String) filename, 400);
    }

    public static Bitmap createBitmap(Context context, String filename, int maxSize) {
        if (context != null && !TextUtils.isEmpty(filename)) {
            filename = context.getFilesDir() + File.separator + filename;
            return createBitmap(filename, maxSize);
        } else {
            return null;
        }
    }

    public static Bitmap createBitmap(String filename, int maxSize) {
        if (TextUtils.isEmpty(filename)) {
            return null;
        } else {
            Bitmap bitmap = null;

            try {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filename, options);
                options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight) / (maxSize < 1 ? 400 : maxSize);
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Config.RGB_565;
                bitmap = BitmapFactory.decodeFile(filename, options);
            } catch (OutOfMemoryError var4) {
                var4.printStackTrace();
            }

            return bitmap;
        }
    }

    public static Bitmap createBitmapFromFile(Context context, String filename) {
        if (context != null && !TextUtils.isEmpty(filename)) {
            Bitmap bitmap = null;
            FileInputStream is = null;

            try {
                is = context.openFileInput(filename);
                bitmap = createBitmap((InputStream) is);
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception var12) {
                    var12.printStackTrace();
                }

            }

            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap createBitmapFromAsset(Context context, String filename) {
        if (context != null && !TextUtils.isEmpty(filename)) {
            Bitmap bitmap = null;
            InputStream is = null;

            try {
                is = context.getAssets().open(filename);
                bitmap = createBitmap(is);
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception var12) {
                    var12.printStackTrace();
                }

            }

            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap createBitmap(int width, int height, Config config) {
        if (width > 0 && height > 0) {
            Bitmap bitmap = null;

            try {
                bitmap = Bitmap.createBitmap(width, height, config);
            } catch (OutOfMemoryError var5) {
                var5.printStackTrace();
            }

            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap createBitmap(InputStream is) {
        if (is == null) {
            return null;
        } else {
            Bitmap bitmap = null;

            try {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, (Rect) null, options);
                options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight) / 400;
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Config.RGB_565;
                bitmap = BitmapFactory.decodeStream(is, (Rect) null, options);
            } catch (OutOfMemoryError var3) {
                var3.printStackTrace();
            }

            return bitmap;
        }
    }

    public static Bitmap createBitmap(InputStream is, Rect outPadding, Options opts) {
        if (is == null) {
            return null;
        } else {
            Bitmap bitmap = null;

            try {
                bitmap = BitmapFactory.decodeStream(is, outPadding, opts);
            } catch (OutOfMemoryError var5) {
                var5.printStackTrace();
            }

            return bitmap;
        }
    }

    public static Bitmap createBitmap(Context context, int resId) {
        Bitmap bitmap = null;
        if (context == null) {
            return bitmap;
        } else {
            try {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(context.getResources(), resId, options);
                options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight) / 400;
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Config.RGB_565;
                bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
            } catch (OutOfMemoryError var4) {
                var4.printStackTrace();
            }

            return bitmap;
        }
    }

    public static Bitmap createBitmap(Context context, Uri uri, int maxSize) {
        if (context != null && uri != null) {
            ContentResolver res = context.getContentResolver();
            ParcelFileDescriptor parcelFileDescriptor = null;
            FileDescriptor fd = null;

            try {
                parcelFileDescriptor = res.openFileDescriptor(uri, "r");
                fd = parcelFileDescriptor.getFileDescriptor();
            } catch (FileNotFoundException var15) {
                var15.printStackTrace();
            } finally {
                try {
                    if (parcelFileDescriptor != null) {
                        parcelFileDescriptor.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

            }

            if (fd == null) {
                return null;
            } else {
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(fd, (Rect) null, options);
                options.inSampleSize = (options.outWidth > options.outHeight ? options.outWidth : options.outHeight) / maxSize;
                options.inJustDecodeBounds = false;
                options.inPreferredConfig = Config.RGB_565;
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, (Rect) null, options);
                if (bitmap != null && (bitmap.getWidth() > options.outWidth || bitmap.getHeight() > options.outHeight)) {
                    Bitmap tmp = Bitmap.createScaledBitmap(bitmap, options.outWidth, options.outHeight, true);
                    if (tmp != null && tmp != bitmap) {
                        clear(bitmap);
                    }

                    if (tmp != null) {
                        bitmap = tmp;
                    }
                }

                return bitmap;
            }
        } else {
            return null;
        }
    }

    public static Bitmap createBitmapDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else {
            Bitmap bitmap = null;

            try {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
            } catch (OutOfMemoryError var3) {
                var3.printStackTrace();
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int specifiedWidth, int specifiedHeight) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        if (originWidth < specifiedWidth && originHeight < specifiedHeight) {
            return bitmap;
        } else {
            int newWidth = originWidth;
            int newHeight = originHeight;
            if (originWidth > specifiedWidth) {
                newWidth = specifiedWidth;
                double i = (double) originWidth * 1.0D / (double) specifiedWidth;
                newHeight = (int) Math.floor((double) originHeight / i);
                bitmap = Bitmap.createScaledBitmap(bitmap, specifiedWidth, newHeight, true);
            }

            if (newHeight > specifiedHeight) {
                int startPointY = (int) ((double) (originHeight - specifiedHeight) / 2.0D);
                bitmap = Bitmap.createBitmap(bitmap, 0, startPointY, newWidth, specifiedHeight);
            }

            return bitmap;
        }
    }

    public static Bitmap resizeBitmap4Sys(Bitmap bitmap, int width, int height) {
        if (width != 0 && height != 0) {
            Bitmap resizedBitmap = null;

            try {
                resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            } catch (OutOfMemoryError var5) {
                var5.printStackTrace();
            }

            if (resizedBitmap == null) {
                return null;
            } else {
                clear(bitmap);
                return resizedBitmap;
            }
        } else {
            return null;
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height, int radius, boolean needCrop, boolean needScale, boolean recycleSource) {
        if (bitmap == null) {
            return null;
        } else {
            if (needCrop && needScale || radius > 0 && (width > 0 || height > 0)) {
                bitmap = cropWidthScale(bitmap, width, height, recycleSource);
            } else if (needScale) {
                bitmap = scale(bitmap, (float) width, (float) height, ScaleType.CENTER_INSIDE, recycleSource);
            } else if (needCrop) {
                bitmap = crop(bitmap, width, height, recycleSource);
            }

            if (radius > 0) {
                bitmap = round(bitmap, width, height, radius, recycleSource);
            }

            return bitmap;
        }
    }

    public static Bitmap cropWithScale(String bitmapPath, int width, int height) {
        Bitmap bitmap = createBitmap(bitmapPath);
        bitmap = scale(bitmap, (float) width, (float) height, ScaleType.CENTER_CROP, true);
        return crop(bitmap, width, height, true);
    }

    public static Bitmap cropWidthScale(Bitmap bitmap, int width, int height, boolean recycleSource) {
        bitmap = scale(bitmap, (float) width, (float) height, ScaleType.CENTER_CROP, recycleSource);
        return crop(bitmap, width, height, true);
    }

    private static Bitmap crop(Bitmap sourceBitmap, int targetWidth, int targetHeight, boolean recycleSource) {
        if (sourceBitmap == null) {
            return null;
        } else {
            Bitmap croppedBitmap = null;
            int xDiff = Math.max(0, sourceBitmap.getWidth() - targetWidth);
            int yDiff = Math.max(0, sourceBitmap.getHeight() - targetHeight);
            int x = xDiff / 2;
            int y = yDiff / 2;

            try {
                croppedBitmap = Bitmap.createBitmap(sourceBitmap, x, y, targetWidth, targetHeight);
            } catch (OutOfMemoryError var10) {
                var10.printStackTrace();
            }

            if (recycleSource && sourceBitmap != croppedBitmap) {
                clear(sourceBitmap);
            }

            return croppedBitmap;
        }
    }

    public static Bitmap scale(Bitmap sourceBitmap, float targetWidth, float targetHeight, ScaleType scaleType, boolean recycleSource) {
        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
            Bitmap scaledBitmap = null;
            float sourceWidth = (float) sourceBitmap.getWidth();
            float sourceHeight = (float) sourceBitmap.getHeight();
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
                    var13.printStackTrace();
                } catch (OutOfMemoryError var14) {
                    var14.printStackTrace();
                }

                if (recycleSource && sourceBitmap != scaledBitmap) {
                    clear(sourceBitmap);
                }

                return scaledBitmap;
            }
        } else {
            return null;
        }
    }

    public static Bitmap round(Bitmap bitmap, int radius, boolean recycleSource) {
        return radius > 0 && bitmap != null ? round(bitmap, bitmap.getWidth(), bitmap.getHeight(), radius, recycleSource) : bitmap;
    }

    public static Bitmap round(Bitmap bitmap, int width, int height, int radius, boolean recycleSource) {
        if (width != 0 && height != 0 && radius > 0 && bitmap != null) {
            Bitmap ret = null;

            try {
                ret = Bitmap.createBitmap(width, height, Config.RGB_565);
            } catch (OutOfMemoryError var10) {
                var10.printStackTrace();
            }

            if (ret == null) {
                return null;
            } else {
                Canvas canvas = new Canvas(ret);
                Paint paint = new Paint();
                Rect rect = new Rect(0, 0, width, height);
                RectF rectF = new RectF(rect);
                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(-12434878);
                canvas.drawRoundRect(rectF, (float) radius, (float) radius, paint);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);
                if (recycleSource) {
                    clear(bitmap);
                }

                return ret;
            }
        } else {
            return bitmap;
        }
    }

    public static Bitmap decorate(Bitmap bitmap, int[] dots) {
        if (bitmap != null && dots != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            try {
                if (dots.length != w * h) {
                    return null;
                }

                if (dots.length > 0) {
                    bitmap.setPixels(dots, 0, w, 0, 0, w, h);
                    return bitmap;
                }
            } catch (OutOfMemoryError var5) {
                var5.printStackTrace();
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            return null;
        } else {
            return null;
        }
    }

    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = -16777216;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int grey = pixels[width * i + j];
                int red = (grey & 16711680) >> 16;
                int green = (grey & '\uff00') >> 8;
                int blue = grey & 255;
                grey = (int) ((double) red * 0.3D + (double) green * 0.59D + (double) blue * 0.11D);
                grey |= alpha | grey << 16 | grey << 8;
                pixels[width * i + j] = grey;
            }
        }

        Bitmap newBmp = Bitmap.createBitmap(width, height, Config.RGB_565);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
        return resizeBmp;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int height = bmpOriginal.getHeight();
        int width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0F);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0.0F, 0.0F, paint);
        return bmpGrayscale;
    }

    public static Bitmap getRCB(Bitmap bitmap, float roundPX) {
        Bitmap dstbmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(dstbmp);
        int color = -12434878;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        paint.setFilterBitmap(true);
        canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return dstbmp;
    }

    public static void refresh(ImageView iv, int imgTagId, Bitmap bitmap, Bitmap defaultBitmap) {
        if (iv != null) {
            Bitmap oldBitmap = (Bitmap) iv.getTag(imgTagId);
            if (oldBitmap != bitmap) {
                clear(oldBitmap);
            }

            iv.setImageBitmap(bitmap == null ? defaultBitmap : bitmap);
            iv.setTag(imgTagId, bitmap);
        }
    }

    public static int refresh(ImageView iv, int imgTagId, Bitmap bitmap, int resId) {
        if (iv == null) {
            return 0;
        } else {
            int hashCode = 0;
            Bitmap oldBitmap = (Bitmap) iv.getTag(imgTagId);
            if (oldBitmap != null && oldBitmap != bitmap) {
                hashCode = oldBitmap.hashCode();
                clear(oldBitmap);
            }

            if (bitmap == null) {
                iv.setImageResource(resId);
            } else {
                iv.setImageBitmap(bitmap);
            }

            iv.setTag(imgTagId, bitmap);
            return hashCode;
        }
    }

    public static int[] getIntArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            int[] pix = null;
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            try {
                pix = new int[w * h];
                bitmap.getPixels(pix, 0, w, 0, 0, w, h);
            } catch (OutOfMemoryError var5) {
                var5.printStackTrace();
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            return pix;
        }
    }

    public static byte[] toBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();

            try {
                baos.close();
            } catch (IOException var4) {
                var4.printStackTrace();
            }

            return bytes;
        }
    }

    public static String getImageNameByTime() {
        Calendar calendar = Calendar.getInstance();
        return "IMG_" + String.valueOf(calendar.get(1)) + calendar.get(2) + calendar.get(5) + "_" + calendar.get(11) + calendar.get(12) + calendar.get(13) + ".jpg";
    }

    public static String getImageNameFromUrl(String url, String postfix) {
        return getImageNameFromUrl(url, postfix, (String) null);
    }

    public static String getImageNameFromUrl(String url, String postfix, String ignoreTag) {
        if (TextUtils.isEmpty(url) || !TextUtils.isEmpty(ignoreTag) && url.startsWith(ignoreTag)) {
            return url;
        } else {
            int lastIndex = url.lastIndexOf(postfix);
            if (lastIndex < 0) {
                lastIndex = url.length() - 1;
            }

            int beginIndex = url.lastIndexOf("/") + 1;
            int slashIndex = url.lastIndexOf("%2F") + 3;
            int finalSlashIndex = url.lastIndexOf("%252F") + 5;
            beginIndex = Math.max(Math.max(beginIndex, slashIndex), finalSlashIndex);
            return beginIndex >= lastIndex ? null : url.substring(beginIndex, lastIndex);
        }
    }

    public static Bitmap copy(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled() ? bitmap.copy(bitmap.getConfig(), bitmap.isMutable()) : null;
    }

    public static Uri saveToMediaStore(ContentResolver resolver, String title, Location location, int orientation, byte[] jpeg, String path) {
        ContentValues values = new ContentValues(9);
        values.put("title", title);
        values.put("_display_name", title + ".jpg");
        Date d = new Date();
        values.put("datetaken", d.getDate());
        values.put("mime_type", "image/jpeg");
        values.put("orientation", orientation);
        values.put("_data", path);
        values.put("_size", jpeg.length);
        if (location != null) {
            values.put("latitude", location.getLatitude());
            values.put("longitude", location.getLongitude());
        }

        Uri uri = resolver.insert(Media.EXTERNAL_CONTENT_URI, values);
        return uri == null ? null : uri;
    }

    public static BitmapDrawable decodeWithOOMHandling(Resources res, String imagePath) {
        BitmapDrawable result = null;
        if (TextUtils.isEmpty(imagePath)) {
            return result;
        } else {
            try {
                result = new BitmapDrawable(res, imagePath);
            } catch (OutOfMemoryError var4) {
                var4.printStackTrace();
                System.gc();
                SystemClock.sleep(1000L);
                System.gc();
            }

            return result;
        }
    }

    public static Bitmap decode(String imgBase64) throws IOException {
        try {
            byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            for (roundedSize = 1; roundedSize < initialSize; roundedSize <<= 1) {
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {
        double w = (double) options.outWidth;
        double h = (double) options.outHeight;
        int lowerBound = maxNumOfPixels == -1 ? 1 : (int) Math.ceil(Math.sqrt(w * h / (double) maxNumOfPixels));
        int upperBound = minSideLength == -1 ? 150 : (int) Math.min(Math.floor(w / (double) minSideLength), Math.floor(h / (double) minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        } else if (maxNumOfPixels == -1 && minSideLength == -1) {
            return 1;
        } else {
            return minSideLength == -1 ? lowerBound : upperBound;
        }
    }

    protected static String getAbsoluteImagePath(Uri uri, Activity activity) {
        String[] proj = new String[]{"_data"};
        Cursor cursor = activity.managedQuery(uri, proj, (String) null, (String[]) null, (String) null);
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String bitmap2String(Bitmap bitmap) throws Exception {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, 0);
        return string;
    }

    public static String writeToFile(Bitmap bitmap, String path, int quality, boolean recycleSource) {
        if (bitmap == null) {
            return null;
        } else {
            try {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File f = new File(path);
                if (f.exists()) {
                    f.delete();
                }

                if (f.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(f);
                    bitmap.compress(CompressFormat.JPEG, quality, fos);
                    fos.flush();
                    fos.close();
                }
            } catch (FileNotFoundException var7) {
                var7.printStackTrace();
                return null;
            } catch (IOException var8) {
                var8.printStackTrace();
            }

            if (recycleSource) {
                clear(bitmap);
            }

            return path;
        }
    }

    public static void saveBmp(Bitmap bitmap, String fileName) {
        if (bitmap != null) {
            int nBmpWidth = bitmap.getWidth();
            int nBmpHeight = bitmap.getHeight();
            int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

            try {
                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileOutputStream fileos = new FileOutputStream(fileName);
                int bfType = 19778;
                long bfSize = (long) (54 + bufferSize);
                int bfReserved1 = 0;
                int bfReserved2 = 0;
                long bfOffBits = 54L;
                writeWord(fileos, bfType);
                writeDword(fileos, bfSize);
                writeWord(fileos, bfReserved1);
                writeWord(fileos, bfReserved2);
                writeDword(fileos, bfOffBits);
                long biSize = 40L;
                long biWidth = (long) nBmpWidth;
                long biHeight = (long) nBmpHeight;
                int biPlanes = 1;
                int biBitCount = 24;
                long biCompression = 0L;
                long biSizeImage = 0L;
                long biXpelsPerMeter = 0L;
                long biYPelsPerMeter = 0L;
                long biClrUsed = 0L;
                long biClrImportant = 0L;
                writeDword(fileos, biSize);
                writeLong(fileos, biWidth);
                writeLong(fileos, biHeight);
                writeWord(fileos, biPlanes);
                writeWord(fileos, biBitCount);
                writeDword(fileos, biCompression);
                writeDword(fileos, biSizeImage);
                writeLong(fileos, biXpelsPerMeter);
                writeLong(fileos, biYPelsPerMeter);
                writeDword(fileos, biClrUsed);
                writeDword(fileos, biClrImportant);
                byte[] bmpData = new byte[bufferSize];
                int wWidth = nBmpWidth * 3 + nBmpWidth % 4;
                int nCol = 0;

                for (int nRealCol = nBmpHeight - 1; nCol < nBmpHeight; --nRealCol) {
                    int wRow = 0;

                    for (int wByteIdex = 0; wRow < nBmpWidth; wByteIdex += 3) {
                        int clr = bitmap.getPixel(wRow, nCol);
                        bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
                        bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color.green(clr);
                        bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color.red(clr);
                        ++wRow;
                    }

                    ++nCol;
                }

                fileos.write(bmpData);
                fileos.flush();
                fileos.close();
            } catch (FileNotFoundException var41) {
                var41.printStackTrace();
            } catch (IOException var42) {
                var42.printStackTrace();
            }

        }
    }

    protected static void writeWord(FileOutputStream stream, int value) throws IOException {
        byte[] b = new byte[]{(byte) (value & 255), (byte) (value >> 8 & 255)};
        stream.write(b);
    }

    protected static void writeDword(FileOutputStream stream, long value) throws IOException {
        byte[] b = new byte[]{(byte) ((int) (value & 255L)), (byte) ((int) (value >> 8 & 255L)), (byte) ((int) (value >> 16 & 255L)), (byte) ((int) (value >> 24 & 255L))};
        stream.write(b);
    }

    protected static void writeLong(FileOutputStream stream, long value) throws IOException {
        byte[] b = new byte[]{(byte) ((int) (value & 255L)), (byte) ((int) (value >> 8 & 255L)), (byte) ((int) (value >> 16 & 255L)), (byte) ((int) (value >> 24 & 255L))};
        stream.write(b);
    }

    public static void clear(Bitmap bitmap) {
        if (bitmap != null && VERSION.SDK_INT < 14) {
            bitmap.recycle();
        }

    }

    public static Bitmap getViewBitmap(View view) {
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        } else {
            Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
            view.destroyDrawingCache();
            view.setWillNotCacheDrawing(willNotCache);
            view.setDrawingCacheBackgroundColor(color);
            return bitmap;
        }
    }

    public void transImage(String fromFile, String toFile, int width, int height, int quality) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            float scaleWidth = (float) width / (float) bitmapWidth;
            float scaleHeight = (float) height / (float) bitmapHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
            File myCaptureFile = new File(toFile);
            FileOutputStream out = new FileOutputStream(myCaptureFile);
            if (resizeBitmap.compress(CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }

            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }

            if (!resizeBitmap.isRecycled()) {
                resizeBitmap.recycle();
            }
        } catch (FileNotFoundException var15) {
            var15.printStackTrace();
        } catch (IOException var16) {
            var16.printStackTrace();
        }

    }
}
