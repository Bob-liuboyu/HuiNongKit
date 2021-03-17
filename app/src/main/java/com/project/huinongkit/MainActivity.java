package com.project.huinongkit;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.config_repo.ArouterConfig;
import com.project.huinongkit.databinding.MainActivityMainBinding;
import com.project.huinongkit.fragment.MainFragment;
import com.project.huinongkit.fragment.MineFragment;
import com.xxf.arch.widget.BaseFragmentAdapter;

import java.util.Arrays;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
@Route(path = ArouterConfig.Main.MAIN)
public class MainActivity extends BaseActivity {
    private MainActivityMainBinding mBinding;
    private BaseFragmentAdapter mBaseFragmentAdapter;
    private MainFragment mainFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mBaseFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        mBaseFragmentAdapter.bindData(true, Arrays.asList(mainFragment, mineFragment));
        mBaseFragmentAdapter.bindTitle(true, Arrays.asList("首页", "我的"));
        mBinding.mViewPager.setAdapter(mBaseFragmentAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.mViewPager);
        mBinding.btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityPermissionsDispatcher.gotoCameraWithCheck(MainActivity.this);
            }
        });
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void gotoCamera() {
        ARouter.getInstance().build(ArouterConfig.Order.ORDER_BODY_3D)
                .navigation();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog("permission_camera_rationale", request);
    }

    @OnShowRationale({Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS})
    void showRationaleForContact(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        showRationaleDialog("permission_contacts_rationale", request);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, "permission_camera_denied", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
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
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}