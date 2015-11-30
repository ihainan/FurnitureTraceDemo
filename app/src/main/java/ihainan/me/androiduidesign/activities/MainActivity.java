package ihainan.me.androiduidesign.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.adapter.HomePageCollectionPagerAdapter;
import ihainan.me.androiduidesign.utils.CommonUtils;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 设置 Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* 设置搜索按钮 */
        final LinearLayout searchLayout = (LinearLayout) findViewById(R.id.action_bar_search_layout);
        final TextView searchText = (TextView) findViewById(R.id.action_bar_search_text);
        searchLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 按钮点击变色
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    searchText.setTextColor(CommonUtils.getColorFromResourceID(MainActivity.this, R.color.search_button_clicked_color));
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    searchText.setTextColor(CommonUtils.getColorFromResourceID(MainActivity.this, R.color.search_button_color));
                }
                return true;
            }
        });

        /* 设置个人页按钮 */
        ((ImageView) findViewById(R.id.person_page_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonInfoActivity.class);
                startActivity(intent);
            }
        });

        /* 设置 TabPaper 和 TabLayout */
        ViewPager viewPaper = (ViewPager) findViewById(R.id.pager);
        viewPaper.setAdapter(new HomePageCollectionPagerAdapter(getSupportFragmentManager(), this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPaper);
    }
}
