package ihainan.me.androiduidesign.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import ihainan.me.androiduidesign.utils.JSONUtil;

public class SearchActivity extends AppCompatActivity {
    public final static String TAG = SearchActivity.class.getSimpleName();

    // UI Elements
    private EditText mSearchView;
    private StaggeredGridView mGridView;
    private View mProgressView;
    private ImageView mSearchClear, mQRCode;

    // List Adapter
    private List<Furniture> mNewFurnitureItems = new ArrayList<Furniture>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 获取 Intent 参数
        Intent intent = getIntent();
        final String typeTag = intent.getStringExtra(ItemListStaggeredActivity.TYPE_TAG);
        final String textTag = intent.getStringExtra(ItemListStaggeredActivity.TEXT_TAG);

        // UI elements
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        mSearchView = (EditText) findViewById(R.id.search_view);
        mProgressView = (ProgressBar) findViewById(R.id.progress_bar);
        mSearchClear = (ImageView) findViewById(R.id.search_clear);
        mQRCode = (ImageView) findViewById(R.id.scan_qr_code_btn);

        /* 设置 Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* 设置 SearchActivity View */
        if (typeTag != null) {
            mSearchView.setHint("Search furniture under #" + textTag);
        }

        /* 返回按钮 */
        ((ImageView) findViewById(R.id.back_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 弹出输入法窗口 */
        mSearchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        // 不显示 Progress Bar
        showProgress(false);

        /* 设置 QRCode Scanner */
        mQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏输入法窗口
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                // 跳转到新页面
                Intent intent = new Intent(SearchActivity.this, QRCodeScannerActivity.class);
                startActivity(intent);
            }
        });

        /* 设置清除按钮 */
        ImageView clearButton = (ImageView) findViewById(R.id.search_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.setText("");
            }
        });
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 清除按钮是否可见
                if (mSearchView.getText().toString().length() > 0)
                    mSearchClear.setVisibility(View.VISIBLE);
                else mSearchClear.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /* 设置 GridView */
        float actionBarHeight = 0.0f;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        mGridView.setPadding(mGridView.getPaddingLeft(), (int) actionBarHeight, mGridView.getPaddingRight(), mGridView.getRowPaddingBottom());

        /* 按下回车，开始搜索 */
        mSearchView.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
        mSearchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    showProgress(true);
                    String param = "";
                    if (typeTag != null) {
                        if (typeTag.equals("STYLE")) param = "&style=" + textTag;
                        else if (typeTag.equals("TYPE")) param = "&type=" + textTag;
                    }
                    final String furnitureName = mSearchView.getText().toString();
                    final String url = ClientRequestQueue.BASE_URL_FUR + "furnitureList&name=" + furnitureName + param;
                    StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Response: " + response);
                            mNewFurnitureItems = JSONUtil.parseFurList(response);

                            // Done! now continue on the UI thread
                            runOnUiThread(returnRes);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            (new AlertDialog.Builder(SearchActivity.this))
                                    .setTitle(R.string.fail_to_connect_title)
                                    .setMessage(R.string.fail_to_connect)
                                    .setPositiveButton(R.string.fail_to_connect_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                            error.printStackTrace();
                            Log.e(TAG, "发送请求 " + url + " 失败 :" + error.getStackTrace());
                        }
                    });

                    // 将请求加入到队列当中
                    ClientRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq);

                    return true;
                }
                return false;
            }
        });
    }

    /* 获取数据完毕，更新 UI */
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            showProgress(false);
            final List<Furniture> furnitureList = new ArrayList<Furniture>();
            Collections.copy(mNewFurnitureItems, furnitureList);
            mGridView.setAdapter(new StaggeredItemAdapter(getApplicationContext(), mNewFurnitureItems));
        }
    };

    /**
     * Shows the progress UI and hides the List.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mGridView.setVisibility(show ? View.GONE : View.VISIBLE);
            mGridView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mGridView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mGridView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
