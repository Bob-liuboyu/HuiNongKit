package com.project.arch_repo.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class RecyclerViewAnimUtil {
    private static final RecyclerViewAnimUtil ourInstance = new RecyclerViewAnimUtil();

    public static RecyclerViewAnimUtil getInstance() {
        return ourInstance;
    }

    private RecyclerViewAnimUtil() {
    }

    /**
     * 关闭默认局部刷新动画
     */
    public void closeDefaultAnimator(RecyclerView mRvCustomer) {
        if (null == mRvCustomer) {
            return;
        }
        mRvCustomer.getItemAnimator().setAddDuration(0);
        mRvCustomer.getItemAnimator().setChangeDuration(0);
        mRvCustomer.getItemAnimator().setMoveDuration(0);
        mRvCustomer.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) mRvCustomer.getItemAnimator()).setSupportsChangeAnimations(false);
    }
}