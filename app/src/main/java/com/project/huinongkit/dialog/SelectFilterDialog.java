package com.project.huinongkit.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.arch_repo.base.dialog.BaseDialog;
import com.project.arch_repo.utils.DateTimeUtils;
import com.project.arch_repo.utils.DisplayUtils;
import com.project.arch_repo.widget.DatePickerDialog;
import com.project.common_resource.SelectFilterModel;
import com.project.common_resource.global.GlobalDataManager;
import com.project.common_resource.response.LoginResDTO;
import com.project.huinongkit.R;
import com.project.huinongkit.databinding.MainDialogSelectFilterBinding;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private SimpleDateFormat sdf;

    public SelectFilterDialog(@NonNull Context context, FragmentActivity activity, SelectFilterModel filterModel, @Nullable OnDialogClickListener<SelectFilterModel> onDialogClickListener) {
        super(context, onDialogClickListener);
        this.activity = activity;
        this.mFilterModel = filterModel;
        if (this.mFilterModel == null) {
            this.mFilterModel = new SelectFilterModel();
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
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
                commit();
            }
        });

        binding.okCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(mFilterModel);
            }
        });
        // 0:待处理 1：已理赔
        binding.tvStatusDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvStatusDone.setSelected(!binding.tvStatusDone.isSelected());
                binding.tvStatusTodo.setSelected(false);
                if (binding.tvStatusDone.isSelected()) {
                    mFilterModel.setClaimStatus(SelectFilterModel.STATUS_DONE);
                } else {
                    mFilterModel.setClaimStatus("");
                }
            }
        });

        binding.tvStatusTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvStatusDone.setSelected(false);
                binding.tvStatusTodo.setSelected(!binding.tvStatusTodo.isSelected());
                if (binding.tvStatusTodo.isSelected()) {
                    mFilterModel.setClaimStatus(SelectFilterModel.STATUS_NO);
                } else {
                    mFilterModel.setClaimStatus("");
                }
            }
        });

        binding.tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mFilterModel.getSubmitStartDate())) {
                    try {
                        Date parse = sdf.parse(mFilterModel.getSubmitStartDate());
                        showStartDataPicker(parse);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showStartDataPicker(new Date());
                }
            }
        });
        binding.tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mFilterModel.getSubmitEndDate())) {
                    try {
                        Date parse = sdf.parse(mFilterModel.getSubmitEndDate());
                        showEndDataPicker(parse);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    showEndDataPicker(new Date());
                }
            }
        });
    }

    private void showStartDataPicker(Date date) {
        new DatePickerDialog(activity, "起始日期", date, new OnDialogClickListener<Date>() {
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
                mFilterModel.setSubmitStartDate(time);
                return false;
            }
        }).show();
    }

    private void createMeasureWayItems() {
        List<LoginResDTO.SettingsBean.CategoryBean> category = GlobalDataManager.getInstance().getSettings().getCategory();
        if (category == null) {
            return;
        }
        for (final LoginResDTO.SettingsBean.CategoryBean way : category) {
            if (way == null) {
                continue;
            }
            final TextView item = new TextView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DisplayUtils.dip2px(getContext(), 70), DisplayUtils.dip2px(getContext(), 28));
            params.rightMargin = DisplayUtils.dip2px(getContext(), 21);
            item.setBackgroundResource(com.project.module_order.R.drawable.filter_select_status);
            item.setTextColor(getContext().getResources().getColorStateList(com.project.module_order.R.color.filter_select_text));
            item.setText(way.getClaimName());
            item.setGravity(Gravity.CENTER);
            item.setLayoutParams(params);
            item.setTextSize(14);
            binding.llCategory.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.isSelected()) {
                        for (int i = 0; i < binding.llCategory.getChildCount(); i++) {
                            binding.llCategory.getChildAt(i).setSelected(false);
                        }
                        item.setSelected(true);
                        mFilterModel.setClaimType(way.getClaimId());
                    } else {
                        for (int i = 0; i < binding.llCategory.getChildCount(); i++) {
                            binding.llCategory.getChildAt(i).setSelected(false);
                        }
                        mFilterModel.setClaimType("");
                    }
                }
            });
        }
    }

    private void showEndDataPicker(Date date) {
        new DatePickerDialog(activity, "结束日期", date, new IResultDialog.OnDialogClickListener<Date>() {
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
                mFilterModel.setSubmitEndDate(time);
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
            return;
        }
        if (!TextUtils.isEmpty(mFilterModel.getSubmitStartDate())) {
            binding.tvDateStart.setText(mFilterModel.getSubmitStartDate());
        }
        if (!TextUtils.isEmpty(mFilterModel.getSubmitEndDate())) {
            binding.tvDateEnd.setText(mFilterModel.getSubmitEndDate());
        }
        if (SelectFilterModel.STATUS_DONE.equals(mFilterModel.getClaimStatus())) {
            binding.tvStatusTodo.setSelected(false);
            binding.tvStatusDone.setSelected(true);
        } else if (SelectFilterModel.STATUS_NO.equals(mFilterModel.getClaimStatus())) {
            binding.tvStatusTodo.setSelected(true);
            binding.tvStatusDone.setSelected(false);
        } else {
            binding.tvStatusTodo.setSelected(false);
            binding.tvStatusDone.setSelected(false);
        }

        createMeasureWayItems();
    }

    private void commit() {
        if (!binding.tvStatusTodo.isSelected() && !binding.tvStatusDone.isSelected()
                && TextUtils.isEmpty(mFilterModel.getSubmitStartDate())
                && TextUtils.isEmpty(mFilterModel.getSubmitEndDate())) {
            ToastUtils.showToast("搜索条件有误，请重新填写");
            return;
        }

        if ((TextUtils.isEmpty(mFilterModel.getSubmitStartDate()) && !TextUtils.isEmpty(mFilterModel.getSubmitEndDate()))
                || (!TextUtils.isEmpty(mFilterModel.getSubmitStartDate()) && TextUtils.isEmpty(mFilterModel.getSubmitEndDate()))) {
            ToastUtils.showToast("起始／结束时间必须同事输入");
            return;
        }

        Date start = null;
        Date end = null;
        if (!TextUtils.isEmpty(mFilterModel.getSubmitStartDate()) && !TextUtils.isEmpty(mFilterModel.getSubmitEndDate())) {
            try {
                start = sdf.parse(mFilterModel.getSubmitStartDate());
                end = sdf.parse(mFilterModel.getSubmitEndDate());
                if (end.compareTo(start) <= 0) {
                    ToastUtils.showToast("结束时间必须大于开始时间");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        confirm(mFilterModel);

    }

    @Override
    public void show() {
        super.show();
        initData();
    }
}
