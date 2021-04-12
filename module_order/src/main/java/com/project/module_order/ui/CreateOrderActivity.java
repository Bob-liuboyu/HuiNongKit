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
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.DatePickerDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.requestModel.CreatePolicyRequestModel;
import com.project.common_resource.response.InsureListResDTO;
import com.project.common_resource.response.LoginResDTO;
import com.project.common_resource.response.PolicyDetailResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.adapter.OrderPhotosListAdapter;
import com.project.module_order.databinding.OrderActivityCreateBinding;
import com.project.module_order.source.impl.OrderRepositoryImpl;
import com.xxf.arch.XXF;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.view.utils.StatusBarUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.project.common_resource.global.ConstantData.FILE_PATH;

/**
 * @fileName: CreateOrderActivity
 * @author: liuboyu
 * @date: 2021/3/15 11:32 AM
 * @description: 创建订单
 */
@Route(path = ArouterConfig.Order.ORDER_CREATE)
public class CreateOrderActivity extends BaseActivity {

    protected OrderActivityCreateBinding binding;
    protected OrderPhotosListAdapter mAdapter;
    //选择
    protected static final int RESULT_CHOOSE = 100;
    //开始测量
    protected static final int RESULT_MEASURE = 101;
    protected List<PolicyDetailResDTO.ClaimListBean> result = new ArrayList<>();
    private SimpleDateFormat sdf;
    private boolean isFromChoose;
    private List<Address> mAddresses;
    private Location mLocation;
    private LocationManager locationManager;
    private String locationProvider = null;
    /**
     * 测量方式
     */
    private List<LoginResDTO.SettingsBean.CategoryBean> category;
    private List<LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean> measureWays;

    private LoginResDTO.SettingsBean.CategoryBean currentCategory;
    private LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean currentMeasureWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = 0xFFFFFFFF;
        StatusBarUtils.compatStatusBarForM(this, false, color);
        binding = OrderActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        createMeasureWayItems();
        createPolicyCategoryItems();
        setListener();
        getLocation();
        clearLocalPhotos();
    }

    private void initView() {
        binding.llPolicyCategory.setVisibility(View.VISIBLE);
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        category = GlobalDataManager.getInstance().getSettings().getCategory();
        binding.tvTitle.setText("理赔登记");
        binding.tvRightText.setText("提交");
        if (category != null) {
            currentCategory = category.get(0);
        }
        if (currentCategory != null && currentCategory.getMeasure_ways() != null && currentCategory.getMeasure_ways().size() > 0) {
            measureWays = currentCategory.getMeasure_ways();
            currentMeasureWay = measureWays.get(0);
        }
        LoginResDTO.UserInfoBean info = GlobalDataManager.getInstance().getUserInfo();
        binding.tvMaster.setText(info != null ? info.getUserName() : "");
    }

    private void createMeasureWayItems() {
        if (measureWays == null) {
            return;
        }
        int padding15 = DisplayUtils.dip2px(this, 15);
        int padding3 = DisplayUtils.dip2px(this, 3);

        for (final LoginResDTO.SettingsBean.CategoryBean.MeasureWaysBean way : measureWays) {
            final TextView item = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = DisplayUtils.dip2px(this, 15);
            item.setPadding(padding15, padding3, padding15, padding3);
            item.setBackgroundResource(R.drawable.filter_select_status);
            item.setTextColor(getResources().getColorStateList(R.color.filter_select_text));
            item.setText(way.getMeasureName());
            item.setGravity(Gravity.CENTER);
            item.setLayoutParams(params);
            binding.llMeasureWay.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            binding.llPolicyCategory.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < binding.llPolicyCategory.getChildCount(); j++) {
                        binding.llPolicyCategory.getChildAt(j).setSelected(false);
                    }
                    item.setSelected(true);
                    currentCategory = bean;
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
                        .navigation(CreateOrderActivity.this, RESULT_CHOOSE);
            }
        });
        binding.btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCamera();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrDialogUtils.createCommonDialog(CreateOrderActivity.this, "确认返回", "已拍摄的测重/测长照片将不会保存", new CommonDialog.OnDialogClickListener() {
                    @Override
                    public void onClickConfirm(View view) {
                        finish();
                    }

                    @Override
                    public void onClickCancel(View view) {

                    }
                }).show();
            }
        });
        binding.tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自动填写的数据，不允许修改
                if (isFromChoose) {
                    return;
                }
                if (!TextUtils.isEmpty(binding.tvDateStart.getText().toString())) {
                    Date parse = null;
                    try {
                        parse = sdf.parse(binding.tvDateStart.getText().toString());
                        showDataPicker("起始日期", binding.tvDateStart, parse);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showDataPicker("起始日期", binding.tvDateStart, new Date());
                }
            }
        });
        binding.tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自动填写的数据，不允许修改
                if (isFromChoose) {
                    return;
                }
                if (!TextUtils.isEmpty(binding.tvDateEnd.getText().toString())) {
                    Date parse = null;
                    try {
                        parse = sdf.parse(binding.tvDateEnd.getText().toString());
                        showDataPicker("结束日期", binding.tvDateEnd, parse);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showDataPicker("结束日期", binding.tvDateEnd, new Date());
                }
            }
        });
        binding.tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitData();
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
            } else if (requestCode == RESULT_MEASURE) {
                List<PolicyDetailResDTO.ClaimListBean> forResult = (List<PolicyDetailResDTO.ClaimListBean>) data.getSerializableExtra("result");
                if (forResult != null && forResult.size() > 0) {
                    result.addAll(forResult);
                    mAdapter = new OrderPhotosListAdapter();
                    binding.recyclerView.setAdapter(mAdapter);
                    mAdapter.bindData(true, result);
                    binding.tvTitlePhotos.setVisibility(View.VISIBLE);
                    binding.llCount.setVisibility(View.VISIBLE);
                    binding.tvCount.setText(result.size() + "只");
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
            GrDialogUtils.createCommonDialog(CreateOrderActivity.this, "提示", "请先填写信息后在进行测量！", new CommonDialog.OnDialogClickListener() {
                @Override
                public void onClickConfirm(View view) {

                }

                @Override
                public void onClickCancel(View view) {

                }
            }).show();
            return;
        }

        Intent intent = new Intent(this, TakePhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mButtonItems", (Serializable) currentMeasureWay.getDetails());
        intent.putExtras(bundle);
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
        // FIXME: 2021-04-07 估计没有用
        requestModel.setClaimId("asdfadfads");
        String claimName = "";
        for (int i = 0; i < binding.llPolicyCategory.getChildCount(); i++) {
            TextView category = (TextView) binding.llPolicyCategory.getChildAt(i);
            if (category != null && category.isSelected()) {
                claimName = category.getText().toString();
                break;
            }
        }
        requestModel.setClaimName(claimName);

        // FIXME: 2021-04-07 估计没有用
        requestModel.setClaimType("claimType");
        requestModel.setClaimUserId(GlobalDataManager.getInstance().getUserInfo().getUserId());
        requestModel.setInsureStartTime(binding.tvDateStart.getText().toString());
        requestModel.setInsureEndTime(binding.tvDateEnd.getText().toString());
        requestModel.setInsureId(binding.tvCode.getText().toString());
        String measureType = "";
        for (int i = 0; i < binding.llMeasureWay.getChildCount(); i++) {
            TextView category = (TextView) binding.llMeasureWay.getChildAt(i);
            if (category != null && category.isSelected()) {
                measureType = category.getText().toString();
                break;
            }
        }
        requestModel.setMeasureType(measureType);
        // FIXME: 2021-04-07 哪里来的phone
        requestModel.setPhone("15011447166");

        List<CreatePolicyRequestModel.PhotoInfoEntity> pigs = new ArrayList<>();
        for (PolicyDetailResDTO.ClaimListBean claimListBean : mAdapter.getData()) {
            if (claimListBean == null) {
                continue;
            }

            CreatePolicyRequestModel.PhotoInfoEntity pig = new CreatePolicyRequestModel.PhotoInfoEntity();
            pig.setPigId("pigId");
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
                photo.setImgBase64(com.project.module_order.utils.ImageUtils.bitmapToString(pigInfoBean.getImgUrl()));
                photo.setResults("result");
                photos.add(photo);
            }
            pig.setBody_info(photos);
            pigs.add(pig);
            requestModel.setPhotoInfo(pigs);
        }

        commitPolicy(requestModel);
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
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mLocation = locationManager.getLastKnownLocation(locationProvider);
            if (mLocation != null) {
                Log.v("TAG", "获取上次的位置-经纬度：" + mLocation.getLongitude() + "   " + mLocation.getLatitude());
                getAddress(mLocation);

            } else {
                //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            }
        } else {
            mLocation = locationManager.getLastKnownLocation(locationProvider);
            if (mLocation != null) {
                Log.v("TAG", "获取上次的位置-经纬度：" + mLocation.getLongitude() + "   " + mLocation.getLatitude());
                getAddress(mLocation);
            } else {
                //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
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
                Log.v("TAG", "获取地址信息：" + result.toString());
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
     * 清空本地图片
     */
    private void clearLocalPhotos() {
        FileUtils.deleteFiles(FILE_PATH);
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
                                .setLoadingNotice("内容较多，请稍后～")))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                    }
                });
    }
}
