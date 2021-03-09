package com.project.arch_repo.base.dialog;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.project.arch_repo.widget.TokenProgressHUDImpl;
import com.xxf.arch.dialog.IResultDialog;
import com.xxf.arch.dialog.XXFDialog;
import com.xxf.arch.widget.progresshud.ProgressHUD;

import java.io.Serializable;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BaseDialog<R extends Serializable> extends XXFDialog<R> {
    private TokenProgressHUDImpl tokenProgressHUD;

    protected BaseDialog(@NonNull Context context, @Nullable IResultDialog.OnDialogClickListener<R> onDialogClickListener) {
        super(context, onDialogClickListener);
    }

    protected BaseDialog(@NonNull Context context, int themeResId, @Nullable IResultDialog.OnDialogClickListener<R> onDialogClickListener) {
        super(context, themeResId, onDialogClickListener);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener, @Nullable IResultDialog.OnDialogClickListener<R> onDialogClickListener) {
        super(context, cancelable, cancelListener, onDialogClickListener);
    }


    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getOwnerActivity() != null) {
            tokenProgressHUD = new TokenProgressHUDImpl(this.getOwnerActivity());
        } else if (this.getContext() instanceof ContextWrapper) {
            tokenProgressHUD = new TokenProgressHUDImpl(((ContextWrapper) this.getContext()).getBaseContext());
        } else {
            tokenProgressHUD = new TokenProgressHUDImpl(this.getContext());
        }
    }

    @Override
    public ProgressHUD progressHUD() {
        return tokenProgressHUD;
    }
}
