package com.project.arch_repo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.project.arch_repo.R;
import com.project.arch_repo.databinding.ArchDialogInputBinding;
import com.project.arch_repo.utils.DisplayUtils;
import com.xxf.arch.XXF;

/**
 * 简单输入对话框
 *
 * @author Fanjb
 * @date 2015-3-1
 */
public class GrInputDialog extends Dialog {

    private String mTitle;
    private String mConfirmStr;
    private String mCancelStr;
    private String mHintStr;
    private OnDialogClickListener mListener;
    private int mEditInputType = -1;
    private int mEditInputMaxLength = -1;

    private ArchDialogInputBinding mBinding;

    public GrInputDialog(Context context) {
        super(context, R.style.arch_custom_dialog_style);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ArchDialogInputBinding.inflate(getLayoutInflater());
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

        //输入框
        if (mEditInputType > -1) {
            mBinding.inputEdit.setInputType(mEditInputType);
        }

        //确认
        if (!TextUtils.isEmpty(mConfirmStr)) {
            mBinding.selectBtn.setText(mConfirmStr);
        }

        //取消
        if (!TextUtils.isEmpty(mCancelStr)) {
            mBinding.cancelBtn.setText(mCancelStr);
        }

        //hint
        if (!TextUtils.isEmpty(mHintStr)) {
            mBinding.inputEdit.setHint(mHintStr);
        }

        mBinding.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickConfirm(v, mBinding.inputEdit.getText().toString());
                }
            }
        });

        mBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickCancel(v, mBinding.inputEdit.getText().toString());
                }
            }
        });

        //最大输入长度
        if (mEditInputMaxLength > 0) {
            mBinding.inputEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mEditInputMaxLength)});
        }
    }

    /**
     * 设置输入框输入类型
     *
     * @return
     */
    public void setEditInputType(int inputType) {
        mEditInputType = inputType;
    }

    /**
     * 设置输入文本长度
     *
     * @param length
     */
    public void setMaxLength(int length) {
        mEditInputMaxLength = length;
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
     * 设置提示文案
     *
     * @param str
     */
    public void setHintStr(String str) {
        mHintStr = str;
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setDialogListener(OnDialogClickListener listener) {
        mListener = listener;
    }

    public interface OnDialogClickListener {
        void onClickConfirm(View view, String content);

        void onClickCancel(View view, String content);
    }

}
