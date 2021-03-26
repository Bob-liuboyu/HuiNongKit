package com.project.module_order.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.activity.BaseTitleBarActivity;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.widget.CommonDialog;
import com.project.arch_repo.widget.GrDialogUtils;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.R;
import com.project.module_order.adapter.OrderPhotosListAdapter;
import com.project.module_order.databinding.OrderActivityCreateBinding;
import com.xxf.view.utils.StatusBarUtils;

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
public class CreateOrderActivity extends BaseTitleBarActivity {

    protected OrderActivityCreateBinding binding;
    protected OrderPhotosListAdapter mAdapter;
    protected static final int RESULT_CHOOSE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitleBarTitle("理赔登记");
        int color = 0xFFFFFFFF;
        StatusBarUtils.compatStatusBarForM(this, false, color);
        binding = OrderActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createMeasureWayItems();
        createPolicyCategoryItems();
        setListener();
    }

    private void createMeasureWayItems() {
        int padding15 = DisplayUtils.dip2px(this, 15);
        int padding3 = DisplayUtils.dip2px(this, 3);

        TextView item1 = new TextView(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        item1.setText("测重");
        item1.setPadding(padding15, padding3, padding15, padding3);
        item1.setGravity(Gravity.CENTER);
        item1.setBackgroundResource(R.drawable.filter_select_status);
        item1.setTextColor(getResources().getColor(R.color.filter_select_text));
        item1.setLayoutParams(params1);

        TextView item2 = new TextView(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.leftMargin = DisplayUtils.dip2px(this, 15);
        item2.setPadding(padding15, padding3, padding15, padding3);
        item2.setBackgroundResource(R.drawable.filter_select_status);
        item2.setTextColor(getResources().getColor(R.color.filter_select_text));
        item2.setText("测长");
        item2.setGravity(Gravity.CENTER);
        item2.setLayoutParams(params2);

        binding.llMeasureWay.addView(item1);
        binding.llMeasureWay.addView(item2);
    }


    private void createPolicyCategoryItems() {
        int padding15 = DisplayUtils.dip2px(this, 15);
        int padding3 = DisplayUtils.dip2px(this, 3);

        TextView item1 = new TextView(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        item1.setText("母猪");
        item1.setPadding(padding15, padding3, padding15, padding3);
        item1.setGravity(Gravity.CENTER);
        item1.setBackgroundResource(R.drawable.filter_select_status);
        item1.setTextColor(getResources().getColor(R.color.filter_select_text));
        item1.setLayoutParams(params1);

        TextView item2 = new TextView(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.leftMargin = DisplayUtils.dip2px(this, 15);
        item2.setPadding(padding15, padding3, padding15, padding3);
        item2.setText("公猪");
        item2.setBackgroundResource(R.drawable.filter_select_status);
        item2.setTextColor(getResources().getColor(R.color.filter_select_text));
        item2.setGravity(Gravity.CENTER);
        item2.setLayoutParams(params2);

        binding.llPolicyCategory.addView(item1);
        binding.llPolicyCategory.addView(item2);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == RESULT_CHOOSE && resultCode == 1) {
            binding.tvCode.setText(data.getStringExtra("result"));
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void showCamera() {
        ARouter.getInstance().build(ArouterConfig.Order.ORDER_TAKE_PHOTO)
                .navigation();
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForCamera(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog("permissions_rationale", request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, "permission_camera_denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void onCameraNeverAskAgain() {
        Toast.makeText(this, "permission_camera_never_askagain", Toast.LENGTH_SHORT).show();
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

}
