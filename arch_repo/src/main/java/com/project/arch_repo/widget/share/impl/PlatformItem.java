package com.project.arch_repo.widget.share.impl;

import android.support.annotation.DrawableRes;

import com.project.arch_repo.widget.share.IPlatformItem;
import com.project.arch_repo.widget.share.Platform;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 分享平台
 */
public abstract class PlatformItem
        implements IPlatformItem {

    private Platform platform;
    private int platformIcon;
    private CharSequence platformName;

    public PlatformItem(Platform platform, @DrawableRes int platformIcon, CharSequence platformName) {
        this.platform = platform;
        this.platformIcon = platformIcon;
        this.platformName = platformName;
    }

    @Override
    public Platform getPlatform() {
        return platform;
    }

    @Override
    public int getPlatformIcon() {
        return platformIcon;
    }

    @Override
    public CharSequence getPlatformName() {
        return platformName;
    }

}
