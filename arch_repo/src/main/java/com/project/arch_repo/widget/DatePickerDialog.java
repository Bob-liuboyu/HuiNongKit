package com.project.arch_repo.widget;

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
public class DatePickerDialog extends BaseDialog<String> {

    private ArchDialogDatapickerBinding mBinding;

    /**
     * 选择监听
     */
    private DataPickerCallBack mCallBack;
    private Date initDate;

    public DatePickerDialog(@NonNull Context context, @Nullable OnDialogClickListener<String> onDialogClickListener) {
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
//        Window win = getDialog().getWindow();
//        // 一定要设置Background，如果不设置，window属性设置无效
//        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        WindowManager.LayoutParams params = win.getAttributes();
//        params.gravity = Gravity.BOTTOM;
//        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        win.setAttributes(params);
//        win.getAttributes().windowAnimations = R.style.arch_AnimBottomDialog;

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
//                if (mCallBack == null) {
//                    dismissAllowingStateLoss();
//                } else if (!mCallBack.onClickCancel(DatePickerDialogFragment.this, instance.getTime())) {
//                    dismissAllowingStateLoss();
//                }
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
//                if (mCallBack == null) {
//                    dismissAllowingStateLoss();
//                } else if (!mCallBack.onClickConfirm(DatePickerDialogFragment.this, instance.getTime())) {
//                    dismissAllowingStateLoss();
//                }
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


    /**
     * 选择监听
     *
     * @param callBack
     */
    public DatePickerDialog setDataPickerCallBack(DataPickerCallBack callBack) {
        this.mCallBack = callBack;
        return this;
    }

    public interface DataPickerCallBack {
        /**
         * @param dialog
         * @param date
         * @return true 标示自己关闭页面,否则自动关闭
         */
        boolean onClickCancel(@NonNull DialogFragment dialog, @NonNull Date date);

        /**
         * @param dialog
         * @param date
         * @return true 标示自己关闭页面,否则自动关闭
         */
        boolean onClickConfirm(@NonNull DialogFragment dialog, @NonNull Date date);
    }
}
