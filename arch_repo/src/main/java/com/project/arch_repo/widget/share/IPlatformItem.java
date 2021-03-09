package com.project.arch_repo.widget.share;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 分享平台
 */
public interface IPlatformItem {

    Platform getPlatform();

    int getPlatformIcon();

    CharSequence getPlatformName();

    void doShare();
}
