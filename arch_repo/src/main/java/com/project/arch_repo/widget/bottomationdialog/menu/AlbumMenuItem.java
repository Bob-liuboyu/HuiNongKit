package com.project.arch_repo.widget.bottomationdialog.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.project.arch_repo.utils.SystemUtils;
import com.project.arch_repo.widget.bottomationdialog.ItemMenuImpl;

import io.reactivex.functions.Consumer;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class AlbumMenuItem extends ItemMenuImpl<String> {

    private Fragment fragment;
    private FragmentActivity activity;
    Consumer<String> consumer;

    public AlbumMenuItem(Fragment fragment, Consumer<String> consumer) {
        super("从手机相册选取", "从手机相册选取");
        this.fragment = fragment;
        this.consumer = consumer;
    }


    public AlbumMenuItem(FragmentActivity activity, Consumer<String> consumer) {
        super("从手机相册选取", "从手机相册选取");
        this.activity = activity;
        this.consumer = consumer;
    }

    @Override
    public void doAction() {
        super.doAction();
        if (activity != null) {
            SystemUtils.doSelectAlbum(activity, this.consumer);
        } else if (fragment != null) {
            SystemUtils.doSelectAlbum(fragment, this.consumer);
        }
    }
}
