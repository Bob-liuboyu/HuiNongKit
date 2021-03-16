package com.project.huinongkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.project.arch_repo.base.dialog.BaseDialog;
import com.project.huinongkit.databinding.MainDialogSelectFilterBinding;
import com.project.huinongkit.model.SelectFilterModel;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.dialog.Void;
import com.xxf.arch.dialog.XXFDialog;
import com.xxf.arch.utils.ToastUtils;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @fileName: SelectFilterDialog
 * @author: liuboyu
 * @date: 2021/3/16 5:18 PM
 * @description: 筛选dialog
 */
public class SelectFilterDialog extends BaseDialog<SelectFilterModel> {
    MainDialogSelectFilterBinding binding;

    public SelectFilterDialog(@NonNull Context context, @Nullable OnDialogClickListener<SelectFilterModel> onDialogClickListener) {
        super(context, onDialogClickListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainDialogSelectFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initListener();
    }

    private void initListener() {
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm(new SelectFilterModel("ok","ok"));
            }
        });

        binding.okCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(new SelectFilterModel("cancel","cancel"));
            }
        });
    }

    private void initView() {
        //全屏 透明
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        window.setAttributes(layoutParams);
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        window.getAttributes().windowAnimations = R.style.arch_AnimBottomDialog;
    }
}
