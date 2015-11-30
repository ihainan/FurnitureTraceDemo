package ihainan.me.androiduidesign.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.etsy.android.grid.StaggeredGridView;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.activities.HomePageObjectFragment;

/**
 * 主页 Tabs 适配器
 */
public class HomePageCollectionPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private final static int PAGES_COUNT = 3;

    public HomePageCollectionPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new HomePageObjectFragment(mContext);
        Bundle args = new Bundle();
        args.putInt(HomePageObjectFragment.TAB_KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Categories";
            case 1:
                return "Style";
            case 2:
                return "Recommendation";
        }
        return "Empty";
    }
}