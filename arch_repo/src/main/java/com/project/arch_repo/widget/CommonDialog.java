package com.project.arch_repo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.project.arch_repo.R;
import com.project.arch_repo.databinding.ArchDialogCommonBinding;
import com.project.arch_repo.databinding.ArchDialogInputBinding;
import com.project.arch_repo.utils.DisplayUtils;
import com.xxf.arch.XXF;

/**
 * @fileName: CommonDialog
 * @author: liuboyu
 * @date: 2021/3/22 9:15 PM
 * @description: 通用对话框
 */
public class CommonDialog extends Dialog {

    private String mTitle;
    private String mConfirmStr;
    private String mCancelStr;
    private String mDesc;
    private CommonDialog.OnDialogClickListener mListener;

    private ArchDialogCommonBinding mBinding;

    public CommonDialog(Context context) {
        super(context, R.style.arch_custom_dialog_style);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ArchDialogCommonBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = DisplayUtils.getRealScreenSize(this.getContext()).x - DisplayUtils.dip2px(XXF.getApplication(),53 * 2);
        getWindow().setAttributes(params);
        initView();
    }


    private void initView() {
        //标题
        if (!TextUtils.isEmpty(mTitle)) {
            mBinding.titleText.setText(mTitle);
        }

        //确认
        if (!TextUtils.isEmpty(mConfirmStr)) {
            mBinding.selectBtn.setText(mConfirmStr);
        }

        //取消
        if (!TextUtils.isEmpty(mCancelStr)) {
            mBinding.cancelBtn.setText(mCancelStr);
        }

        //content
        if (!TextUtils.isEmpty(mDesc)) {
            mBinding.tvDesc.setText(mDesc);
        }

        mBinding.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickConfirm(v);
                }
            }
        });

        mBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickCancel(v);
                }
            }
        });
    }


    /**
     * 设置标题栏
     *
     * @param title
     */
    public void setTitleBar(String title) {
        mTitle = title;
    }

    /**
     * 设置标题栏
     *
     * @param desc
     */
    public void setDesc(String desc) {
        mDesc = desc;
    }

    /**
     * 设置确认按钮
     *
     * @param confirm
     */
    public void setConfirm(String confirm) {
        mConfirmStr = confirm;
    }

    /**
     * 设置取消按钮
     *
     * @param cancel
     */
    public void setCancel(String cancel) {
        mCancelStr = cancel;
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setDialogListener(CommonDialog.OnDialogClickListener listener) {
        mListener = listener;
    }

    public interface OnDialogClickListener {
        void onClickConfirm(View view);

        void onClickCancel(View view);
    }

}