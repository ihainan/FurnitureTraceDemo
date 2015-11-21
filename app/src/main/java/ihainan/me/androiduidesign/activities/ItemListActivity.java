package ihainan.me.androiduidesign.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.adapter.ListItemAdapter;
import ihainan.me.androiduidesign.model.Furniture;
import ihainan.me.androiduidesign.utils.ClientRequestQueue;
import ihainan.me.androiduidesign.utils.GlobalVar;
import ihainan.me.androiduidesign.utils.JSONUtil;

public class ItemListActivity extends AppCompatActivity {
    public final static String TYPE_TAG = "TYPE_TAG";
    public final static String TEXT_TAG = "TEXT_TAG";
    public final static String TAG = ItemListActivity.class.getSimpleName();
    private boolean loadingMore = false;
    private ListItemAdapter mListItemAdapter;
    private String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* 设置封面文字 */
        Intent intent = getIntent();
        String typeTag = intent.getStringExtra(TYPE_TAG);
        String textTag = intent.getStringExtra(TEXT_TAG);
        ((TextView) findViewById(R.id.cover_text)).setText(("#" + textTag).toUpperCase());
        switch (typeTag) {
            case "STYLE":
                /* 设置置顶图片 */
                String uri = "@drawable/style" + GlobalVar.STYLES_ENGLISH.get(GlobalVar.STYLES_CHINESE.indexOf(textTag)).toLowerCase();
                int imageResource = this.getResources().getIdentifier(uri, null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                ((ImageView) findViewById(R.id.top_image)).setImageDrawable(res);

                /* 设置资源请求 URL */
                String param = "fur_style=" + textTag;
                mUrl = ClientRequestQueue.BASE_URL_FUR + "furnitureList&" + param;
                break;
            case "TYPE":
                /* 设置置顶图片 */
                uri = "@drawable/category" + GlobalVar.CATEGORIES_ENGLISH.get(GlobalVar.CATEGORIES_CHINESE.indexOf(textTag)).toLowerCase();
                imageResource = this.getResources().getIdentifier(uri, null, getPackageName());
                res = getResources().getDrawable(imageResource);
                ((ImageView) findViewById(R.id.top_image)).setImageDrawable(res);

                /* 设置资源请求 URL */
                param = "type=" + textTag;
                mUrl = ClientRequestQueue.BASE_URL_FUR + "furnitureList&" + param;
                break;
        }

        /* 设置搜索按钮 */
        final LinearLayout searchLayout = (LinearLayout) findViewById(R.id.action_bar_search_layout);
        final TextView searchText = (TextView) findViewById(R.id.action_bar_search_text);
        searchLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    searchText.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    searchText.setTextColor(Color.rgb(181, 181, 181));
                }
                return true;
            }
        });

        /* 显示后退按钮 */
        ImageView backButton = (ImageView) findViewById(R.id.btn_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 配置 ListView */
        ListView lvItems = (ListView) findViewById(R.id.lv_item_list);
        List<Furniture> furnitureItems = new ArrayList<Furniture>();
        mListItemAdapter = new ListItemAdapter(this, furnitureItems);
        lvItems.setAdapter(mListItemAdapter);


        /* 设置上拉更新 */
        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.load_more_foot_view, null, false);
        lvItems.addFooterView(footerView);
        lvItems.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;

                // 到底部并且还没有开始加载更多数据，那么就从远程服务器拉取数据
                if ((lastInScreen == totalItemCount) && !(loadingMore)) {
                    new Thread(loadMoreListItems).start();
                }
            }
        });
    }

    private List<Furniture> mNewFurnitureItems = new ArrayList<Furniture>();

    /* 从服务器获取更多数据 */
    private Runnable loadMoreListItems = new Runnable() {
        @Override
        public void run() {
            // Set flag so we cant load new items at the same time
            loadingMore = true;

            // 从远程服务器中拉取数据
            StringRequest strReq = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    mNewFurnitureItems = JSONUtil.parseFurList(response);

                    // Done! now continue on the UI thread
                    runOnUiThread(returnRes);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e(TAG, "发送请求 " + mUrl + " 失败 :" + error.getStackTrace());
                }
            });

            // 将请求加入到队列当中
            ClientRequestQueue.getInstance(ItemListActivity.super.getApplicationContext()).addToRequestQueue(strReq);
        }
    };

    /* 获取数据完毕，更新 UI */
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            mListItemAdapter.updateList(mNewFurnitureItems);
            loadingMore = false;
        }
    };
}
