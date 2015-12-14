package ihainan.me.androiduidesign.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.elbbbird.android.socialsdk.SocialSDK;
import com.elbbbird.android.socialsdk.model.SocialShareScene;

import ihainan.me.androiduidesign.R;

public class ShareActivity extends AppCompatActivity {
    public static final int SHARE_ID = 9527;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Button btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialSDK.setDebugMode(true);
                SocialSDK.init("wxf3550eb8c131cdff", "weibo_app_id", "qq_app_id");
                SocialSDK.shareTo(ShareActivity.this,
                        new SocialShareScene(SHARE_ID,
                                null,
                                SocialShareScene.SHARE_TYPE_WECHAT,
                                "分享一下",
                                "分享一下",
                                "https://mmbiz.qlogo.cn/mmbiz/b8qLPUsicjFQVlonTQbC2aV5wBYSUHYz0CYbyMPWZ1rWCZXaMNKkmHXXqicr9ibw7C3pZHLmibYf0349icfE5W4tqtw/0?wx_fmt=png",
                                "http://www.baidu.com"));
            }
        });
    }
}
