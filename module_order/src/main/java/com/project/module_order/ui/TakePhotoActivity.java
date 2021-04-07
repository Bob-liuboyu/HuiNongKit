package com.project.module_order.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
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
import com.project.arch_repo.widget.ImagePopupWindow;
import com.project.common_resource.TakePhotoModel;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.PolicyDetailResDTO;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private LocationManager locationManager;
    private String locationProvider = null;
    private String message = null;

    private boolean isRemindInstall = false;
    private TakePhotoBtnAdapter mBtnAdapter;
    private int currentBtnIndex = 0;
    private Bitmap maskBitmap;
    private List<Address> mAddresses;
    private Location mLocation;
    private List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean> mButtonItems;
    /**
     * 每只猪的照片
     */
    private List<PolicyDetailResDTO.ClaimListBean.PigInfoBean> photos = new ArrayList<>();
    private List<PolicyDetailResDTO.ClaimListBean> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = getResources().getColor(R.color.arch_black);
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityTakePhotoBinding.inflate(getLayoutInflater());
        maskBitmap = ImageUtils.getBitmap(this, R.drawable.ic_camera, DisplayUtils.dip2px(this, 200), DisplayUtils.dip2px(this, 200));
        setContentView(mBinding.getRoot());
        initView();
        initCamera();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showPigTips();
            }
        }, 200);
        getLocation();
    }

    private void initView() {
        mButtonItems = (List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean>) getIntent().getSerializableExtra("mButtonItems");
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
                if (mButtonItems.get(currentBtnIndex).getName().contains("脸")) {
                    showPigFaceTips();
                }
            }
        });
        mBinding.ivTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PolicyDetailResDTO.ClaimListBean.PigInfoBean photoModel = new PolicyDetailResDTO.ClaimListBean.PigInfoBean();

                TakePhotoModel model = SurfaceCameraUtils.takePhoto(TakePhotoActivity.this, mBinding.surfaceview, maskBitmap);
                // 先显示，后保存
                View view = mBinding.rvPhotos.getLayoutManager().findViewByPosition(currentBtnIndex);
                View btnLayout = view.findViewById(R.id.ll_button);
                ImageView img = view.findViewById(R.id.iv_photo);
                ImageView delete = view.findViewById(R.id.iv_delete);
                delete.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                img.setImageBitmap(model.getBitmap());
                mButtonItems.get(currentBtnIndex).setUrl(model.getPath());
                checkNextButton();
                canNext();

                photoModel.setImgUrl(model.getPath());
                photoModel.setColumn(mButtonItems.get(currentBtnIndex).getName());
                photoModel.setResults("result");
                photos.add(photoModel);

            }
        });

        mBtnAdapter.setDeletePhotoListener(new TakePhotoBtnAdapter.DeletePhotoListener() {
            @Override
            public void onDelete(int pos) {
                View view = mBinding.rvPhotos.getLayoutManager().findViewByPosition(currentBtnIndex);
                View btnLayout = view.findViewById(R.id.ll_button);
                ImageView img = view.findViewById(R.id.iv_photo);
                ImageView delete = view.findViewById(R.id.iv_delete);
                View llRootView = view.findViewById(R.id.ll_rootView);

                img.setImageBitmap(null);
                img.setVisibility(View.GONE);
                btnLayout.setVisibility(View.VISIBLE);
                delete.setVisibility(View.INVISIBLE);
                llRootView.setSelected(true);
                canNext();
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
                if (mAddresses != null) {
                    pig.setAddress(mAddresses.toString());
                }
                if (mLocation != null) {
                    pig.setLatitude(mLocation.getLatitude() + "");
                    pig.setLongitude(mLocation.getLongitude() + "");
                }
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
                if (mButtonItems.get(currentBtnIndex).getName().contains("脸")) {
                    showPopupWindow(R.mipmap.sample_pig_face);
                } else {
                    showPopupWindow(R.mipmap.sample_pig);
                }
            }
        });
    }

    private void commitPhotos() {
        PolicyDetailResDTO.ClaimListBean pig = new PolicyDetailResDTO.ClaimListBean();
        pig.setPigInfo(photos);
        if (mAddresses != null) {
            pig.setAddress(mAddresses.toString());
        }
        if (mLocation != null) {
            pig.setLatitude(mLocation.getLatitude() + "");
            pig.setLongitude(mLocation.getLongitude() + "");
        }
        result.add(pig);
        Intent intent = getIntent();
        intent.putExtra("result", (Serializable) result);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showPigTips() {
        boolean tips = SharedPreferencesUtils.getBooleanValue(this, "showTipsBody", false);
        if (!tips) {
            SharedPreferencesUtils.setBooleanValue(this, "showTipsBody", true);
            showPopupWindow(R.mipmap.sample_pig);
        }
    }

    private void showPigFaceTips() {
        boolean tips = SharedPreferencesUtils.getBooleanValue(this, "showPigFace", false);
        if (!tips) {
            SharedPreferencesUtils.setBooleanValue(this, "showPigFace", true);
            showPopupWindow(R.mipmap.sample_pig_face);
        }
    }

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
     * 切换到下一个部位
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
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
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


    @SuppressLint("MissingPermission")
    private void getLocation() {
        //1.获取位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.v("TAG", "定位方式GPS");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.v("TAG", "定位方式Network");
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLocation = locationManager.getLastKnownLocation(locationProvider);
            if (mLocation != null) {
                Toast.makeText(this, mLocation.getLongitude() + " " +
                        mLocation.getLatitude() + "", Toast.LENGTH_SHORT).show();
                Log.v("TAG", "获取上次的位置-经纬度：" + mLocation.getLongitude() + "   " + mLocation.getLatitude());
                getAddress(mLocation);

            } else {
                //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            }
        } else {
            mLocation = locationManager.getLastKnownLocation(locationProvider);
            if (mLocation != null) {
                Toast.makeText(this, mLocation.getLongitude() + " " +
                        mLocation.getLatitude() + "", Toast.LENGTH_SHORT).show();
                Log.v("TAG", "获取上次的位置-经纬度：" + mLocation.getLongitude() + "   " + mLocation.getLatitude());
                getAddress(mLocation);
            } else {
                //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            }
        }
    }

    public LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //如果位置发生变化，重新显示地理位置经纬度
                Toast.makeText(TakePhotoActivity.this, location.getLongitude() + " " +
                        location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                Log.v("TAG", "监视地理位置变化-经纬度：" + location.getLongitude() + "   " + location.getLatitude());
            }
        }
    };

    /**
     * 获取地址信息:城市、街道等信息
     *
     * @param location
     * @return
     */
    private void getAddress(Location location) {
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                mAddresses = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
                Toast.makeText(this, "获取地址信息：" + result.toString(), Toast.LENGTH_LONG).show();
                Log.v("TAG", "获取地址信息：" + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
