package com.project.huinongkit.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project.arch_repo.base.dialog.BaseDialog;
import com.project.arch_repo.utils.DateTimeUtils;
import com.project.arch_repo.widget.DatePickerDialog;
import com.project.huinongkit.R;
import com.project.huinongkit.databinding.MainDialogSelectFilterBinding;
import com.project.huinongkit.model.SelectFilterModel;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.utils.ToastUtils;

import java.util.Date;

/**
 * @fileName: SelectFilterDialog
 * @author: liuboyu
 * @date: 2021/3/16 5:18 PM
 * @description: 筛选dialog
 */
public class SelectFilterDialog extends BaseDialog<SelectFilterModel> {
    MainDialogSelectFilterBinding binding;
    private FragmentActivity activity;

    public SelectFilterDialog(@NonNull Context context, FragmentActivity activity, @Nullable OnDialogClickListener<SelectFilterModel> onDialogClickListener) {
        super(context, onDialogClickListener);
        this.activity = activity;
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
                confirm(new SelectFilterModel("ok", "ok"));
            }
        });

        binding.okCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(new SelectFilterModel("cancel", "cancel"));
            }
        });

        binding.tvStatusDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvStatusDone.setSelected(true);
                binding.tvStatusTodo.setSelected(false);
            }
        });

        binding.tvStatusTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvStatusDone.setSelected(false);
                binding.tvStatusTodo.setSelected(true);
            }
        });

        binding.tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDataPicker();
            }
        });
        binding.tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDataPicker();
            }
        });
    }

    private void showStartDataPicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new OnDialogClickListener<Date>() {
            @Override
            public boolean onCancel(@NonNull DialogInterface dialog, @Nullable Date cancelResult) {
                return false;
            }

            @Override
            public boolean onConfirm(@NonNull DialogInterface dialog, @Nullable Date confirmResult) {
                String time = DateTimeUtils.formatDateSimple(confirmResult.getTime());
                binding.tvDateStart.setText(time);
                binding.tvDateStart.setSelected(true);
                return false;
            }
        });
        datePickerDialog.initData(new Date());
        datePickerDialog.show();
    }

    private void showEndDataPicker() {
        new DatePickerDialog(activity, new IResultDialog.OnDialogClickListener<Date>() {
            @Override
            public boolean onCancel(@NonNull DialogInterface dialog, @Nullable Date cancelResult) {
                return false;
            }

            @Override
            public boolean onConfirm(@NonNull DialogInterface dialog, @Nullable Date confirmResult) {
                String time = DateTimeUtils.formatDateSimple(confirmResult.getTime());
                binding.tvDateEnd.setText(time);
                binding.tvDateEnd.setSelected(true);
                return false;
            }
        }).show();
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
        window.setDimAmount(0.5f);
    }
}
