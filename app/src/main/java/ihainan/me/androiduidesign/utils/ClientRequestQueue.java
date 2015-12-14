package ihainan.me.androiduidesign.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 单例 RequestQueue，用于向服务器发送 HTTP 请求
 */
public class ClientRequestQueue {
    public final static String BASE_URL = "http://10.1.114.25/furniture/";
    public final static String BASE_URL_USER = BASE_URL + "controller/UserInfo.php?action=";
    public final static String BASE_URL_VOTE = BASE_URL + "controller/VoteInfo.php?action=";
    public final static String BASE_URL_FUR = BASE_URL + "controller/FurnitureInfo.php?action=";

    public static final String TAG = ClientRequestQueue.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static ClientRequestQueue mInstance;
    private static Context mCtx;

    /**
     * 私有构造函数
     */
    private ClientRequestQueue(Context ctx) {
        mCtx = ctx;
    }

    /**
     * 实例化单例，保证线程安全
     */
    private synchronized static void newInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ClientRequestQueue(context);
        }
    }

    /**
     * 获取 ClientRequestQueue 实例
     *
     * @return ClientRequestQueue 实例
     */
    public static ClientRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            newInstance(context);
        }
        return mInstance;
    }

    /**
     * 获取请求队列
     *
     * @return 请求队列
     */
    public RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    /**
     * 添加请求到请求队列当中
     *
     * @param req 需要添加的请求
     * @param tag 请求的标签
     * @param <T> 请求返回的数据类型
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue(mCtx).add(req);
    }

    /**
     * 添加请求到请求队列当中
     *
     * @param req 需要添加的请求
     * @param <T> 请求返回的数据类型
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue(mCtx).add(req);
    }

    /**
     * 获取特定标签的请求
     *
     * @param tag 请求的标签
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
