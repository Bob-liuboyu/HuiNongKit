package com.project.arch_repo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.project.arch_repo.R;
import com.project.arch_repo.base.dialog.BaseDialog;
import com.project.arch_repo.databinding.ArchDialogDatapickerBinding;
import com.project.common_resource.SelectFilterModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    private String title;
    private Date date;


    public DatePickerDialog(@NonNull Context context, String title, Date date, @Nullable OnDialogClickListener<Date> onDialogClickListener) {
        super(context, onDialogClickListener);
        this.title = title;
        this.date = date;
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

        if (date != null) {
            __initDate2UI(date);
        }

    }


    /**
     * 初始化
     */
    private void initView() {
        mBinding.mDatePicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mBinding.tvTitle.setText(title);
        __initDate2UI(new Date());
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


    private void __initDate2UI(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mBinding.mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        mBinding.mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mBinding.mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

}
