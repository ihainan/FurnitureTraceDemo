package ihainan.me.androiduidesign.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.model.Furniture;
import ihainan.me.androiduidesign.model.ShoppingCart;
import ihainan.me.androiduidesign.model.Vote;
import ihainan.me.androiduidesign.utils.ClientRequestQueue;
import ihainan.me.androiduidesign.utils.JSONUtil;

public class PersonInfoActivity extends AppCompatActivity {
    public final static String TAG = PersonInfoActivity.class.getSimpleName();
    public final static String PAGE_TAG = TAG + "_PAGE";
    private String mUrl;
    private List<Vote> mVoteList;

    /* UI Elements */
    private LinearLayout mContentLayout;
    private TextView mTvVotes, mTvShoppingCart;
    private RelativeLayout mLayoutVotes, mLayoutShoppingCart;

    private int mCurrentPage = 0;    // 0 表示显示投票页，1 表示购物车页

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        /* Intent */
        Intent intent = getIntent();
        mCurrentPage = intent.getIntExtra(PAGE_TAG, 0);

        /* 设置 Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* UI Element */
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.content_all);
        mContentLayout = (LinearLayout) findViewById(R.id.content_main);
        mTvVotes = (TextView) findViewById(R.id.votes_number);
        mTvShoppingCart = (TextView) findViewById(R.id.shopping_cart_number);
        mLayoutVotes = (RelativeLayout) findViewById(R.id.btn_votes_number);
        mLayoutShoppingCart = (RelativeLayout) findViewById(R.id.btn_shopping_cart);

        /* 用户名 */
        SharedPreferences userProfile = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        String userName = userProfile.getString(LoginActivity.PREFS_FIELD_USER_NAME, "ERROR");
        ((TextView) findViewById(R.id.action_bar_username)).setText(userName);

        /* 配置页面 Padding Top */
        float actionBarHeight = 0.0f;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        mainLayout.setPadding(mainLayout.getPaddingLeft(), (int) actionBarHeight, mainLayout.getPaddingRight(), mainLayout.getPaddingBottom());


        /* 按钮事件 */
        ((ImageView) findViewById(R.id.back_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLayoutVotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage != 0) {
                    mCurrentPage = 0;
                    mContentLayout.removeAllViews();
                    mContentLayout.addView(getView());
                }
            }
        });

        mLayoutShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage != 1) {
                    mCurrentPage = 1;
                    mContentLayout.removeAllViews();
                    mContentLayout.addView(getView());
                }
            }
        });


        mUrl = ClientRequestQueue.BASE_URL_VOTE + "voteList" + "&" + "userid=" + 5;
    }

    /* 获取数据完毕，更新 UI */
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            mTvVotes.setText("" + mVoteList.size());
            mTvShoppingCart.setText(ShoppingCart.getInstance().getCount() + "");
            mContentLayout.removeAllViews();
            mContentLayout.addView(getView());
        }
    };

    private StaggeredGridView mGridViewVote;
    private RelativeLayout mLayoutGridViewShoppingCart;
    private int totalPrice;

    @Override
    protected void onResume() {
        super.onResume();
        // 从远程服务器中拉取数据
        StringRequest strReq = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                mVoteList = JSONUtil.parseVoteList(response);

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

    public View getView() {
        // 投票页
        if (mCurrentPage == 0) {
            if (mGridViewVote != null) return mGridViewVote;
            mGridViewVote = (StaggeredGridView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.page_votes, null, false);
            mGridViewVote.setAdapter(new BaseAdapter() {
                private LayoutInflater mVi;

                @Override
                public int getCount() {
                    return mVoteList.size();
                }

                @Override
                public Object getItem(int position) {
                    return mVoteList.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (mVi == null) mVi = LayoutInflater.from(getApplicationContext());
                    final Vote vote = (Vote) getItem(position);
                    if (convertView == null)
                        convertView = mVi.inflate(R.layout.content_vote_item, null);

                    TextView tvLike = (TextView) convertView.findViewById(R.id.like_number);
                    TextView tvDislike = (TextView) convertView.findViewById(R.id.dislike_number);
                    final ImageView ivFur = (ImageView) convertView.findViewById(R.id.vote_item_image);
                    final TextView tvName = (TextView) convertView.findViewById(R.id.vote_item_name);

                    tvLike.setText("" + vote.getVot_approve());
                    tvDislike.setText("" + vote.getVot_oppose());

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplication(), ItemDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(ItemDetailActivity.ITEM_ID_, vote.getFur_id());
                            startActivity(intent);
                        }
                    });

                    // 获取家具图片
                    final String url = ClientRequestQueue.BASE_URL_FUR + "detail&fur_id=" + vote.getFur_id();

                    // 从远程服务器中拉取数据
                    StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            Furniture furniture = JSONUtil.parseFur(response);
                            tvName.setText("" + furniture.getFur_name());
                            if (furniture.getPic() != null && furniture.getPic().size() != 0)
                                Picasso.with(getApplicationContext()).load(furniture.getPic().get(0).getPicAdd()).into(ivFur);
                            else
                                Picasso.with(getApplicationContext()).load(R.drawable.categorybed).into(ivFur);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.e(TAG, "发送请求 " + url + " 失败 :" + error.getStackTrace());
                        }
                    });

                    // 将请求加入到队列当中
                    ClientRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq);

                    return convertView;
                }
            });
            return mGridViewVote;
        } else if (mCurrentPage == 1) {
            // if (mLayoutGridViewShoppingCart != null) return mLayoutGridViewShoppingCart;
            mLayoutGridViewShoppingCart = (RelativeLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.page_shopping_cart, null, false);

            // 总价
            final TextView totalPriceText = (TextView) mLayoutGridViewShoppingCart.findViewById(R.id.item_total_price);
            totalPrice = 0;

            // 下单
            Button checkoutButon = (Button) mLayoutGridViewShoppingCart.findViewById(R.id.checkout);
            checkoutButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(PersonInfoActivity.this).setMessage("Are you sure want to check out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ShoppingCart.getInstance().clearAll();
                            totalPrice = 0;
                            mTvShoppingCart.setText(ShoppingCart.getInstance().getCount() + "");
                            mContentLayout.removeAllViews();
                            mContentLayout.addView(getView());
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setTitle("Checkout").create().show();

                }
            });


            // 列表
            StaggeredGridView mGridViewShoppingCart = (StaggeredGridView) mLayoutGridViewShoppingCart.findViewById(R.id.grid_view);
            mGridViewShoppingCart.setColumnCountLandscape(1);
            mGridViewShoppingCart.setColumnCountPortrait(1);
            mGridViewShoppingCart.setAdapter(new BaseAdapter() {
                private LayoutInflater mVi;
                private ShoppingCart shoppingCart = ShoppingCart.getInstance();

                @Override
                public int getCount() {
                    return shoppingCart.getCount();
                }

                @Override
                public Object getItem(int position) {
                    return shoppingCart.getItem(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    if (mVi == null) mVi = LayoutInflater.from(getApplicationContext());
                    final ShoppingCart.ShoppingCartItem shoppingCartItem = (ShoppingCart.ShoppingCartItem) getItem(position);
                    if (convertView == null)
                        convertView = mVi.inflate(R.layout.content_shopping_cart, null);

                    // 点击转到详情页
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(ItemDetailActivity.ITEM_ID_, shoppingCartItem.getFurID());
                            startActivity(intent);
                        }
                    });

                    // 获取家具信息
                    final String url = ClientRequestQueue.BASE_URL_FUR + "detail&fur_id=" + shoppingCartItem.getFurID();

                    // UI Elements
                    final ImageView itemImage = (ImageView) convertView.findViewById(R.id.item_image);
                    final TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
                    itemName.setMaxLines(2);
                    final TextView itemPrice = (TextView) convertView.findViewById(R.id.item_price);
                    final Button itemQuantity = (Button) convertView.findViewById(R.id.btn_quantity);

                    itemQuantity.setText("" + shoppingCartItem.getQuantity());

                    // 从远程服务器中拉取数据
                    StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            Furniture furniture = JSONUtil.parseFur(response);
                            itemName.setText("" + furniture.getFur_name());
                            itemPrice.setText("" + furniture.getFur_price());
                            totalPrice += furniture.getFur_price() * shoppingCartItem.getQuantity();
                            if (position == getCount() - 1) {
                                totalPriceText.setText("￥" + totalPrice);
                            }
                            if (furniture.getPic() != null && furniture.getPic().size() != 0)
                                Picasso.with(getApplicationContext()).load(furniture.getPic().get(0).getPicAdd()).into(itemImage);
                            else
                                Picasso.with(getApplicationContext()).load(R.drawable.categorybed).into(itemImage);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.e(TAG, "发送请求 " + url + " 失败 :" + error.getStackTrace());
                        }
                    });

                    // 将请求加入到队列当中
                    ClientRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq);
                    return convertView;
                }
            });
            return mLayoutGridViewShoppingCart;
        }

        return null;
    }
}
