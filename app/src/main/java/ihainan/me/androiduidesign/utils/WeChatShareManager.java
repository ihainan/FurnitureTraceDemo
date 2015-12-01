package ihainan.me.androiduidesign.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信分享管理类
 */
public class WeChatShareManager {
    private static WeChatShareManager instance;
    private static Context mContext;

    /* API 信息 */
    private final static String APP_ID = "wxf3550eb8c131cdff";
    private final static String APP_SECRET = "d4624c36b6795d1d99dcf0547af5443d";

    private static synchronized void newInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = new WeChatShareManager(context);
        }
    }

    public static WeChatShareManager getInstance(Context context) {
        if (instance == null) newInstance(context);
        return instance;
    }

    /**
     * 会话
     */

    public static final int WECHAT_SHARE_TYPE_TALK = SendMessageToWX.Req.WXSceneSession;
    /**
     * 朋友圈
     */
    public static final int WECHAT_SHARE_TYPE_FRIENDS = SendMessageToWX.Req.WXSceneTimeline;

    /* WeChat SDK */
    private IWXAPI wxApi;

    private WeChatShareManager(Context context) {
        mContext = context;
        initWeChatShare();
    }

    private void initWeChatShare() {
        wxApi = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
        wxApi.registerApp(APP_ID);
    }

    /**
     * 分享网页到微信
     *
     * @param shareContent 需要分享的网页内容
     */
    public void shareWebPage(ShareContent shareContent) {
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = shareContent.getUrl();
        WXMediaMessage msg = new WXMediaMessage(webPage);

        /* 标题、描述、图片等 */
        msg.title = shareContent.getTitle();
        msg.description = shareContent.getDescription();
        Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), shareContent.getPicId());
        msg.thumbData = CommonUtils.getBytesFromBitmap(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shareContent.getShareScreen();
        wxApi.sendReq(req);
    }

    /**
     * 分享内容
     */
    public static class ShareContent {
        private String url, title, description;
        private int picId;
        private int shareScreen;

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getPicId() {
            return picId;
        }

        public int getShareScreen() {
            return shareScreen;
        }

        public ShareContent(String url, String title, String description, int picId, int shareScreen) {

            this.url = url;
            this.title = title;
            this.description = description;
            this.picId = picId;
            this.shareScreen = shareScreen;
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
