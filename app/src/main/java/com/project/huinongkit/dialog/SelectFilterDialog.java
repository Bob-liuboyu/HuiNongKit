package com.project.huinongkit.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.project.arch_repo.base.dialog.BaseDialog;
import com.project.arch_repo.utils.DateTimeUtils;
import com.project.arch_repo.widget.DatePickerDialog;
import com.project.huinongkit.R;
import com.project.huinongkit.databinding.MainDialogSelectFilterBinding;
import com.project.common_resource.SelectFilterModel;
import com.xxf.arch.dialog.IResultDialog;

import java.util.Date;

/**
 * @fileName: SelectFilterDialog
 * @author: liuboyu
 * @date: 2021/3/16 5:18 PM
 * @description: 筛选dialog
 */
public class SelectFilterDialog extends BaseDialog<SelectFilterModel> {
    private MainDialogSelectFilterBinding binding;
    private FragmentActivity activity;
    private SelectFilterModel mFilterModel;

    public SelectFilterDialog(@NonNull Context context, FragmentActivity activity, SelectFilterModel filterModel, @Nullable OnDialogClickListener<SelectFilterModel> onDialogClickListener) {
        super(context, onDialogClickListener);
        this.activity = activity;
        this.mFilterModel = filterModel;
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
                confirm(mFilterModel);
            }
        });

        binding.okCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(mFilterModel);
            }
        });

        binding.tvStatusDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvStatusDone.setSelected(true);
                binding.tvStatusTodo.setSelected(false);
                mFilterModel.setStatus("tvStatusDone");
            }
        });

        binding.tvStatusTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvStatusDone.setSelected(false);
                binding.tvStatusTodo.setSelected(true);
                mFilterModel.setStatus("tvStatusTodo");
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, "起始日期", new OnDialogClickListener<Date>() {
            @Override
            public boolean onCancel(@NonNull DialogInterface dialog, @Nullable Date cancelResult) {
                binding.tvDateStart.setText("");
                binding.tvDateStart.setSelected(false);
                return false;
            }

            @Override
            public boolean onConfirm(@NonNull DialogInterface dialog, @Nullable Date confirmResult) {
                String time = DateTimeUtils.formatDateSimple(confirmResult.getTime());
                binding.tvDateStart.setText(time);
                binding.tvDateStart.setSelected(true);
                mFilterModel.setStartDate(time);
                return false;
            }
        });
        datePickerDialog.initData(new Date());
        datePickerDialog.show();
    }

    private void showEndDataPicker() {
        new DatePickerDialog(activity, "结束日期", new IResultDialog.OnDialogClickListener<Date>() {
            @Override
            public boolean onCancel(@NonNull DialogInterface dialog, @Nullable Date cancelResult) {
                binding.tvDateEnd.setText("");
                binding.tvDateEnd.setSelected(false);
                return false;
            }

            @Override
            public boolean onConfirm(@NonNull DialogInterface dialog, @Nullable Date confirmResult) {
                String time = DateTimeUtils.formatDateSimple(confirmResult.getTime());
                binding.tvDateEnd.setText(time);
                binding.tvDateEnd.setSelected(true);
                mFilterModel.setEndDate(time);
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

    private void initData() {
        if (mFilterModel == null) {
            mFilterModel = new SelectFilterModel();
        }
        binding.tvDateStart.setText(mFilterModel.getStartDate());
        binding.tvDateEnd.setText(mFilterModel.getEndDate());
        if (mFilterModel.getStatus().equals("tvStatusDone")) {
            binding.tvStatusTodo.setSelected(false);
            binding.tvStatusDone.setSelected(true);
        } else if (mFilterModel.getStatus().equals("tvStatusTodo")) {
            binding.tvStatusTodo.setSelected(true);
            binding.tvStatusDone.setSelected(false);
        } else {
            binding.tvStatusTodo.setSelected(false);
            binding.tvStatusDone.setSelected(false);
        }
    }
}
