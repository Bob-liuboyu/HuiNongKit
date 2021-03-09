package com.project.arch_repo.widget.share.impl;

import com.project.arch_repo.R;
import com.project.arch_repo.widget.share.Platform;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 微信分享->朋友圈
 */
public class WXFriendsCirclePlatformItem extends PlatformItem {
    WXMediaMessage message;

    public WXFriendsCirclePlatformItem(WXMediaMessage message) {
        super(Platform.WECHAT, R.mipmap.arch_ic_friends_circle, "朋友圈");
        this.message = message;
    }

    @Override
    public void doShare() {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = message;
        req.transaction = "";
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
//        new BaseWeChatObservable<SendMessageToWX.Req>(req)
//                .subscribe();
    }
}
