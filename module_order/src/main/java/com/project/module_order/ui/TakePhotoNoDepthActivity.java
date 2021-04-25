package com.project.module_order.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
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

import io.reactivex.functions.Consumer;

import static com.project.module_order.utils.ImageUtils.getBitmap;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2021-03-25
 * @Description
 */
public class TakePhotoNoDepthActivity extends BaseActivity {
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

    }

    private void initView() {
        preview = new CameraPreview(this, mBinding.surfaceview);
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBinding.layout.addView(preview);
        preview.setKeepScreenOn(true);


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
        intent.putExtra("pigId", pigId);
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
            mBinding.llWarning.setVisibility(View.VISIBLE);
            mBinding.tvWarning.setText(measureResponse.getMessage());
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
    }
}
