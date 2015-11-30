package ihainan.me.androiduidesign.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.adapter.StaggeredItemAdapter;
import ihainan.me.androiduidesign.model.Furniture;
import ihainan.me.androiduidesign.utils.ClientRequestQueue;
import ihainan.me.androiduidesign.utils.CommonUtils;
import ihainan.me.androiduidesign.utils.JSONUtil;

public class ItemListStaggeredActivity extends AppCompatActivity {
    public final static String TYPE_TAG = "TYPE_TAG";
    public final static String TEXT_TAG = "TEXT_TAG";
    public final static String TAG = ItemListStaggeredActivity.class.getSimpleName();

    /* UI Elements */
    private StaggeredGridView mGridView;
    private TextView tvSearch;

    private List<Furniture> mNewFurnitureItems = new ArrayList<Furniture>();
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list_staggered);

        /* 获取来源数据 */
        Intent intent = getIntent();
        final String typeTag = intent.getStringExtra(TYPE_TAG) == null ? "STYLE" : intent.getStringExtra(TYPE_TAG);
        final String textTag = intent.getStringExtra(TEXT_TAG) == null ? "简约现代" : intent.getStringExtra(TEXT_TAG);

        /* UI elements */
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        tvSearch = (TextView) findViewById(R.id.action_bar_search_text);

        /* 设置 Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* 设置 Padding Top */
        float actionBarHeight = CommonUtils.getToolBarHeight(ItemListStaggeredActivity.this);
        mGridView.setPadding(mGridView.getPaddingLeft(), (int) actionBarHeight, mGridView.getPaddingRight(), mGridView.getRowPaddingBottom());

        /* 设置搜索按钮和文字 */
        tvSearch.setText("Search under #" + textTag);
        final LinearLayout searchLayout = (LinearLayout) findViewById(R.id.action_bar_search_layout);
        final TextView searchText = (TextView) findViewById(R.id.action_bar_search_text);
        searchLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    searchText.setTextColor(Color.WHITE);
                } else {
                    searchText.setTextColor(Color.rgb(181, 181, 181));
                    searchText.setTextColor(getResources().getColor(R.color.search_button_color));
                }
                return true;
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(TYPE_TAG, typeTag);
                intent.putExtra(TEXT_TAG, textTag);
                startActivity(intent);
            }
        });

        /* 设置后退按钮 */
        ImageView backButton = (ImageView) findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 构造请求 URL */
        switch (typeTag) {
            case "STYLE":
                /* 设置资源请求 URL */
                String param = "style=" + textTag;
                mUrl = ClientRequestQueue.BASE_URL_FUR + "furnitureList&" + param;
                break;
            case "TYPE":
                /* 设置资源请求 URL */
                param = "type=" + textTag;
                mUrl = ClientRequestQueue.BASE_URL_FUR + "furnitureList&" + param;
                break;
        }

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
        ClientRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq);
    }

    /* 获取数据完毕，更新 UI */
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            final List<Furniture> furnitureList = new ArrayList<Furniture>();
            Collections.copy(mNewFurnitureItems, furnitureList);
            mGridView.setAdapter(new StaggeredItemAdapter(getApplicationContext(), mNewFurnitureItems));
        }
    };
}
