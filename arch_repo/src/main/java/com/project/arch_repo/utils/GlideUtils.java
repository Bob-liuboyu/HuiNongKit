package com.project.arch_repo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.xxf.arch.image.svg.SvgSoftwareLayerSetter;

import java.util.concurrent.ExecutionException;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class GlideUtils {

    /**
     * fragment 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param fragment
     * @return
     */
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return false;
        }
        FragmentActivity parentActivity = fragment.getActivity();
        return canLoadImage(parentActivity);
    }

    /**
     * context 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param context
     * @return
     */
    public static boolean canLoadImage(Context context) {
        if (context == null) {
            return false;
        }
        if (!(context instanceof Activity)) {
            return true;
        }
        Activity activity = (Activity) context;
        return canLoadImage(activity);
    }


    /**
     * 是否销毁或者正在销毁
     *
     * @return
     */
    public static final boolean isDestroyOrFinishing(@NonNull Activity activity) {
        if (activity == null) {
            return true;
        }
        boolean destroyed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                activity.isDestroyed();
        return destroyed || activity.isFinishing();
    }


    /**
     * activity 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param activity
     * @return
     */
    public static boolean canLoadImage(Activity activity) {
        return !isDestroyOrFinishing(activity);
    }

    /**
     * 是否可以加载
     *
     * @param v
     * @return
     */
    public static boolean canLoadImage(View v) {
        return v != null && canLoadImage(v.getContext());
    }


    public static boolean isSvg(String imageUrl) {
        return !TextUtils.isEmpty(imageUrl) && imageUrl.endsWith(".svg");
    }

    /**
     * 加载图片
     *
     * @param view
     * @param imageUrl
     * @param imagePlaceholder
     * @param imageError
     */
    public static void loadImage(ImageView view, String imageUrl, @DrawableRes int imagePlaceholder, @DrawableRes int imageError, boolean circleCrop) {
        if (canLoadImage(view)) {
            if (isSvg(imageUrl)) {

                RequestBuilder<PictureDrawable> requestBuilder = Glide.with(view.getContext())
                        .as(PictureDrawable.class)
                        .transition(withCrossFade())
                        .listener(new SvgSoftwareLayerSetter())
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .load(imageUrl)
                        .placeholder(imagePlaceholder)
                        .error(imageError);
                if (circleCrop) {
                    requestBuilder.circleCrop();
                }
                requestBuilder
                        .into(view);
            } else {
                RequestBuilder<Drawable> requestBuilder = Glide.with(view.getContext())
                        .load(imageUrl)
                        .placeholder(imagePlaceholder)
                        .error(imageError);
                if (circleCrop) {
                    requestBuilder.circleCrop();
                }
                requestBuilder
                        .into(view);

            }
        }
    }


    /**
     * 加载图片
     *
     * @param view
     * @param imageUrl
     */
    public static void loadImage(ImageView view, String imageUrl) {
        loadImage(view, imageUrl, 0, 0, false);
    }

    /**
     * 加载图片
     *
     * @param view
     * @param source
     */
    public static void loadImage(ImageView view, int source) {
        Glide.with(view.getContext()).load(source).into(view);
    }

    /**
     * 加载圆形图片
     *
     * @param view
     * @param imageUrl
     */
    public static void loadCircleImage(ImageView view, String imageUrl) {
        loadImage(view, imageUrl, 0, 0, true);
    }

    /**
     * 圆角
     *
     * @param context
     * @param bitmap
     * @param roundingRadius
     * @return
     */
    public static Bitmap roundedCorners(Context context, Bitmap bitmap, int roundingRadius) throws ExecutionException, InterruptedException {
        BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, roundingRadius);
     /*   RoundedCorners roundedCorners = new RoundedCorners(roundingRadius);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        return Glide.with(context)
                .asBitmap()
                .load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(options)
                .submit()
                .get();*/
    }


    /**
     * 圆形图片
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Bitmap circleCrop(Context context, Bitmap bitmap) {
        BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
        return TransformationUtils.circleCrop(bitmapPool, bitmap, bitmap.getWidth(), bitmap.getHeight());
    }
}
