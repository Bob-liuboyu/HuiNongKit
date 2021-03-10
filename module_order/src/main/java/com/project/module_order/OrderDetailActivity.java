package com.project.module_order;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.project.arch_repo.base.activity.BaseActivity;
import com.project.arch_repo.utils.SystemUtils;
import com.project.config_repo.ArouterConfig;
import com.project.module_order.databinding.OrderActivityDetailBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.CameraPermissionTransformer;

import java.util.Arrays;

import io.reactivex.functions.Consumer;
//import permissions.dispatcher.NeedsPermission;
//import permissions.dispatcher.OnNeverAskAgain;
//import permissions.dispatcher.OnPermissionDenied;
//import permissions.dispatcher.OnShowRationale;
//import permissions.dispatcher.PermissionRequest;

/**
 * @fileName: OrderDetailActivity
 * @author: liuboyu
 * @date: 2021/3/10 2:50 PM
 * @description: 订单详情
 */
public class OrderDetailActivity extends BaseActivity {
//    private OrderActivityDetailBinding binding;
//    private static final int REQUEST_CODE_SETTING = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = OrderActivityDetailBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        setListener();
//    }
//
//    public void setListener() {
//        if (binding == null) {
//            return;
//        }
//        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCamera();
//            }
//        });
//    }
//
//    @NeedsPermission(Manifest.permission.CAMERA)
//    public void showCamera() {
//    }
//
//    @OnShowRationale(Manifest.permission.CAMERA)
//    public void showRationale(final PermissionRequest permissionRequest) {
//        permissionRequest.proceed();
//    }
//
//    @OnNeverAskAgain(Manifest.permission.CAMERA)
//    public void neverAsk() {
//        Toast.makeText(this, "您拒绝了相机权限，且不再询问，请前往设置中心授权", Toast.LENGTH_LONG).show();
//    }
//
//    @OnPermissionDenied(Manifest.permission.CAMERA)
//    public void denied() {
//        Toast.makeText(this, "您拒绝了相机权限，该功能不可用", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
}
