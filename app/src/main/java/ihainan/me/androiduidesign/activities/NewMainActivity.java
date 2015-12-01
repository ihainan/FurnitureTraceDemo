package ihainan.me.androiduidesign.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.adapter.HomePageCollectionPagerAdapter;
import ihainan.me.androiduidesign.utils.CommonUtils;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class NewMainActivity extends AppCompatActivity implements MaterialTabListener {
    private final static String TAG = NewMainActivity.class.getSimpleName();
    private MaterialTabHost mTabHost;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        /* 设置 Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, String.valueOf(CommonUtils.dpToPixel(this, 30)));

        // UI Elements
        mTabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        mViewPager = (ViewPager) this.findViewById(R.id.pager);

        HomePageCollectionPagerAdapter pagerAdapter = new HomePageCollectionPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
}
