package ihainan.me.androiduidesign.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.model.Furniture;
import ihainan.me.androiduidesign.model.Logistics;
import ihainan.me.androiduidesign.model.Pic;
import ihainan.me.androiduidesign.model.ShoppingCart;
import ihainan.me.androiduidesign.utils.ClientRequestQueue;
import ihainan.me.androiduidesign.utils.CommonUtils;
import ihainan.me.androiduidesign.utils.JSONUtil;
import ihainan.me.androiduidesign.utils.NumZero;

public class ItemDetailActivity extends AppCompatActivity {
    private final static String TAG = ItemDetailActivity.class.getSimpleName();

    // Intent Extras
    public final static String ITEM_ID_ = "ITEM_ID";

    /* UI Elements */
    private SliderLayout mDemoSlider;
    private TextView tvItemName, tvItemMaterial, tvItemPrice, tvItemBrand, tvItemType, tvStyle;
    private Button btnShare, btnBuy;
    private TableLayout tlLogs;

    // Furniture Info
    private String mUrl;
    private int mFurnitureID;
    private Furniture furniture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        /* 获取家具 ID */
        mFurnitureID = getIntent().getIntExtra(ITEM_ID_, -1);

        /* 设置 Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* UI Element */
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        tvItemName = (TextView) findViewById(R.id.item_name);
        tvItemPrice = (TextView) findViewById(R.id.item_price);
        tvItemMaterial = (TextView) findViewById(R.id.item_material);
        tvItemBrand = (TextView) findViewById(R.id.item_brand);
        tvItemType = (TextView) findViewById(R.id.item_type);
        tlLogs = (TableLayout) findViewById(R.id.furniture_logs_tb);
        tvStyle = (TextView) findViewById(R.id.item_style);
        btnShare = (Button) findViewById(R.id.vote_btn);
        btnBuy = (Button) findViewById(R.id.buy_btn);

        /* 按钮事件 */
        ((ImageView) findViewById(R.id.back_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((ImageView) findViewById(R.id.search_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        ((ImageView) findViewById(R.id.shopping_cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, PersonInfoActivity.class);
                intent.putExtra(PersonInfoActivity.PAGE_TAG, 1);
                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 购物车按钮 */
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.dialog_buy_item,
                        (ViewGroup) findViewById(R.id.buy_layout));
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);

                /* 设置购买数量 */
                final NumberPicker numberPicker = (NumberPicker) layout.findViewById(R.id.num_picker);
                int currentQuantity = ShoppingCart.getInstance().getQuantity(furniture.getFur_id());
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(20);
                numberPicker.setValue(currentQuantity);

                /* 确定和取消按钮 */
                builder.setPositiveButton("购买", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShoppingCart.getInstance().setShoppingCartItem(furniture.getFur_id(), numberPicker.getValue());
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setView(layout);
                builder.create().show();
            }
        });

        /* 配置页面 Padding Top */
        float actionBarHeight = 0.0f;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        mainLayout.setPadding(mainLayout.getPaddingLeft(), (int) actionBarHeight, mainLayout.getPaddingRight(), mainLayout.getPaddingBottom());

        /* 配置 Slider */
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(10000);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(R.color.red_pink_24, R.color.red_pink_26);
        final NumZero n = new NumZero(this);
        mDemoSlider.setNumLayout(n);

        /* 构造请求 URL */
        mUrl = ClientRequestQueue.BASE_URL_FUR + "detail&fur_id=" + mFurnitureID;

        // 从远程服务器中拉取数据
        StringRequest strReq = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                furniture = JSONUtil.parseFur(response);

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final HashMap<String, String> maps) {
        for (String name : maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);

            // initialize a SliderLayout
            textSliderView
                    .image(maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .enableSaveImageByLongClick(getFragmentManager());

            // add your extra information
            mDemoSlider.addSlider(textSliderView);
        }
    }

    /* 获取数据完毕，更新 UI */
    private Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            tvItemName.setText("" + furniture.getFur_name());
            tvItemBrand.setText("" + furniture.getFur_brand());
            tvItemPrice.setText("$" + furniture.getFur_price());
            tvItemType.setText("" + furniture.getFur_type());
            tvItemMaterial.setText("" + furniture.getFur_material());
            tvStyle.setText("" + furniture.getFur_style());

            List<Pic> pics = furniture.getPic();
            HashMap<String, String> images = new HashMap<>();
            for (int i = 0; i < pics.size(); ++i) {
                images.put(String.valueOf(i), pics.get(i).getPicAdd());
            }

            defaultCompleteSlider(images);

            /* 追述日志 */
            List<Logistics> logs = furniture.getLog();
            for (int i = 0; i < logs.size(); ++i) {
                Logistics log = logs.get(i);
                TableRow tableRow = (TableRow) LayoutInflater.from(getApplicationContext()).inflate(R.layout.content_item_log, null, false);
                if (i != 0) tableRow.setPadding(tableRow.getPaddingLeft(),
                        CommonUtils.dpToPixel(getApplicationContext(), getResources().getDimension(R.dimen.table_row_padding_top)),
                        tableRow.getPaddingRight(),
                        tableRow.getBottom());
                ((TextView) tableRow.findViewById(R.id.item_log_date)).setText("" + new SimpleDateFormat("yyyy-MM-dd").format(log.getLog_date()).toString());
                ((TextView) tableRow.findViewById(R.id.item_log_status)).setText("" + log.getLog_des().toString());
                tlLogs.addView(tableRow);
            }
        }
    };
}
