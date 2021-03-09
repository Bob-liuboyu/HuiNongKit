package com.project.arch_repo.widget.bottomationdialog;

/**
 * @author xuanyouwu
 * @email xuanyouwu@163.com
 * @time 2016-09-21 15:45
 * item 渲染控制模型
 */
public interface ItemMenu<T> {

    T getItem();

    int getItemColor();

    CharSequence getItemTitle();

    boolean isItemDisable();

    void doAction();

}
