package com.project.arch_repo.widget.share.impl;

import com.project.arch_repo.R;
import com.project.arch_repo.widget.share.Platform;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 微信分享平台->聊天
 */
public class WXChatPlatformItem extends PlatformItem {

    WXMediaMessage message;

    public WXMediaMessage getMessage() {
        return message;
    }

    public WXChatPlatformItem(WXMediaMessage message) {
        super(Platform.WECHAT, R.mipmap.arch_ic_wechat, "微信");
        this.message = message;
    }

    @Override
    public void doShare() {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = message;
        req.transaction = "";
        req.scene = SendMessageToWX.Req.WXSceneSession;
//        new BaseWeChatObservable<SendMessageToWX.Req>(req)
//                .subscribe();
    }
}
