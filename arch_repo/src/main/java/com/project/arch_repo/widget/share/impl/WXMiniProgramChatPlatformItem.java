package com.project.arch_repo.widget.share.impl;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.project.arch_repo.utils.BitmapUtil;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.xxf.arch.XXF;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 微信小程序分享平台
 */
public class WXMiniProgramChatPlatformItem extends WXChatPlatformItem {


    public WXMiniProgramChatPlatformItem(String shareTitle,
                                         String shareDescription,
                                         byte[] shareThumbData,
                                         String miniProgramPath) {
        super(create(shareTitle, shareDescription, shareThumbData, miniProgramPath));
    }


    private String shareThumbDataHttpUrl;

    /**
     * @param shareTitle            分享标题
     * @param shareDescription      分享描述
     * @param shareThumbDataHttpUrl 分享封面缩略图网络地址
     * @param miniProgramPath       分享到微信小程序跳转的页面
     */
    public WXMiniProgramChatPlatformItem(String shareTitle,
                                         String shareDescription,
                                         String shareThumbDataHttpUrl,
                                         String miniProgramPath) {
        super(create(shareTitle, shareDescription, null, miniProgramPath));
        this.shareThumbDataHttpUrl = shareThumbDataHttpUrl;
    }

    private static WXMediaMessage create(String shareTitle,
                                         String shareDescription,
                                         byte[] shareThumbData,
                                         String miniProgramPath) {
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        // TODO: 2019-10-30 微信分享，暂时不能用
//        miniProgram.miniprogramType = BuildConfig.WX_MINI_PROGRAM_OBJECT;
//        miniProgram.webpageUrl = BuildConfig.OFFICIAL_WEBSITE_URL;
//        miniProgram.userName = BuildConfig.WX_MINI_PRO_APP_ID;
        miniProgram.path = miniProgramPath;

        WXMediaMessage mediaMessage = new WXMediaMessage(miniProgram);
        mediaMessage.title = shareTitle;
        mediaMessage.description = shareDescription;
        mediaMessage.thumbData = shareThumbData;
        mediaMessage.mediaObject = miniProgram;
        return mediaMessage;
    }

    @SuppressLint("CheckResult")
    @Override
    public void doShare() {
        if (!TextUtils.isEmpty(shareThumbDataHttpUrl)) {
            //下载
            Observable
                    .fromCallable(new Callable<byte[]>() {
                        @Override
                        public byte[] call() throws Exception {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            URL u = new URL(shareThumbDataHttpUrl);
                            InputStream is = u.openStream();
                            DataInputStream dis = new DataInputStream(is);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = dis.read(buffer)) > 0) {
                                byteArrayOutputStream.write(buffer, 0, length);
                            }
                            return BitmapUtil.compressionByte(byteArrayOutputStream.toByteArray(), 300, 240);
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(XXF.<byte[]>bindToErrorNotice())
                    .subscribe(new Consumer<byte[]>() {
                        @Override
                        public void accept(byte[] bytes) throws Exception {
                            getMessage().thumbData = bytes;
                            WXMiniProgramChatPlatformItem.super.doShare();
                        }
                    });
        } else {
            super.doShare();
        }
    }
}
