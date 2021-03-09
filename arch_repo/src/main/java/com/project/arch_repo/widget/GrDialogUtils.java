package com.project.arch_repo.widget;

import android.content.Context;


/**
 * 通用对话框工具类
 *
 * @author Fanjb
 * @date 2015-3-12
 */
public class GrDialogUtils {

    /**
     * 创建一个简单的输入对话框
     *
     * @param context
     * @param title
     * @param listener
     * @return
     */
    public static GrInputDialog createInputDialog(Context context, String title, String hint, GrInputDialog.OnDialogClickListener listener) {
        GrInputDialog dialog = new GrInputDialog(context);
        dialog.setTitleBar(title);
        dialog.setHintStr(hint);
        dialog.setDialogListener(listener);
        return dialog;
    }

    /**
     * 创建一个简单的输入对话框
     *
     * @param context
     * @param title
     * @param listener
     * @return
     */
    public static GrInputDialog createInputDialog(Context context, String title, GrInputDialog.OnDialogClickListener listener) {
        GrInputDialog dialog = new GrInputDialog(context);
        dialog.setTitleBar(title);
        dialog.setDialogListener(listener);
        return dialog;
    }

    /**
     * 创建一个简单的输入对话框
     *
     * @param context
     * @return
     */
    public static GrInputDialog createInputDialog(Context context, GrInputDialog.OnDialogClickListener listener) {
        GrInputDialog dialog = new GrInputDialog(context);
        dialog.setDialogListener(listener);
        return dialog;
    }

}
