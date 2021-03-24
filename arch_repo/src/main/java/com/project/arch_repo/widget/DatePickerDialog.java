package com.project.arch_repo.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.project.arch_repo.R;
import com.project.arch_repo.base.dialog.BaseDialog;
import com.project.arch_repo.databinding.ArchDialogDatapickerBinding;

import java.util.Calendar;
import java.util.Date;

/**
 * @author liuboyu  E-mail:545777678@qq.com
 * @Date 2019-06-27
 * @Description 时间选择弹窗
 */
public class DatePickerDialog extends BaseDialog<Date> {

    private ArchDialogDatapickerBinding mBinding;

    /**
     * 选择监听
     */
    private Date initDate;

    public DatePickerDialog(@NonNull Context context, @Nullable OnDialogClickListener<Date> onDialogClickListener) {
        super(context, onDialogClickListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ArchDialogDatapickerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
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


    /**
     * 初始化
     */
    private void initView() {
        mBinding.mDatePicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        __initDate2UI();
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                instance.set(mBinding.mDatePicker.getYear(),
                        mBinding.mDatePicker.getMonth(),
                        mBinding.mDatePicker.getDayOfMonth(),
                        mBinding.mTimePicker.getCurrentHour(),
                        mBinding.mTimePicker.getCurrentMinute(),
                        0);
                cancel(instance.getTime());
            }
        });
        mBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                instance.set(mBinding.mDatePicker.getYear(),
                        mBinding.mDatePicker.getMonth(),
                        mBinding.mDatePicker.getDayOfMonth(),
                        mBinding.mTimePicker.getCurrentHour(),
                        mBinding.mTimePicker.getCurrentMinute(),
                        0);
                confirm(instance.getTime());
            }
        });

        //24小时制
        mBinding.mTimePicker.setIs24HourView(true);
    }


    private void __initDate2UI() {
        if (mBinding == null || initDate == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initDate);
        mBinding.mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        mBinding.mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mBinding.mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    /**
     * 初始化显示日期
     *
     * @param initDate
     */
    public DatePickerDialog initData(Date initDate) {
        this.initDate = initDate;
        __initDate2UI();
        return this;
    }
}
