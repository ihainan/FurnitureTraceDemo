package ihainan.me.androiduidesign.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.etsy.android.grid.StaggeredGridView;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;
import java.util.List;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.adapter.StyleListAdapter;
import ihainan.me.androiduidesign.adapter.TypeListAdapter;
import ihainan.me.androiduidesign.model.Collocation;
import ihainan.me.androiduidesign.model.Furniture;
import ihainan.me.androiduidesign.utils.ClientRequestQueue;
import ihainan.me.androiduidesign.utils.CommonUtils;
import ihainan.me.androiduidesign.utils.GlobalVar;
import ihainan.me.androiduidesign.utils.JSONUtil;
import ihainan.me.androiduidesign.utils.NumZero;

/**
 * Tab 页适配.
 */
public class HomePageObjectFragment extends Fragment {
    public final static String TAG = HomePageObjectFragment.class.getSimpleName();
    public final static String TAB_KEY = "FRAGEMENT_";
    private Context mContext;

    public HomePageObjectFragment(Context mContext) {
        this.mContext = mContext;
        setRetainInstance(true);
    }

    private View rootViewTabType, rootViewTabStyle, rootViewTabRec;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();

        // ToolBar Height
        float actionBarHeight = CommonUtils.getToolBarHeight(mContext) + CommonUtils.dpToPixel(mContext, 30);

        // Type Tab
        if (args.getInt(TAB_KEY) == 0) {
            if (rootViewTabType != null) return rootViewTabType;
            rootViewTabType = inflater.inflate(
                    R.layout.page_type, container, false);

            /* 设置 MarginTop */
            LinearLayout linearLayout = (LinearLayout) rootViewTabType.findViewById(R.id.type_layout);
            linearLayout.setPadding(linearLayout.getPaddingLeft(), (int) actionBarHeight, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());

            /* 配置 GridView */
            StaggeredGridView gvCategory = (StaggeredGridView) linearLayout.findViewById(R.id.grid_view);
            gvCategory.setAdapter(new TypeListAdapter(mContext));
            return rootViewTabType;
        } else if (args.getInt(TAB_KEY) == 1) {
            // Style Tab
            if (rootViewTabStyle != null) return rootViewTabStyle;
            rootViewTabStyle = inflater.inflate(
                    R.layout.page_style, container, false);


            LinearLayout linearLayout = (LinearLayout) rootViewTabStyle.findViewById(R.id.style_layout);
            linearLayout.setPadding(linearLayout.getPaddingLeft(), (int) actionBarHeight, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());

            /* 配置 GridView & Adapter */
            StaggeredGridView gvCategory = (StaggeredGridView) linearLayout.findViewById(R.id.grid_view);
            gvCategory.setAdapter(new StyleListAdapter(mContext));
            return rootViewTabStyle;
        } else if (args.getInt(TAB_KEY) == 2) {
            // Style Tab
            if (rootViewTabRec != null) return rootViewTabRec;

            rootViewTabRec = inflater.inflate(
                    R.layout.page_recommendation, container, false);


            // Layout Padding Top
            final RelativeLayout layout = (RelativeLayout) rootViewTabRec.findViewById(R.id.recommendation_layout);
            layout.setPadding(layout.getPaddingLeft(), (int) actionBarHeight, layout.getPaddingRight(), layout.getPaddingBottom());

            // Hint Text
            final ShimmerTextView hintText = (ShimmerTextView) layout.findViewById(R.id.hint_text);

            // Card View
            final LinearLayout cardViewLayout = (LinearLayout) layout.findViewById(R.id.cardview_layout);

            // Price Search EditText
            final EditText priceEditText = (EditText) layout.findViewById(R.id.input_price);
            priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            priceEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        final Shimmer shimmer = new Shimmer();
                        hintText.setText(R.string.recommendation_hint_text);
                        hintText.setVisibility(View.VISIBLE);
                        shimmer.start(hintText);
                        cardViewLayout.removeAllViews();

                        // 检测输入是否合法
                        String priceStr = priceEditText.getText().toString();
                        final int price;
                        try {
                            price = Integer.valueOf(priceStr);
                            Log.d(TAG, "Price " + price);
                        } catch (Exception e) {
                            Toast.makeText(mContext, "输入 " + priceStr + " 并不是数字", Toast.LENGTH_LONG);
                            return false;
                        }

                        // 发送请求获取推荐数据
                        final String url = ClientRequestQueue.BASE_URL_FUR + "collocation&price=" + price;
                        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "URL: " + url + "\nResponse: " + response);
                                Collocation collocation = JSONUtil.parseFurCollcation(response);

                                // 显示和隐藏数据
                                hintText.setVisibility(View.INVISIBLE);
                                for (int i = 0; i < GlobalVar.STYLES_CHINESE.size(); ++i) {
                                    View layout = inflater.inflate(R.layout.content_rec_item, container, false);
                                    CardView cardView = (CardView) layout.findViewById(R.id.card_view);
                                    TextView textView = (TextView) cardView.findViewById(R.id.rec_type);
                                    textView.setText(GlobalVar.STYLES_CHINESE.get(i));

                                    SliderLayout sliderLayout = (SliderLayout) layout.findViewById(R.id.slider_new);
                                    if (i == 0)
                                        configSliderLayout(sliderLayout, collocation.get简约现代());
                                    else if (i == 1)
                                        configSliderLayout(sliderLayout, collocation.get美式乡村());
                                    else if (i == 2)
                                        configSliderLayout(sliderLayout, collocation.get中式现代());
                                    else if (i == 3)
                                        configSliderLayout(sliderLayout, collocation.get韩式田园());
                                    cardViewLayout.addView(layout);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                (new AlertDialog.Builder(mContext))
                                        .setTitle(R.string.fail_to_connect_title)
                                        .setMessage(R.string.fail_to_connect)
                                        .setPositiveButton(R.string.fail_to_connect_ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                hintText.setText(R.string.fail_to_connect);
                                shimmer.cancel();
                                error.printStackTrace();
                                Log.e(TAG, "发送请求 " + url + " 失败 :" + error.getStackTrace());
                            }
                        });

                        // 将请求加入到队列当中
                        ClientRequestQueue.getInstance(mContext).addToRequestQueue(strReq);

                        return true;
                    }
                    return false;
                }
            });
            return rootViewTabRec;
        }

        return null;
    }

    private void configSliderLayout(SliderLayout mDemoSlider, final List<Furniture> furnitureList) {
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(10000);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(R.color.red_pink_24, R.color.red_pink_26);
        final NumZero n = new NumZero(mContext);
        mDemoSlider.setNumLayout(n);
        HashMap<Integer, String> images = new HashMap<>();
        for (int i = 0; i < furnitureList.size(); ++i) {
            images.put(i, furnitureList.get(i).getPic().get(0).getPicAdd());
        }

        for (final Integer id : images.keySet()) {
            TextSliderView textSliderView = new TextSliderView(mContext);

            // initialize a SliderLayout
            textSliderView
                    .image(images.get(id))
                    .description(furnitureList.get(id).getFur_name())
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {
                            Intent intent = new Intent(mContext, ItemDetailActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(ItemDetailActivity.ITEM_ID_, furnitureList.get(id).getFur_id());
                            mContext.startActivity(intent);
                        }
                    });

            // Name
            textSliderView.getBundle().putString("extra", furnitureList.get(id).getFur_name());

            // add your extra information
            mDemoSlider.addSlider(textSliderView);
        }
    }
}