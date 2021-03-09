package com.project.arch_repo.widget.bottomationdialog;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.project.arch_repo.utils.ImageUtils;
import com.project.arch_repo.utils.SystemUtils;
import com.project.arch_repo.widget.bottomationdialog.menu.AlbumMenuItem;
import com.project.arch_repo.widget.bottomationdialog.menu.CameraMenuItem;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 图片选择对话框[拍照/从相册选择]
 */

public class BottomPicSelectDialog extends BottomActionDialog {

    /**
     * 无裁切
     *
     * @param context
     * @param listener
     */
    public BottomPicSelectDialog(@NonNull FragmentActivity context,
                                 @NonNull final OnPicSelectListener listener) {
        super(context,
                Arrays.asList(new CameraMenuItem(context, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (listener != null) {
                            listener.onSelecPicFromCamera(s);
                        }
                    }
                }), new AlbumMenuItem(context, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (listener != null) {
                            listener.onSelectPicFromAlbum(s);
                        }
                    }
                })));
    }

    /**
     * 无裁切
     *
     * @param fragment
     * @param listener
     */
    public BottomPicSelectDialog(@NonNull Fragment fragment,
                                 @NonNull final OnPicSelectListener listener) {
        super(fragment.getContext(),
                Arrays.asList(new CameraMenuItem(fragment, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (listener != null) {
                            listener.onSelecPicFromCamera(s);
                        }
                    }
                }), new AlbumMenuItem(fragment, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (listener != null) {
                            listener.onSelectPicFromAlbum(s);
                        }
                    }
                })));
    }

    /**
     * 自带长宽比例（1：1） 正方形 裁切
     *
     * @param context
     * @param outputXY
     * @param croptedConsumer
     */
    public BottomPicSelectDialog(@NonNull FragmentActivity context,
                                 final int outputXY,
                                 @NonNull Consumer<String> croptedConsumer) {
        this(context, 1, 1, outputXY, outputXY, croptedConsumer);
    }

    /**
     * 自带裁切
     *
     * @param context
     * @param aspectX         长比例
     * @param aspectY         宽比例
     * @param outputX         输出图片长最大值 px
     * @param outputY         输出图片宽最大值 px
     * @param croptedConsumer
     */
    public BottomPicSelectDialog(@NonNull FragmentActivity context,
                                 final int aspectX, final int aspectY,
                                 final int outputX, final int outputY,
                                 @NonNull Consumer<String> croptedConsumer) {
        super(context,
                Arrays.asList(new CameraMenuItem(context, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        cropImage(context, s, aspectX, aspectY, outputX, outputY, croptedConsumer);
                    }
                }), new AlbumMenuItem(context, new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        cropImage(context, s, aspectX, aspectY, outputX, outputY, croptedConsumer);
                    }
                })));
    }


    /**
     * 裁切图片
     *
     * @param fragmentActivity
     * @param url
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     * @param croptedConsumer
     */
    private static void cropImage(FragmentActivity fragmentActivity,
                                  final String url,
                                  final int aspectX, final int aspectY,
                                  final int outputX, final int outputY,
                                  @NonNull Consumer<String> croptedConsumer) {
        File outputFile = new File(ImageUtils.getTempImageDir(fragmentActivity), System.currentTimeMillis() + "_temp_.png");
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent cropIntent = new SystemUtils.CropIntentBuilder()
                .inputImgUri(Uri.fromFile(new File(url)))
                .outputXY(outputX, outputY)
                .aspectXY(aspectX, aspectY)
                .inputImgFile(new File(url))
                .outputFile(outputFile)
                .build();
        Disposable subscribe = XXF.startActivityForResult(
                fragmentActivity,
                cropIntent,
                10005)
                .take(1)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, String>() {
                    @Override
                    public String apply(ActivityResult activityResult) throws Exception {
                        return outputFile.getAbsolutePath();
                    }
                })
                .compose(XXF.bindUntilEvent(fragmentActivity, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.bindToErrorNotice())
                .subscribe(croptedConsumer);
    }

    public interface OnPicSelectListener {
        /**
         * 拍照
         *
         * @param path
         */
        void onSelecPicFromCamera(String path);

        /**
         * 从相册选择
         *
         * @param path
         */
        void onSelectPicFromAlbum(String path);
    }


}
