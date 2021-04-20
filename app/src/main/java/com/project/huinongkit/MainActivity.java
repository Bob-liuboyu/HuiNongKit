package com.project.huinongkit;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.activity.BaseTitleBarActivity;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.databinding.MainActivityMainBinding;
import com.project.huinongkit.fragment.MainFragment;
import com.project.huinongkit.fragment.MineFragment;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.arch.widget.BaseFragmentAdapter;
import com.xxf.view.utils.StatusBarUtils;

import java.util.Arrays;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
@Route(path = ArouterConfig.Main.MAIN)
public class MainActivity extends BaseTitleBarActivity {
    private MainActivityMainBinding mBinding;
    private BaseFragmentAdapter mBaseFragmentAdapter;
    private MainFragment mainFragment;
    private MineFragment mineFragment;
    public static final int CODE_REQUEST_CREATE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = 0xFFFFFFFF;
        StatusBarUtils.compatStatusBarForM(this, false, color);
        mBinding = MainActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initFragment();
        initView();
    }

    private void initFragment() {
        mainFragment = MainFragment.newInstance();
        mineFragment = MineFragment.newInstance();
    }


    private void initView() {
        getTitleBar().setTitleBarTitle("慧农保").setTitleBarLeftIcon(null, null);
        mBaseFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        mBaseFragmentAdapter.bindData(true, Arrays.asList(mainFragment, mineFragment));
        mBinding.mViewPager.setAdapter(mBaseFragmentAdapter);
        mBinding.tvHome.setSelected(true);
        mBinding.tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBinding.mViewPager.getCurrentItem() == 0){
                    mainFragment.refreshData();
                    return;
                }
                mBinding.mViewPager.setCurrentItem(0);
                mBinding.tvHome.setSelected(true);
                mBinding.tvMine.setSelected(false);
            }
        });
        mBinding.tvMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.mViewPager.setCurrentItem(1);
                mBinding.tvHome.setSelected(false);
                mBinding.tvMine.setSelected(true);
            }
        });
        mBinding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.showCameraWithCheck(MainActivity.this);
            }
        });
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void showCamera() {
//        if (!isHuawei()) {
//            ToastUtils.showToast("当前应用只支持华为设备！");
//            return;
//        }
        ARouter.getInstance().build(ArouterConfig.Order.ORDER_CREATE)
                .navigation(this, CODE_REQUEST_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        if (requestCode == CODE_REQUEST_CREATE && resultCode == Activity.RESULT_OK) {
            mainFragment.refreshData();
        }
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void showRationaleForCamera(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog("使用此功能需要您的拍照、储存卡权限", request);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
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
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    public boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }
}