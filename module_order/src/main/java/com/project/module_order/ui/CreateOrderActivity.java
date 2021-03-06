package com.project.module_order.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.DateTimeUtils;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.utils.FileUtils;
import com.project.arch_repo.utils.SharedPreferencesUtils;
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.DatePickerDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.requestModel.CreatePolicyRequestModel;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.AppConstant;
import com.project.module_order.R;
import com.project.module_order.adapter.OrderPhotosListAdapter;
import com.project.module_order.databinding.OrderActivityCreateBinding;
import com.project.module_order.source.impl.OrderRepositoryImpl;
import com.xxf.arch.XXF;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.StatusBarUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.project.common_resource.global.ConstantData.FILE_PATH;

/**
 * @fileName: CreateOrderActivity
 * @author: liuboyu
 * @date: 2021/3/15 11:32 AM
 * @description: ????????????
 */
@Route(path = ArouterConfig.Order.ORDER_CREATE)
public class CreateOrderActivity extends BaseActivity {

    protected OrderActivityCreateBinding binding;
    protected OrderPhotosListAdapter mAdapter;
    //??????
    protected static final int RESULT_CHOOSE = 100;
    //????????????
    protected static final int RESULT_MEASURE = 101;
    protected List<PolicyDetailResDTO.ClaimListBean> result = new ArrayList<>();
    private SimpleDateFormat sdf;
    private boolean isFromChoose;
    private boolean isFromPhoto;
    private List<Address> mAddresses;
    private Location mLocation;
    private LocationManager locationManager;
    private String locationProvider = null;
    /**
     * ????????????
     */
    private List<LoginResDTO.SettingsBean.CategoryBean> category;
    private List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean> measureWays;

    private LoginResDTO.SettingsBean.CategoryBean currentCategory = null;
    private LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean currentMeasureWay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = 0xFFFFFFFF;
        StatusBarUtils.compatStatusBarForM(this, false, color);
        binding = OrderActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        createPolicyCategoryItems();
        createMeasureWayItems();
        setListener();
        getLocation();
        clearLocalPhotos();
    }

    private void initView() {
        binding.llPolicyCategory.setVisibility(View.VISIBLE);
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        category = GlobalDataManager.getInstance().getSettings().getCategory();
        binding.tvTitle.setText("????????????");
        binding.tvRightText.setText("??????");
        if (category != null) {
            currentCategory = category.get(0);
        }
        if (currentCategory != null && currentCategory.getMeasure_ways() != null && currentCategory.getMeasure_ways().size() > 0) {
            measureWays = currentCategory.getMeasure_ways();
            currentMeasureWay = measureWays.get(0);
        }
        binding.tvMaster.setText(GlobalDataManager.getInstance().getUserInfo().getUserName());
    }

    private void createMeasureWayItems() {
        if (currentCategory == null || currentCategory.getMeasure_ways() == null) {
            return;
        }
        binding.llMeasureWay.removeAllViews();
        if (currentCategory.getMeasure_ways().size() == 1) {
            binding.layoutMeasure.setVisibility(View.GONE);
            currentMeasureWay = currentCategory.getMeasure_ways().get(0);
            return;
        }
        binding.layoutMeasure.setVisibility(View.VISIBLE);
        int padding15 = DisplayUtils.dip2px(this, 15);
        int padding3 = DisplayUtils.dip2px(this, 3);

        for (final LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean way : currentCategory.getMeasure_ways()) {
            final TextView item = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DisplayUtils.dip2px(this, 15);
            item.setPadding(padding15, padding3, padding15, padding3);
            item.setBackgroundResource(R.drawable.filter_select_status);
            item.setTextColor(getResources().getColorStateList(R.color.filter_select_text));
            item.setText(way.getMeasureName());
            item.setGravity(Gravity.CENTER);
            item.setLayoutParams(params);
            item.setTextSize(13);
            binding.llMeasureWay.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFromPhoto) {
                        ToastUtils.showToast("???????????????????????????");
                        return;
                    }
                    for (int j = 0; j < binding.llMeasureWay.getChildCount(); j++) {
                        binding.llMeasureWay.getChildAt(j).setSelected(false);
                    }
                    item.setSelected(true);
                    currentMeasureWay = way;
                }
            });
        }
        if (binding.llMeasureWay.getChildCount() > 0) {
            binding.llMeasureWay.getChildAt(0).setSelected(true);
        }
    }


    private void createPolicyCategoryItems() {
        if (category == null) {
            return;
        }
        int padding15 = DisplayUtils.dip2px(this, 15);
        int padding3 = DisplayUtils.dip2px(this, 3);

        for (final LoginResDTO.SettingsBean.CategoryBean bean : category) {
            final TextView item = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DisplayUtils.dip2px(this, 15);
            item.setPadding(padding15, padding3, padding15, padding3);
            item.setText(bean.getClaimName());
            item.setBackgroundResource(R.drawable.filter_select_status);
            item.setTextColor(getResources().getColorStateList(R.color.filter_select_text));
            item.setGravity(Gravity.CENTER);
            item.setLayoutParams(params);
            item.setTextSize(13);
            binding.llPolicyCategory.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFromPhoto) {
                        ToastUtils.showToast("???????????????????????????");
                        return;
                    }
                    for (int j = 0; j < binding.llPolicyCategory.getChildCount(); j++) {
                        binding.llPolicyCategory.getChildAt(j).setSelected(false);
                    }
                    item.setSelected(true);
                    currentCategory = bean;
                    createMeasureWayItems();
                    if (currentCategory != null && currentCategory.getMeasure_ways() != null) {
                        currentMeasureWay = currentCategory.getMeasure_ways().get(0);
                    }
                }
            });
        }
        if (binding.llPolicyCategory.getChildCount() > 0) {
            binding.llPolicyCategory.getChildAt(0).setSelected(true);
        }
    }

    private void setListener() {
        binding.tvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(ArouterConfig.Order.ORDER_CHOOSE)
                        .withString("words", binding.tvName.getText().toString())
                        .navigation(CreateOrderActivity.this, RESULT_CHOOSE);
            }
        });
        binding.btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromPhoto) {
                    commitData();
                } else {
                    showCamera();
                }
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromPhoto) {
                    showCamera();
                } else {
                    GrDialogUtils.createCommonDialog(CreateOrderActivity.this, "????????????", "??????????????????/???????????????????????????", new CommonDialog.OnDialogClickListener() {
                        @Override
                        public void onClickConfirm(View view) {
                            finish();
                        }

                        @Override
                        public void onClickCancel(View view) {

                        }
                    }).show();
                }
            }
        });
        binding.tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????????????????????????????????
                if (isFromChoose || isFromPhoto) {
                    ToastUtils.showToast("???????????????????????????");
                    return;
                }
                if (!TextUtils.isEmpty(binding.tvDateStart.getText().toString())) {
                    Date parse = null;
                    try {
                        parse = sdf.parse(binding.tvDateStart.getText().toString());
                        showDataPicker("????????????", binding.tvDateStart, parse);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showDataPicker("????????????", binding.tvDateStart, new Date());
                }
            }
        });
        binding.tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????????????????????????????????
                if (isFromChoose || isFromPhoto) {
                    ToastUtils.showToast("???????????????????????????");
                    return;
                }
                if (!TextUtils.isEmpty(binding.tvDateEnd.getText().toString())) {
                    Date parse = null;
                    try {
                        parse = sdf.parse(binding.tvDateEnd.getText().toString());
                        showDataPicker("????????????", binding.tvDateEnd, parse);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showDataPicker("????????????", binding.tvDateEnd, new Date());
                }
            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CHOOSE) {
                InsureListResDTO.ResultListBean result = (InsureListResDTO.ResultListBean) data.getSerializableExtra("result");
                binding.tvCode.setText(result.getInsureId());
                binding.tvName.setText(result.getInsureName());
                binding.tvDateStart.setText(result.getInsureStartTime());
                binding.tvDateEnd.setText(result.getInsureEndTime());
                isFromChoose = true;
                binding.tvName.setEnabled(false);
                binding.tvCode.setEnabled(false);
            } else if (requestCode == RESULT_MEASURE) {
                List<PolicyDetailResDTO.ClaimListBean> forResult = (List<PolicyDetailResDTO.ClaimListBean>) data.getSerializableExtra("result");
                if (forResult != null && forResult.size() > 0) {
                    result.addAll(forResult);
                    mAdapter = new OrderPhotosListAdapter();
                    binding.recyclerView.setAdapter(mAdapter);
                    mAdapter.bindData(true, result);
                    binding.tvTitlePhotos.setVisibility(View.VISIBLE);
                    binding.llCount.setVisibility(View.VISIBLE);
                    binding.tvCount.setText(result.size() + "???");
                    isFromPhoto = true;
                }

                if (isFromPhoto) {
                    binding.btnCancel.setText("????????????");
                    binding.btnMeasure.setText("??????");
                }
            }
        }
    }

    public void showCamera() {
        String name = binding.tvName.getText().toString();
        String code = binding.tvCode.getText().toString();
        String startTime = binding.tvDateStart.getText().toString();
        String endTime = binding.tvDateEnd.getText().toString();
        if (TextUtils.isEmpty(name) | TextUtils.isEmpty(code) |
                TextUtils.isEmpty(startTime) | TextUtils.isEmpty(endTime)) {
            GrDialogUtils.createCommonDialog(CreateOrderActivity.this, "??????", "???????????????????????????????????????", new CommonDialog.OnDialogClickListener() {
                @Override
                public void onClickConfirm(View view) {

                }

                @Override
                public void onClickCancel(View view) {

                }
            }).show();
            return;
        }
        if (currentMeasureWay == null) {
            return;
        }

        Intent intent = new Intent(this, TakePhotoNoDepthActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mButtonItems", (Serializable) currentMeasureWay.getDetails());
        intent.putExtras(bundle);
        intent.putExtra("orderId", binding.tvCode.getText().toString());
        if (mLocation != null) {
            intent.putExtra("latitude", mLocation.getLatitude() + "");
            intent.putExtra("longitude", mLocation.getLongitude() + "");
        }
        if (mAddresses != null && mAddresses.get(0) != null && mAddresses.get(0).getAddressLine(0) != null) {
            intent.putExtra("addr", mAddresses.get(0).getAddressLine(0).toString());
        }
        intent.putExtra("category", currentCategory.getClaimId());
        startActivityForResult(intent, RESULT_MEASURE);
    }

    private void showDataPicker(String title, final TextView textView, final Date date) {
        new DatePickerDialog(this, title, date, new IResultDialog.OnDialogClickListener<Date>() {
            @Override
            public boolean onCancel(@NonNull DialogInterface dialog, @Nullable Date cancelResult) {
                return false;
            }

            @Override
            public boolean onConfirm(@NonNull DialogInterface dialog, @Nullable Date confirmResult) {
                String time = DateTimeUtils.formatDateSimple(confirmResult.getTime());
                textView.setText(time);
                textView.setSelected(true);
                return false;
            }
        }).show();
    }


    private void commitData() {
        CreatePolicyRequestModel requestModel = new CreatePolicyRequestModel();
        requestModel.setToken(GlobalDataManager.getInstance().getToken());
        String claimType = "";
        if (currentCategory != null) {
            claimType = currentCategory.getClaimId();
        }
        LoginResDTO.UserInfoBean info = GlobalDataManager.getInstance().getUserInfo();
        requestModel.setClaimName(info.getUserName());
        requestModel.setInsureName(binding.tvName.getText().toString());
        requestModel.setClaimType(claimType);
        requestModel.setClaimUserId(GlobalDataManager.getInstance().getUserInfo().getUserId());
        requestModel.setInsureStartTime(binding.tvDateStart.getText().toString());
        requestModel.setInsureEndTime(binding.tvDateEnd.getText().toString());
        requestModel.setInsureId(binding.tvCode.getText().toString());
        String measureType = "";
        if (currentMeasureWay != null) {
            measureType = currentMeasureWay.getMeasureType();
        }
        requestModel.setMeasureType(measureType);
        requestModel.setPhone(SharedPreferencesUtils.getStringValue(getContext(), "phone", ""));

        List<CreatePolicyRequestModel.PhotoInfoEntity> pigs = new ArrayList<>();
        if (mAdapter != null) {
            for (PolicyDetailResDTO.ClaimListBean claimListBean : mAdapter.getData()) {
                if (claimListBean == null) {
                    continue;
                }

                CreatePolicyRequestModel.PhotoInfoEntity pig = new CreatePolicyRequestModel.PhotoInfoEntity();
                pig.setPigId(claimListBean.getId());
                if (mAddresses != null && mAddresses.get(0) != null && mAddresses.get(0).getAddressLine(0) != null) {
                    pig.setAddress(mAddresses.get(0).getAddressLine(0).toString());
                }
                if (mLocation != null) {
                    pig.setLatitude(mLocation.getLatitude() + "");
                    pig.setLongitude(mLocation.getLongitude() + "");
                }
                List<PolicyDetailResDTO.ClaimListBean.PigInfoBean> pigInfo = claimListBean.getPigInfo();
                if (pigInfo == null || pigInfo.size() == 0) {
                    continue;
                }

                List<CreatePolicyRequestModel.PhotoInfoEntity.BodyInfoEntity> photos = new ArrayList<>();
                for (PolicyDetailResDTO.ClaimListBean.PigInfoBean pigInfoBean : pigInfo) {
                    if (pigInfoBean == null) {
                        continue;
                    }
                    CreatePolicyRequestModel.PhotoInfoEntity.BodyInfoEntity photo = new CreatePolicyRequestModel.PhotoInfoEntity.BodyInfoEntity();
                    photo.setColumn(pigInfoBean.getColumn());
                    photo.setImgBase64(com.project.module_order.utils.ImageUtils.bitmapPathToString(pigInfoBean.getImgUrl()));
                    photo.setResults(pigInfoBean.getResults());
                    photo.setName(pigInfoBean.getName());
                    photos.add(photo);
                }
                pig.setBody_info(photos);
                pigs.add(pig);
                requestModel.setPhotoInfo(pigs);
            }
        }
        commitPolicy(requestModel);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        //1.?????????????????????
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //2.????????????????????????GPS??????NetWork
        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //?????????GPS
            locationProvider = LocationManager.GPS_PROVIDER;
            Log.v("TAG", "????????????GPS");
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //?????????Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
            Log.v("TAG", "????????????Network");
        } else {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLocation = getLastKnownLocation(locationManager);
            if (mLocation != null) {
                Log.v("TAG", "?????????????????????-????????????" + mLocation.getLongitude() + "   " + mLocation.getLatitude());
                getAddress(mLocation);

            } else {
                //????????????????????????????????????????????????????????????????????????????????????minTime???????????????minDistace
                locationManager.requestLocationUpdates(locationProvider, 300, 1, locationListener);
            }
        } else {
            mLocation = locationManager.getLastKnownLocation(locationProvider);
            if (mLocation != null) {
                Log.v("TAG", "?????????????????????-????????????" + mLocation.getLongitude() + "   " + mLocation.getLatitude());
                getAddress(mLocation);
            } else {
                //????????????????????????????????????????????????????????????????????????????????????minTime???????????????minDistace
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            }
        }
    }

    private Location getLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private LocationListener locationListener = new LocationListener() {
        // Provider??????????????????????????????????????????????????????????????????????????????????????????
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider???enable???????????????????????????GPS?????????
        @Override
        public void onProviderEnabled(String provider) {
        }

        // Provider???disable???????????????????????????GPS?????????
        @Override
        public void onProviderDisabled(String provider) {
        }

        //??????????????????????????????????????????Provider?????????????????????????????????????????????
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                //????????????????????????????????????????????????????????????
                Log.v("TAG", "????????????????????????-????????????" + location.getLongitude() + "   " + location.getLatitude());
            }
        }
    };

    /**
     * ??????????????????:????????????????????????
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
                Log.v("TAG", "?????????????????????" + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * ??????????????????
     */
    private void clearLocalPhotos() {
//        FileUtils.deleteFiles(FILE_PATH);
        FileUtils.deleteFiles(getFilesDir().getAbsolutePath());
    }

    private void commitPolicy(CreatePolicyRequestModel requestModel) {
        if (requestModel == null) {
            return;
        }
        OrderRepositoryImpl.getInstance()
                .submit(requestModel)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<Boolean>bindToLifecycle(this))
                .compose(XXF.<Boolean>bindToErrorNotice())
                .compose(XXF.<Boolean>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("????????????????????????")))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (result) {
                            ToastUtils.showToast("????????????");
                            setResult(AppConstant.RESULT_CODE.RESULT_OK, new Intent());
                            finish();
                        }
                    }
                });
    }

    /**
     * ???????????????id
     *
     * @return
     */
    private long generatePigId() {
        StringBuilder str = new StringBuilder();//?????????????????????
        Random random = new Random();
        //??????????????????????????????????????????
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return Long.valueOf(str.toString());
    }
}
