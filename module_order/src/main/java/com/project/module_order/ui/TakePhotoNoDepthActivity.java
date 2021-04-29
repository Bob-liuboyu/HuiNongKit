package com.project.module_order.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.utils.SharedPreferencesUtils;
import com.project.arch_repo.utils.TimeUtils;
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.arch_repo.widget.ImagePopupWindow;
import com.project.common_resource.TakePhotoModel;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.requestModel.MeasurePicResponse;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.MeasureResponse;
import com.project.common_resource.response.MeasureResponse2;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.adapter.TakePhotoBtnAdapter;
import com.project.module_order.databinding.OrderActivityTakePhotoNoDepthBinding;
import com.project.module_order.source.impl.PicMeasureRepositoryImpl;
import com.project.module_order.test.CameraPreview;
import com.project.module_order.utils.ImageUtils;
import com.project.module_order.utils.SurfaceCameraUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.view.recyclerview.adapter.BaseRecyclerAdapter;
import com.xxf.view.recyclerview.adapter.BaseViewHolder;
import com.xxf.view.recyclerview.adapter.OnItemClickListener;
import com.xxf.view.utils.StatusBarUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.project.module_order.utils.ImageUtils.getBitmap;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-25
 * @Description
 */
public class TakePhotoNoDepthActivity extends BaseActivity implements SensorEventListener {
    private OrderActivityTakePhotoNoDepthBinding mBinding;

    private static final String TAG = TakePhotoNoDepthActivity.class.getSimpleName();
    private TakePhotoBtnAdapter mBtnAdapter;
    private int currentBtnIndex = 0;
    private Bitmap maskBitmap;
    private List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean> mButtonItems;
    private String orderId;
    private String latitude;
    private String longitude;
    private long pigId;
    private String category;
    private String addr;
    private CameraPreview preview;
    private Camera camera;
    private int mCurrentCameraId = 0;  //1是前置 0是后置
    /**
     * 每只猪的照片
     */
    private List<PolicyDetailResDTO.ClaimListBean.PigInfoBean> photos = new ArrayList<>();
    private List<PolicyDetailResDTO.ClaimListBean> result = new ArrayList<>();

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float mGravity = 9.80665F;
    /**
     * 拍照按钮
     */
    private Drawable drawableUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = getResources().getColor(R.color.arch_black);
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = OrderActivityTakePhotoNoDepthBinding.inflate(getLayoutInflater());
        maskBitmap = getBitmap(this, R.mipmap.logo_mask_full, DisplayUtils.getRealScreenSize(this).x, DisplayUtils.getRealScreenSize(this).y);
        setContentView(mBinding.getRoot());
        initView();
        //能繁母猪，不需要显示这个
        if (category.equals("0")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopupWindow(R.mipmap.sample_pig);
                }
            }, 200);

        }
        initSensor(this);

    }

    private void initView() {
        preview = new CameraPreview(this, mBinding.surfaceview);
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBinding.layout.addView(preview);
        preview.setKeepScreenOn(true);

        drawableUp = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.mipmap.icon_take));
        mButtonItems = (List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean.DetailsBean>) getIntent().getSerializableExtra("mButtonItems");
        orderId = getIntent().getStringExtra("orderId");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        addr = getIntent().getStringExtra("addr");
        category = getIntent().getStringExtra("category");
        mButtonItems.get(0).setSelect(true);
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
                    showPopupWindow(R.mipmap.sample_pig_face);
                } else if (mButtonItems.get(currentBtnIndex).getName().contains("长") || mButtonItems.get(currentBtnIndex).getName().contains("重")) {
                    //能繁母猪，不需要显示这个
                    if (category.equals("0")) {
                        showPopupWindow(R.mipmap.sample_pig);
                    }
                }

                updateLine();
            }
        });

        updateLine();

        mBinding.ivTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PolicyDetailResDTO.ClaimListBean.PigInfoBean photoModel = new PolicyDetailResDTO.ClaimListBean.PigInfoBean();

                final TakePhotoModel model = SurfaceCameraUtils.takePhoto(TakePhotoNoDepthActivity.this, mBinding.surfaceview, maskBitmap);
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

                photoModel.setImgUrl(model.getPath());
                photoModel.setColumn(mButtonItems.get(currentBtnIndex).getColumn());
                photoModel.setName(mButtonItems.get(currentBtnIndex).getName());

                if (mButtonItems.get(currentBtnIndex).getColumn().equals("pigBody")) {
                    picMeasure2(mButtonItems.get(currentBtnIndex).getColumn(),
                            category,
                            GlobalDataManager.getInstance().getToken(),
                            "depthImage",
                            longitude,
                            latitude,
                            orderId,
                            ImageUtils.bitmapToString(model.getBitmap()),
                            new Consumer<MeasureResponse2>() {
                                @Override
                                public void accept(MeasureResponse2 measureResponse) throws Exception {
                                    if (measureResponse != null && measureResponse.getCode() != 200) {
                                        showWarningDialog(measureResponse.getMessage());
                                        return;
                                    }
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
                delete(pos);
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
                pig.setId(pigId + "");
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
                    //能繁母猪，不需要显示这个
                    if (category.equals("0")) {
                        showPopupWindow(R.mipmap.sample_pig);
                    }
                }
            }
        });

        mBinding.surfaceview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.mScrollView.setVisibility(View.INVISIBLE);
                mBinding.llWarning.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void delete(int pos) {
        if (mBtnAdapter == null) {
            return;
        }
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

    private void commitPhotos() {
        if ((photos == null || photos.size() <= 0) && result.size() == 0) {
            finish();
            return;
        }
        PolicyDetailResDTO.ClaimListBean pig = new PolicyDetailResDTO.ClaimListBean();
        pig.setId(pigId + "");
        pig.setPigInfo(photos);
        result.add(pig);

        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(3)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 3 - aLong;
                    }
                })
                .compose(XXF.<Long>bindToLifecycle(this))
                .compose(XXF.<Long>bindToErrorNotice())
                .compose(XXF.<Long>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("图片处理中～")))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (aLong <= 1) {
                            Intent intent = getIntent();
                            intent.putExtra("result", (Serializable) result);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });

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


    private void updateLine() {
        // 0:育肥猪 1：能繁母猪
        if (category.equals("0") && (mButtonItems.get(currentBtnIndex).getName().contains("长") || mButtonItems.get(currentBtnIndex).getName().contains("重"))) {
            mBinding.lineLayout.setVisibility(View.VISIBLE);
        } else {
            mBinding.lineLayout.setVisibility(View.GONE);
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
            if (mButtonItems.get(currentBtnIndex).getName().contains("脸") && "0".equals(category)) {
                showPopupWindow(R.mipmap.sample_pig_face);
            }
            mBtnAdapter.notifyDataSetChanged();
        }

        updateLine();
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
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mBinding.mScrollView.setVisibility(View.INVISIBLE);
                mBinding.llWarning.setVisibility(View.INVISIBLE);
            }
        });
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
                                .setLoadingNotice("测量中～")))
                .subscribe(consumer);
    }

    private void picMeasure2(String claimType,
                             String measureType,
                             String token,
                             String depthImage,
                             String longitude,
                             String latitude,
                             String insureId,
                             String imgBase64, Consumer<MeasureResponse2> consumer) {
        PicMeasureRepositoryImpl.getInstance()
                .measure2(claimType,
                        measureType,
                        token,
                        depthImage,
                        longitude,
                        latitude,
                        insureId,
                        imgBase64)
                .compose(XXF.<MeasureResponse2>bindToLifecycle(this))
                .compose(XXF.<MeasureResponse2>bindToErrorNotice())
                .compose(XXF.<MeasureResponse2>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("测量中～")))
                .subscribe(consumer);
    }

    private void dealMeasureData(MeasureResponse2 measureResponse) {
        if (measureResponse == null || measureResponse.getData() == null) {
            return;
        }
        pigId = measureResponse.getData().getPigId();
        if (!measureResponse.isSuccess()) {
            if (!TextUtils.isEmpty(measureResponse.getMessage())) {
                mBinding.llWarning.setVisibility(View.VISIBLE);
                mBinding.tvWarning.setText(measureResponse.getMessage());
            }
            mBinding.mScrollView.setVisibility(View.INVISIBLE);
        } else {
            MeasureResponse response = measureResponse.getData().getResults();
            if (response == null) {
                return;
            }
            mBinding.llWarning.setVisibility(View.GONE);
            mBinding.mScrollView.setVisibility(View.VISIBLE);
            mBinding.tvMeasureResult.setText("长度：" + response.getWeight());
            mBinding.tvData.setText(TimeUtils.formatYMD(System.currentTimeMillis()));
            mBinding.tvAddr.setText(addr);
            mBinding.tvCall.setText(SharedPreferencesUtils.getStringValue(getContext(), "phone", ""));
            mBinding.tvPos.setText(latitude + ", " + longitude);
            mBinding.tvCompany.setText(GlobalDataManager.getInstance().getUserInfo().getCompanyName());
            mBinding.tvName.setText(GlobalDataManager.getInstance().getUserInfo().getUserName());

        }
    }

    private void showWarningDialog(String msg) {
        GrDialogUtils.createCommonDialog(this, "提示", msg, new CommonDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm(View view) {
                delete(currentBtnIndex);
            }

            @Override
            public void onClickCancel(View view) {

            }
        }).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                mCurrentCameraId = 0;
                camera = Camera.open(mCurrentCameraId);
                camera.startPreview();
                preview.setCamera(camera);
            } catch (RuntimeException ex) {
                Toast.makeText(getContext(), "相机初始化失败", Toast.LENGTH_LONG).show();
            }
        }
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
            preview.setNull();
        }
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void initSensor(Context context) {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private float mLastX;
    private float mLastY;
    private float mLastZ;

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent == null) {
            return;
        }
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        mLastX = x;
        mLastY = y;
        mLastZ = z;
//        Log.e("xxxxxxxx", x + " , " + y);
        if (x > -0.5 && x < 2.5 && y > -1 && y < 1) {
            mBinding.tvOritation.setVisibility(View.GONE);
            mBinding.ivTake.setEnabled(true);
            DrawableCompat.setTint(drawableUp, ContextCompat.getColor(getContext(), R.color.arch_color_white));
            mBinding.ivTake.setImageDrawable(drawableUp);
        } else {
//            Log.e("xxxxxxxx", x + " , " + y);
            mBinding.tvOritation.setVisibility(View.VISIBLE);
            mBinding.tvOritation.setText("请调整拍设角度！");
            mBinding.ivTake.setEnabled(false);
            DrawableCompat.setTint(drawableUp, ContextCompat.getColor(getContext(), R.color.arch_color_d1031c));
            mBinding.ivTake.setImageDrawable(drawableUp);
        }

//        if (xValue < -8 || xValue > 8 || yValue < -5 || yValue > 5) {
//
//        } else {
//
//        }
    }
}
