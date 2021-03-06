package com.project.arch_repo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.project.arch_repo.permissions.CallPhonePermissionTransformer;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.rxjava.transformer.CameraPermissionTransformer;
import com.xxf.arch.rxjava.transformer.FilePermissionTransformer;
import com.xxf.arch.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class SystemUtils {

    public static final int REQUEST_CODE_CAMERA = 59999;
    public static final int REQUEST_CODE_ALBUM = 59998;
    public static final int REQUEST_CODE_DOCUMENT = 59997;


    /**
     * @return ????????????????????????(???????????????)
     */
    public static File getRandomImageFile() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = String.format("capture_img_%s.jpg", sdf.format(new Date()));
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
    }

    /**
     * ??????????????????
     *
     * @param imageFile
     * @return
     */
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    public static Intent getTakePhotoIntent(File imageFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < 24) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put("_data", imageFile.getAbsolutePath());
            Uri outImgUri = XXF.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outImgUri);
        }
        return intent;
    }

    /**
     * ????????????
     *
     * @return
     */
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public static Intent getAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    public static Disposable doTakePhoto(final FragmentActivity context, Consumer<String> consumer) {
        return doTakePhoto(context, getRandomImageFile(), consumer);
    }

    public static Disposable doTakePhoto(final FragmentActivity context, final File imageFile, Consumer<String> consumer) {
        return Observable.zip(
                XXF.requestPermission(context, Manifest.permission.CAMERA)
                        .compose(new CameraPermissionTransformer(context))
                        .take(1),
                XXF.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .compose(new FilePermissionTransformer(context))
                        .take(1),
                new BiFunction<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                        if (!(aBoolean && aBoolean2)) {
                            throw new RuntimeException("permission dennied");
                        }
                        return aBoolean && aBoolean2;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        return XXF.startActivityForResult(context, getTakePhotoIntent(imageFile), REQUEST_CODE_CAMERA)
                                .take(1)
                                .map(new Function<ActivityResult, String>() {
                                    @Override
                                    public String apply(ActivityResult activityResult) throws Exception {
                                        if (!activityResult.isOk()) {
                                            throw new RuntimeException("cancel");
                                        }
                                        return imageFile.getAbsolutePath();
                                    }
                                });
                    }
                })
                .compose(XXF.<String>bindUntilEvent(context, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<String>bindToErrorNotice())
                .subscribe(consumer);
    }


    /**
     * ??????
     *
     * @param context
     * @param consumer
     */
    public static void doTakePhoto(final Fragment context, Consumer<String> consumer) {
        doTakePhoto(context, getRandomImageFile(), consumer);
    }

    /**
     * ??????
     *
     * @param context
     * @param imageFile
     * @param consumer
     * @return
     */
    public static Disposable doTakePhoto(final Fragment context, final File imageFile, Consumer<String> consumer) {
        return Observable.zip(
                XXF.requestPermission(context, Manifest.permission.CAMERA)
                        .compose(new CameraPermissionTransformer(context.getContext()))
                        .take(1),
                XXF.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .compose(new FilePermissionTransformer(context.getContext()))
                        .take(1),
                new BiFunction<Boolean, Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                        if (!(aBoolean && aBoolean2)) {
                            throw new RuntimeException("permission denied");
                        }
                        return aBoolean && aBoolean2;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        return XXF.startActivityForResult(context, getTakePhotoIntent(imageFile), REQUEST_CODE_CAMERA)
                                .take(1)
                                .map(new Function<ActivityResult, String>() {
                                    @Override
                                    public String apply(ActivityResult activityResult) throws Exception {
                                        if (!activityResult.isOk()) {
                                            throw new RuntimeException("cancel");
                                        }
                                        return imageFile.getAbsolutePath();
                                    }
                                });
                    }
                })
                .compose(XXF.<String>bindUntilEvent(context, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<String>bindToErrorNotice())
                .subscribe(consumer);
    }

    /**
     * ?????????????????????
     *
     * @param context
     * @param picName ???name ??????full path
     * @param bmp
     * @return
     */
    public static Observable<File> saveImageToAlbum(FragmentActivity context, String picName, Bitmap bmp) {
        return XXF.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(new CameraPermissionTransformer(context))
                .take(1)
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("permission denied");
                        }
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(Boolean aBoolean) throws Exception {
                        return Observable
                                .fromCallable(new Callable<File>() {
                                    @Override
                                    public File call() throws Exception {
                                        File picFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), picName);
                                        FileOutputStream fos = new FileOutputStream(picFile);
                                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                        fos.flush();
                                        fos.close();
                                        try {
                                            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                                    picFile.getAbsolutePath(), picName, null);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                        // ????????????????????????
                                        Uri localUri = Uri.fromFile(picFile);
                                        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                                        context.sendBroadcast(localIntent);
                                        return picFile;
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                })
                .compose(XXF.bindUntilEvent(context, Lifecycle.Event.ON_DESTROY));
    }

    /**
     * ????????????
     *
     * @param context
     * @param consumer
     * @return
     */
    public static Disposable doSelectAlbum(final FragmentActivity context, Consumer<String> consumer) {
        return XXF.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(new CameraPermissionTransformer(context))
                .take(1)
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("permission denied");
                        }
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        return XXF.startActivityForResult(context, getAlbumIntent(), REQUEST_CODE_ALBUM)
                                .take(1)
                                .map(new Function<ActivityResult, String>() {
                                    @Override
                                    public String apply(ActivityResult activityResult) throws Exception {
                                        if (!activityResult.isOk()) {
                                            throw new RuntimeException("cancel");
                                        }
                                        return UriUtils.getPath(context, activityResult.getData().getData());
                                    }
                                });
                    }
                })
                .compose(XXF.<String>bindUntilEvent(context, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<String>bindToErrorNotice())
                .subscribe(consumer);
    }


    /**
     * ????????????
     *
     * @param context
     * @param consumer
     * @return
     */
    public static Disposable doSelectAlbum(final Fragment context, Consumer<String> consumer) {
        return XXF.requestPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(new CameraPermissionTransformer(context.getContext()))
                .take(1)
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("permission denied");
                        }
                        return aBoolean;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Boolean aBoolean) throws Exception {
                        return XXF.startActivityForResult(context, getAlbumIntent(), REQUEST_CODE_ALBUM)
                                .take(1)
                                .map(new Function<ActivityResult, String>() {
                                    @Override
                                    public String apply(ActivityResult activityResult) throws Exception {
                                        if (!activityResult.isOk()) {
                                            throw new RuntimeException("cancel");
                                        }
                                        return UriUtils.getPath(context.getContext(), activityResult.getData().getData());
                                    }
                                });
                    }
                })
                .compose(XXF.<String>bindUntilEvent(context, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<String>bindToErrorNotice())
                .subscribe(consumer);
    }


    private static Intent getDocumentIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] supportedMimeTypes = {
                //  "application/msword",
                "application/pdf"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
        //?????????pdf
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//??????
        return intent;
    }


    /**
     * ????????????
     *
     * @param activity
     * @param consumer
     * @return
     */
    public static Disposable doSelectDocument(final FragmentActivity activity, Consumer<String> consumer) {
        Intent intent = getDocumentIntent();
        return XXF.startActivityForResult(activity, intent, REQUEST_CODE_DOCUMENT)
                .map(new Function<ActivityResult, String>() {
                    @Override
                    public String apply(ActivityResult activityResult) throws Exception {
                        if (!activityResult.isOk()) {
                            throw new RuntimeException("cancel");
                        }
                        return UriUtils.getPath(activity, activityResult.getData().getData());
                    }
                })
                .compose(XXF.<String>bindUntilEvent(activity, Lifecycle.Event.ON_DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<String>bindToErrorNotice())
                .subscribe(consumer);
    }


    /**
     * ??????????????????
     *
     * @param context
     * @param phone
     */
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static final void callPhone(Context context, String phone) {
        try {
            if (context == null) {
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                return;
            }
            if (TextUtils.isEmpty(phone.trim())) {
                return;
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phone.trim());
            intent.setData(data);
            context.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
            ToastUtils.showToast("???????????????????????????!");
        }
    }

    /**
     * ????????????????????????,??????????????????
     *
     * @param fragmentActivity
     * @param phone
     */
    @SuppressLint("CheckResult")
    public static final void callPhoneSafe(final FragmentActivity fragmentActivity, final String phone) {
        XXF.requestPermission(fragmentActivity, Manifest.permission.CALL_PHONE)
                .compose(new CallPhonePermissionTransformer(fragmentActivity, false))
                .take(1)
                .map(new Function<Boolean, Boolean>() {
                    @Override
                    public Boolean apply(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("call phone permission denied!");
                        }
                        return aBoolean;
                    }
                })
                .compose(XXF.<Boolean>bindUntilEvent(fragmentActivity, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.<Boolean>bindToErrorNotice())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        callPhone(fragmentActivity, phone);
                    }
                });
    }

    /**
     * ????????????????????????
     *
     * @param context
     * @param phone
     */
    public static final boolean goDialPhone(Context context, String phone) {
        if (context != null && phone != null && phone.trim().length() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + phone.trim());
            intent.setData(data);
            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException ignored) {
            }
        }
        return false;
    }

    /**
     * ???????????????
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Activity act) {
        try {
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * ???????????????
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Activity act, boolean clearFouces) {
        try {
            if (clearFouces) {
                View currentFocus = act.getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
            }
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * ???????????????2
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Context act, View v) {
        try {
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * ???????????????2
     *
     * @param act
     */
    public static void hideSoftKeyBoard(Context act, View v, boolean clearFouces) {
        try {
            if (v != null && clearFouces) {
                v.clearFocus();
            }
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    /**
     * ???????????????
     *
     * @param act
     */
    public static void showSoftKeyBoard(Activity act) {
        try {
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(act.getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
        }
    }

    /**
     * ???????????????2
     *
     * @param act
     */
    public static void showSoftKeyBoard(Context act, View v) {
        try {
            if (v != null) v.requestFocus();
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        } catch (Exception e) {
        }
    }


    /**
     * ??????????????????
     *
     * @param lable
     * @param charSequence
     */
    public static void copyTextToClipboard(@Nullable String lable, @NonNull CharSequence charSequence) {
        ClipboardManager cmb = (ClipboardManager) XXF.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb == null) {
            return;
        }
        //??????????????????????????????,???????????????????????????"??????"??????
        cmb.setPrimaryClip(ClipData.newPlainText(lable, charSequence));
    }

    /**
     * ??????????????????
     *
     * @param charSequence
     */
    public static void copyTextToClipboard(@NonNull CharSequence charSequence) {
        ClipboardManager cmb = (ClipboardManager) XXF.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb == null) {
            return;
        }
        //??????????????????????????????,???????????????????????????"??????"??????
        cmb.setPrimaryClip(ClipData.newPlainText("text", charSequence));
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public static CharSequence getTextFromClipboard() {
        ClipboardManager cmb = (ClipboardManager) XXF.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null) {
            if (cmb.hasPrimaryClip()) {
                if (cmb.getPrimaryClip().getItemCount() > 0) {
                    return cmb.getPrimaryClip().getItemAt(0).getText();
                }
            }
        }
        return null;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????
     * ??????TextView.OnEditorActionListener??? onEditorAction(TextView v, int actionId, KeyEvent event)???????????????
     *
     * @param actionId
     * @param event
     * @return
     */
    public static boolean isSearchClick(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_NEXT
                || actionId == EditorInfo.IME_ACTION_SEND
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction());
    }


    private static Uri queryMediaImageUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = XXF.getApplication().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data=? ", new String[]{filePath}, (String) null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else if (imageFile.exists()) {
            ContentValues values = new ContentValues();
            values.put("_data", filePath);
            return XXF.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    public static class CropIntentBuilder {
        private Intent mCropIntent = new Intent("com.android.camera.action.CROP");

        public CropIntentBuilder() {
            //????????? ????????????!!!!!
            this.mCropIntent.putExtra("crop", true);
            this.mCropIntent.putExtra("outputX", 320);
            this.mCropIntent.putExtra("outputY", 320);
            this.mCropIntent.putExtra("scale", true);
            this.mCropIntent.putExtra("aspectX", 1);
            this.mCropIntent.putExtra("aspectY", 1);
            this.mCropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            this.mCropIntent.putExtra("noFaceDetection", true);
            this.mCropIntent.putExtra("scale", true);
            this.mCropIntent.putExtra("scaleUpIfNeeded", true);
        }

        public CropIntentBuilder inputImgUri(Uri inImgUri) {
            this.mCropIntent.setDataAndType(inImgUri, "image/*");
            return this;
        }

        public CropIntentBuilder inputImgFile(File inImgFile) {
            Uri inImgUri = queryMediaImageUri(inImgFile);
            this.mCropIntent.setDataAndType(inImgUri, "image/*");
            return this;
        }

        public CropIntentBuilder aspectXY(int x, int y) {
            this.mCropIntent.putExtra("aspectX", x);
            this.mCropIntent.putExtra("aspectY", y);
            return this;
        }

        public CropIntentBuilder outputXY(int x, int y) {
            this.mCropIntent.putExtra("outputX", x);
            this.mCropIntent.putExtra("outputY", y);
            return this;
        }

        public CropIntentBuilder outputBitmap() {
            this.mCropIntent.putExtra("return-data", true);
            return this;
        }

        public CropIntentBuilder outputUri(Uri outputImgUri) {
            this.mCropIntent.putExtra("return-data", false);
            this.mCropIntent.putExtra("output", outputImgUri);
            return this;
        }

        public CropIntentBuilder outputFile(File outputImgFile) {
            this.mCropIntent.putExtra("return-data", false);
            this.mCropIntent.putExtra("output", queryMediaImageUri(outputImgFile));
            return this;
        }

        public Intent build() {
            return this.mCropIntent;
        }
    }
}
