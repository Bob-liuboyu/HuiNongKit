package com.project.module_order.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.DateTimeUtils;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.DatePickerDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.common_resource.OrderModel;
import com.project.common_resource.OrderPhotoListModel;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.response.LoginResDTO;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.adapter.OrderPhotosListAdapter;
import com.project.module_order.databinding.OrderActivityCreateBinding;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.StatusBarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * @fileName: CreateOrderActivity
 * @author: liuboyu
 * @date: 2021/3/15 11:32 AM
 * @description: 创建订单
 */
@RuntimePermissions
@Route(path = ArouterConfig.Order.ORDER_CREATE)
public class CreateOrderActivity extends BaseActivity {

    protected OrderActivityCreateBinding binding;
    protected OrderPhotosListAdapter mAdapter;
    //选择
    protected static final int RESULT_CHOOSE = 100;
    //开始测量
    protected static final int RESULT_MEASURE = 101;
    protected List<OrderPhotoListModel> result = new ArrayList<>();
    private SimpleDateFormat sdf;
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
    }

    private void initView() {
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
            params.leftMargin = DisplayUtils.dip2px(this, 15);
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
                CreateOrderActivityPermissionsDispatcher.showCameraWithCheck(CreateOrderActivity.this);
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
                ToastUtils.showToast("提交理赔成功");
                finish();
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
                OrderModel result = (OrderModel) data.getSerializableExtra("result");
                binding.tvCode.setText(result.getId());
                binding.tvName.setText(result.getName());
                binding.tvDateStart.setText(result.getStartData());
                binding.tvDateEnd.setText(result.getEndData());
            } else if (requestCode == RESULT_MEASURE) {
                List<OrderPhotoListModel> forResult = (List<OrderPhotoListModel>) data.getSerializableExtra("result");
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

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showCamera() {
        if (!isHuawei()) {
            ToastUtils.showToast("当前应用只支持华为设备！");
            return;
        }
        String name = binding.tvName.getText().toString();
        String code = binding.tvCode.getText().toString();
        String startTime = binding.tvDateStart.getText().toString();
        String endTime = binding.tvDateEnd.getText().toString();
        if (TextUtils.isEmpty(name) | TextUtils.isEmpty(code) |
                TextUtils.isEmpty(startTime) | TextUtils.isEmpty(endTime)) {
            ToastUtils.showToast("请先填写信息后在进行测量！");
            return;
        }
        ARouter.getInstance().build(ArouterConfig.Order.ORDER_TAKE_PHOTO)
                .navigation(CreateOrderActivity.this, RESULT_MEASURE);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForCamera(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog("使用此功能需要您的拍照、储存卡权限", request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void onCameraNeverAskAgain() {
        Toast.makeText(this, "请到设置中开启拍照、储存卡权限", Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(String message, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("允许", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(message)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CreateOrderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

    public boolean isHuawei() {

        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }

    }

}
