/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.module_order.body3d;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hiar.ARBodyTrackingConfig;
import com.huawei.hiar.ARConfigBase;
import com.huawei.hiar.AREnginesApk;
import com.huawei.hiar.ARSession;
import com.huawei.hiar.exceptions.ARCameraNotAvailableException;
import com.huawei.hiar.exceptions.ARUnSupportedConfigurationException;
import com.huawei.hiar.exceptions.ARUnavailableClientSdkTooOldException;
import com.huawei.hiar.exceptions.ARUnavailableServiceApkTooOldException;
import com.huawei.hiar.exceptions.ARUnavailableServiceNotInstalledException;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.utils.ImageMaskUtil;
import com.project.module_order.R;
import com.project.module_order.body3d.rendering.BodyRenderManager;
import com.project.module_order.common.ConnectAppMarketActivity;
import com.project.module_order.common.DisplayRotationManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.project.module_order.utils.ImageUtils.getBitmap;

/**
 * The sample code demonstrates the capability of HUAWEI AR Engine to identify
 * body skeleton points and output human body features such as limb endpoints,
 * body posture, and skeleton.
 *
 * @author HW
 * @since 2020-04-01
 */
public class BodyActivity extends Activity {
    private static final String TAG = BodyActivity.class.getSimpleName();

    private ARSession mArSession;

    private GLSurfaceView mSurfaceView;

    private BodyRenderManager mBodyRenderManager;

    private DisplayRotationManager mDisplayRotationManager;

    // Used for the display of recognition data.
    private TextView mTextView;

    private String message = null;

    private boolean isRemindInstall = false;

    private ImageView btn_done;
    private Bitmap maskBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body3d_activity_main);
        mTextView = findViewById(R.id.bodyTextView);
        mSurfaceView = findViewById(R.id.bodySurfaceview);
        mDisplayRotationManager = new DisplayRotationManager(this);
        maskBitmap = getBitmap(this, R.mipmap.logo_mask_full, DisplayUtils.getRealScreenSize(this).x, DisplayUtils.getRealScreenSize(this).y);
//        maskBitmap = BitmapFactory.decodeStream(getClass().getResourceAsStream("/res/drawable/ic_camera.png"));;
        // Keep the OpenGL ES running context.
        mSurfaceView.setPreserveEGLContextOnPause(true);

        // Set the OpenGLES version.
        mSurfaceView.setEGLContextClientVersion(2);

        // Set the EGL configuration chooser, including for the
        // number of bits of the color buffer and the number of depth bits.
        mSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        mBodyRenderManager = new BodyRenderManager(this);
        mBodyRenderManager.setDisplayRotationManage(mDisplayRotationManager);

        mSurfaceView.setRenderer(mBodyRenderManager);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        btn_done = findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                takePhoto();
                mBodyRenderManager.getDepthImage();
            }
        });
        btn_done.setImageBitmap(maskBitmap);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        Exception exception = null;
        message = null;
        if (mArSession == null) {
            try {
                if (!arEngineAbilityCheck()) {
                    finish();
                    return;
                }
                mArSession = new ARSession(this);
                ARBodyTrackingConfig config = new ARBodyTrackingConfig(mArSession);
                config.setEnableItem(ARConfigBase.ENABLE_DEPTH | ARConfigBase.ENABLE_MASK);
                mArSession.configure(config);
                mBodyRenderManager.setArSession(mArSession);
            } catch (Exception capturedException) {
                exception = capturedException;
                setMessageWhenError(capturedException);
            }
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Creating session", exception);
                if (mArSession != null) {
                    mArSession.stop();
                    mArSession = null;
                }
                return;
            }
        }
        try {
            mArSession.resume();
        } catch (ARCameraNotAvailableException e) {
            Toast.makeText(this, "Camera open failed, please restart the app", Toast.LENGTH_LONG).show();
            mArSession = null;
            return;
        }
        mSurfaceView.onResume();
        mDisplayRotationManager.registerDisplayListener();
    }

    /**
     * Check whether HUAWEI AR Engine server (com.huawei.arengine.service) is installed on the current device.
     * If not, redirect the user to HUAWEI AppGallery for installation.
     */
    private boolean arEngineAbilityCheck() {
        boolean isInstallArEngineApk = AREnginesApk.isAREngineApkReady(this);
        if (!isInstallArEngineApk && isRemindInstall) {
            Toast.makeText(this, "Please agree to install.", Toast.LENGTH_LONG).show();
            finish();
        }
        Log.d(TAG, "Is Install AR Engine Apk: " + isInstallArEngineApk);
        if (!isInstallArEngineApk) {
            startActivity(new Intent(this, ConnectAppMarketActivity.class));
            isRemindInstall = true;
        }
        return AREnginesApk.isAREngineApkReady(this);
    }

    private void setMessageWhenError(Exception catchException) {
        if (catchException instanceof ARUnavailableServiceNotInstalledException) {
            startActivity(new Intent(this, ConnectAppMarketActivity.class));
        } else if (catchException instanceof ARUnavailableServiceApkTooOldException) {
            message = "Please update HuaweiARService.apk";
        } else if (catchException instanceof ARUnavailableClientSdkTooOldException) {
            message = "Please update this app";
        } else if (catchException instanceof ARUnSupportedConfigurationException) {
            message = "The configuration is not supported by the device!";
        } else {
            message = "exception throw";
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause start.");
        super.onPause();
        if (mArSession != null) {
            mDisplayRotationManager.unregisterDisplayListener();
            mSurfaceView.onPause();
            mArSession.pause();
        }
        Log.i(TAG, "onPause end.");
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy start.");
        super.onDestroy();
        if (mArSession != null) {
            mArSession.stop();
            mArSession = null;
        }
        Log.i(TAG, "onDestroy end.");
    }

    @Override
    public void onWindowFocusChanged(boolean isHasFocus) {
        Log.d(TAG, "onWindowFocusChanged");
        super.onWindowFocusChanged(isHasFocus);
        if (isHasFocus) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    // ---------------------------------------------------------------

    private String generateFilename() {
        return Environment.getExternalStorageDirectory() + "/DCIM/HuiNongKit/"
                + System.currentTimeMillis() + ".jpg";
    }

    private void saveBitmapToDisk(Bitmap bitmap, String filename) throws IOException {

        File out = new File(filename);
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(filename);
             ByteArrayOutputStream outputData = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData);
            outputData.writeTo(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            Log.e("xxxxxxxxx", "IOException = " + ex);
            throw new IOException("Failed to save bitmap to disk", ex);
        }
    }

    private void takePhoto() {
        final String filename = generateFilename();
        /*ArSceneView view = fragment.getArSceneView();*/
        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(mSurfaceView.getWidth(), mSurfaceView.getHeight(),
                Bitmap.Config.ARGB_8888);
        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PixelCopy.request(mSurfaceView, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
                @Override
                public void onPixelCopyFinished(int copyResult) {
                    if (copyResult == PixelCopy.SUCCESS) {
                        try {
                            final Bitmap result = ImageMaskUtil.createWaterMaskLeftTop(BodyActivity.this, bitmap, maskBitmap, 100, 100);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_done.setImageBitmap(result);
                                }
                            });

                            saveBitmapToDisk(result, filename);
                        } catch (Exception e) {
                            Toast toast = Toast.makeText(BodyActivity.this, e.toString(),
                                    Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                "Photo saved", Snackbar.LENGTH_LONG);
                        snackbar.setAction("Open in Photos", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                File photoFile = new File(filename);

                                Uri photoURI = FileProvider.getUriForFile(BodyActivity.this,
                                        BodyActivity.this.getPackageName() + ".ar.codelab.name.provider",
                                        photoFile);
                                Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                                intent.setDataAndType(photoURI, "image/*");
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivity(intent);
                            }
                        });
                        snackbar.show();
                    } else {
                        Log.d("HelloArActivity", "Failed to copyPixels: " + copyResult);
                        Toast toast = Toast.makeText(BodyActivity.this,
                                "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    handlerThread.quitSafely();
                }
            }, new Handler(handlerThread.getLooper()));
        }
    }


//    private Bitmap createWatermark(Bitmap bitmap, String mark) {
//        int w = bitmap.getWidth();
//        int h = bitmap.getHeight();
//        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        Bitmap src = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
//        Canvas canvas = new Canvas(bmp);
//        Paint p = new Paint();
//        // ????????????
//        p.setColor(Color.parseColor("#c5576370"));
//        // ??????????????????
//        p.setTextSize(150);
//        //?????????
//        p.setAntiAlias(true);
//        //????????????
//        canvas.drawBitmap(bitmap, 0, 0, p);
//        //????????????
//        canvas.drawText(mark, 0, h / 2, p);
//
//        int iconHeight = iconWidth * ((watermark.getHeight()*1000)/watermark.getWidth())/1000;//???????????????????????????????????????????????? iconHeight = iconWidth;
//        //??????????????????????????????
//        RectF rectF=new RectF(marginLeft,srcHeight - marginBottom - layout.getHeight()/2 - iconHeight/2
//                ,marginLeft + iconWidth,srcHeight - marginBottom - layout.getHeight()/2 + iconHeight/2);
//        canvas.drawBitmap(watermark,null,rectF,null);//????????????????????????
//        canvas.save();
//        canvas.restore();
//        return bmp;
//    }


}