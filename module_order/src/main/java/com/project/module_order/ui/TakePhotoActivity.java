package com.project.module_order.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.utils.SharedPreferencesUtils;
import com.project.arch_repo.utils.TimeUtils;
import com.project.arch_repo.widget.ImagePopupWindow;
import com.project.common_resource.TakePhotoModel;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.requestModel.MeasurePicResponse;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.MeasureResponse;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.adapter.TakePhotoBtnAdapter;
import com.project.module_order.body3d.rendering.BodyRenderManager;
import com.project.module_order.common.ConnectAppMarketActivity;
import com.project.module_order.common.DisplayRotationManager;
import com.project.module_order.databinding.OrderActivityTakePhotoBinding;
import com.project.module_order.source.impl.PicMeasureRepositoryImpl;
import com.project.module_order.utils.ImageUtils;
import com.project.module_order.utils.SurfaceCameraUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.utils.StatusBarUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.project.module_order.utils.ImageUtils.getBitmap;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-25
 * @Description
 */
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
    private List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean> mButtonItems;
    private String orderId;
    private String latitude;
    private String longitude;
    private long pigId;
    private String addr;
    /**
     * ??????????????????
     */
    private List<PolicyDetailResDTO.ClaimListBean.PigInfoBean> photos = new ArrayList<>();
    private List<PolicyDetailResDTO.ClaimListBean> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = getResources().getColor(R.color.arch_black);
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityTakePhotoBinding.inflate(getLayoutInflater());
        maskBitmap = getBitmap(this, R.mipmap.logo_mask_full, DisplayUtils.getRealScreenSize(this).x, DisplayUtils.getRealScreenSize(this).y);
        setContentView(mBinding.getRoot());
        initView();
        initCamera();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showPopupWindow(R.mipmap.sample_pig);
            }
        }, 200);

    }

    private void initView() {
        mButtonItems = (List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean>) getIntent().getSerializableExtra("mButtonItems");
        orderId = getIntent().getStringExtra("orderId");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        addr = getIntent().getStringExtra("addr");
        pigId = getIntent().getLongExtra("pigId", 0);
        mBinding.rvPhotos.setAdapter(mBtnAdapter = new TakePhotoBtnAdapter());
        mBtnAdapter.bindData(true, mButtonItems);
        mBtnAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerAdapter adapter, BaseViewHolder holder, View itemView, int index) {
                for (LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean item : mButtonItems) {
                    item.setSelect(false);
                }
                mButtonItems.get(index).setSelect(true);
                mBtnAdapter.notifyDataSetChanged();
                currentBtnIndex = index;
                if (mButtonItems.get(currentBtnIndex).getName().contains("???")) {
                    showPopupWindow(R.mipmap.sample_pig_face);
                }
            }
        });
        mBinding.ivTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PolicyDetailResDTO.ClaimListBean.PigInfoBean photoModel = new PolicyDetailResDTO.ClaimListBean.PigInfoBean();

                final TakePhotoModel model = SurfaceCameraUtils.takePhoto(TakePhotoActivity.this, mBinding.surfaceview, maskBitmap);
                // ?????????????????????
                View view = mBinding.rvPhotos.getLayoutManager().findViewByPosition(currentBtnIndex);
                View btnLayout = view.findViewById(R.id.ll_button);
                ImageView img = view.findViewById(R.id.iv_photo);
                ImageView delete = view.findViewById(R.id.iv_delete);
                delete.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                img.setImageBitmap(model.getBitmap());
                mButtonItems.get(currentBtnIndex).setUrl(model.getPath());

                photoModel.setImgUrl(model.getPath());
                photoModel.setColumn(mButtonItems.get(currentBtnIndex).getColumn());
                photoModel.setName(mButtonItems.get(currentBtnIndex).getName());

                if (mButtonItems.get(currentBtnIndex).getColumn().equals("pigBody")) {
                    final MeasurePicResponse request = new MeasurePicResponse();
                    request.setBaodan(orderId);
                    request.setDepthImage("depthImage");
                    request.setId(pigId);
                    request.setScope_dir(null);
                    request.setLatitude(latitude);
                    request.setLongitude(longitude);
                    request.setImage(com.project.module_order.utils.ImageUtils.bitmapToString(model.getBitmap()));
                    if (request == null || photos == null || photoModel == null) {
                        return;
                    }
                    picMeasure(request, new Consumer<MeasureResponse>() {
                        @Override
                        public void accept(MeasureResponse measureResponse) throws Exception {
                            photoModel.setResults(new Gson().toJson(measureResponse));
                            photos.add(photoModel);
                            checkNextButton();
                            canNext();
                            dealMeasureData(measureResponse);
                        }
                    });
                } else {
                    photoModel.setResults(new Gson().toJson(new MeasureResponse()));
                    photos.add(photoModel);
                    checkNextButton();
                    canNext();

                }
            }
        });

        mBtnAdapter.setDeletePhotoListener(new TakePhotoBtnAdapter.DeletePhotoListener() {
            @Override
            public void onDelete(int pos) {
                mBtnAdapter.getData().get(pos).setUrl("");
                mBtnAdapter.getData().get(pos).setSelect(true);
                canNext();
                for (int i = 0; i < mBtnAdapter.getData().size(); i++) {
                    if (mBtnAdapter.getData().get(i).getUrl().isEmpty()) {
                        currentBtnIndex = i;
                        break;
                    }
                }
                for (LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean datum : mBtnAdapter.getData()) {
                    datum.setSelect(false);
                }

                mBtnAdapter.getData().get(currentBtnIndex).setSelect(true);
                mBtnAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickPhoto(int pos) {
                ARouter.getInstance()
                        .build(ArouterConfig.Order.ORDER_PHOTO_PRE)
                        .withString("url", mButtonItems.get(pos).getUrl())
                        .navigation();
            }
        });
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PolicyDetailResDTO.ClaimListBean pig = new PolicyDetailResDTO.ClaimListBean();
                pig.setPigInfo(photos);
                result.add(pig);
                photos.clear();
                reset();
            }
        });
        mBinding.tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitPhotos();
            }
        });
        mBinding.ivQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonItems.get(currentBtnIndex).getName().contains("???")) {
                    showPopupWindow(R.mipmap.sample_pig_face);
                } else {
                    showPopupWindow(R.mipmap.sample_pig);
                }
            }
        });
    }

    private void commitPhotos() {
        if (photos == null || photos.size() <= 0) {
            finish();
            return;
        }
        PolicyDetailResDTO.ClaimListBean pig = new PolicyDetailResDTO.ClaimListBean();
        pig.setPigInfo(photos);
        result.add(pig);
        Intent intent = getIntent();
        intent.putExtra("result", (Serializable) result);
        setResult(RESULT_OK, intent);
        finish();
    }

//    private void showPigTips() {
//        boolean tips = SharedPreferencesUtils.getBooleanValue(this, "showTipsBody", false);
//        if (!tips) {
//            SharedPreferencesUtils.setBooleanValue(this, "showTipsBody", true);
//            showPopupWindow(R.mipmap.sample_pig);
//        }
//    }
//
//    private void showPigFaceTips() {
//        boolean tips = SharedPreferencesUtils.getBooleanValue(this, "showPigFace", false);
//        if (!tips) {
//            SharedPreferencesUtils.setBooleanValue(this, "showPigFace", true);
//            showPopupWindow(R.mipmap.sample_pig_face);
//        }
//    }

    private void reset() {
        for (LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean takePhotoButtonItem : mButtonItems) {
            takePhotoButtonItem.setSelect(false);
            takePhotoButtonItem.setUrl("");
        }
        mButtonItems.get(0).setSelect(true);
        currentBtnIndex = 0;
        mBtnAdapter.notifyDataSetChanged();
    }

    /**
     * ????????????????????????
     */
    private void checkNextButton() {
        for (LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean item : mButtonItems) {
            item.setSelect(false);
        }
        if (currentBtnIndex < mButtonItems.size() - 1) {
            currentBtnIndex = currentBtnIndex + 1;
            mButtonItems.get(currentBtnIndex).setSelect(true);
            mBtnAdapter.notifyDataSetChanged();
        }

        if (mButtonItems.get(currentBtnIndex).getName().contains("???")) {
            mBinding.lineLayout.setVisibility(View.VISIBLE);
        } else {
            mBinding.lineLayout.setVisibility(View.GONE);
        }
    }

    private void canNext() {
        if (mButtonItems == null) {
            return;
        }

        boolean canNext = true;
        for (LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean item : mButtonItems) {
            if (item != null && TextUtils.isEmpty(item.getUrl())) {
                canNext = false;
            }
        }

        if (canNext) {
            mBinding.tvNext.setEnabled(true);
        } else {
            mBinding.tvNext.setEnabled(false);
        }
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
                config.setFocusMode(ARConfigBase.FocusMode.AUTO_FOCUS);
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

    @SuppressLint("MissingPermission")
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

    private void showPopupWindow(int source) {
        ImagePopupWindow popupWindow = new ImagePopupWindow(this);
        popupWindow.setSrc(source);
        popupWindow.showAsDropDown(mBinding.ivQuestion, 100, 0);
    }

    private void picMeasure(MeasurePicResponse model, Consumer<MeasureResponse> consumer) {
        if (model == null) {
            return;
        }
        PicMeasureRepositoryImpl.getInstance()
                .measure(model)
                .compose(XXF.<MeasureResponse>bindToLifecycle(this))
                .compose(XXF.<MeasureResponse>bindToErrorNotice())
                .compose(XXF.<MeasureResponse>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("????????????")))
                .subscribe(consumer);
    }

    private void dealMeasureData(MeasureResponse measureResponse) {
        if (measureResponse == null) {
            return;
        }

        if (measureResponse.getStatus() != MeasureResponse.CODE_SUCCESS) {
            mBinding.llWarning.setVisibility(View.VISIBLE);
            mBinding.tvWarning.setText(measureResponse.getMsg());
            mBinding.mScrollView.setVisibility(View.INVISIBLE);
        } else {
            mBinding.llWarning.setVisibility(View.GONE);
            mBinding.mScrollView.setVisibility(View.VISIBLE);
            mBinding.tvMeasureResult.setText("?????????" + measureResponse.getLength());
            mBinding.tvData.setText(TimeUtils.formatYMD(System.currentTimeMillis()));
            mBinding.tvAddr.setText(addr);
            mBinding.tvCall.setText(SharedPreferencesUtils.getStringValue(getContext(), "phone", ""));
            mBinding.tvPos.setText(latitude + ", " + longitude);
            mBinding.tvCompany.setText(GlobalDataManager.getInstance().getUserInfo().getCompanyName());
            mBinding.tvName.setText(GlobalDataManager.getInstance().getUserInfo().getUserName());

        }
    }
}
