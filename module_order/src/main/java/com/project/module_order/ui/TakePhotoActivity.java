package com.project.module_order.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.huawei.hiar.ARBodyTrackingConfig;
import com.huawei.hiar.ARConfigBase;
import com.huawei.hiar.AREnginesApk;
import com.huawei.hiar.ARSession;
import com.huawei.hiar.exceptions.ARCameraNotAvailableException;
import com.huawei.hiar.exceptions.ARUnSupportedConfigurationException;
import com.huawei.hiar.exceptions.ARUnavailableClientSdkTooOldException;
import com.huawei.hiar.exceptions.ARUnavailableServiceApkTooOldException;
import com.huawei.hiar.exceptions.ARUnavailableServiceNotInstalledException;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.common_resource.TakePhotoButtonItem;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.adapter.TakePhotoBtnAdapter;
import com.project.module_order.body3d.rendering.BodyRenderManager;
import com.project.module_order.common.ConnectAppMarketActivity;
import com.project.module_order.common.DisplayRotationManager;
import com.project.module_order.databinding.OrderActivityTakePhotoBinding;
import com.project.module_order.utils.ImageUtils;
import com.project.module_order.utils.SurfaceCameraUtils;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-25
 * @Description
 */
@Route(path = ArouterConfig.Order.ORDER_TAKE_PHOTO)
public class TakePhotoActivity extends BaseActivity {
    private OrderActivityTakePhotoBinding mBinding;

    private static final String TAG = TakePhotoActivity.class.getSimpleName();

    private ARSession mArSession;

    private BodyRenderManager mBodyRenderManager;

    private DisplayRotationManager mDisplayRotationManager;

    private String message = null;

    private boolean isRemindInstall = false;
    private TakePhotoBtnAdapter mBtnAdapter;
    private int currentBtnIndex = 0;
    private Bitmap maskBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = getResources().getColor(R.color.arch_black);
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityTakePhotoBinding.inflate(getLayoutInflater());
        maskBitmap = ImageUtils.getBitmap(this, R.drawable.ic_camera);
        setContentView(mBinding.getRoot());
        initView();
        initCamera();
    }

    private void initView() {
        final List<TakePhotoButtonItem> list = new ArrayList();
        list.add(new TakePhotoButtonItem("测重识别", true));
        list.add(new TakePhotoButtonItem("猪脸识别", false));
        list.add(new TakePhotoButtonItem("耳标识别", false));
        mBinding.ivTemp.setText("测重识别");
        mBinding.rvPhotos.setAdapter(mBtnAdapter = new TakePhotoBtnAdapter());
        mBtnAdapter.bindData(true, list);
        mBtnAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                for (TakePhotoButtonItem item : list) {
                    item.setSelect(false);
                }
                list.get(index).setSelect(true);
                mBtnAdapter.notifyDataSetChanged();
                mBinding.ivTemp.setText(list.get(index).getName());
                currentBtnIndex = index;
            }
        });
        mBinding.ivTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = SurfaceCameraUtils.takePhoto(TakePhotoActivity.this, mBinding.surfaceview, maskBitmap);
                View view = mBinding.rvPhotos.getLayoutManager().findViewByPosition(currentBtnIndex);
//                mBodyRenderManager.getDepthImage();
            }
        });
    }

    private void initCamera() {
        mDisplayRotationManager = new DisplayRotationManager(this);
        // Keep the OpenGL ES running context.
        mBinding.surfaceview.setPreserveEGLContextOnPause(true);
        // Set the OpenGLES version.
        mBinding.surfaceview.setEGLContextClientVersion(2);
        // Set the EGL configuration chooser, including for the
        // number of bits of the color buffer and the number of depth bits.
        mBinding.surfaceview.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        mBodyRenderManager = new BodyRenderManager(this);
        mBodyRenderManager.setDisplayRotationManage(mDisplayRotationManager);

        mBinding.surfaceview.setRenderer(mBodyRenderManager);
        mBinding.surfaceview.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
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
        mBinding.surfaceview.onResume();
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
            mBinding.surfaceview.onPause();
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
}
