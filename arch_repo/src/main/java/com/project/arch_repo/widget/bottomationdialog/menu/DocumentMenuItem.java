package com.project.arch_repo.widget.bottomationdialog.menu;

import android.support.v4.app.FragmentActivity;

import com.project.arch_repo.utils.SystemUtils;
import com.project.arch_repo.widget.bottomationdialog.ItemMenuImpl;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class DocumentMenuItem extends ItemMenuImpl<String> {

    private FragmentActivity activity;
    Consumer<String> consumer;

    public DocumentMenuItem(FragmentActivity activity, Consumer<String> consumer) {
        super("本地文件", "本地文件");
        this.activity = activity;
        this.consumer = consumer;
    }

    @Override
    public void doAction() {
        super.doAction();
        if (activity != null) {
            SystemUtils.doSelectDocument(activity, this.consumer);
        }
    }
}
