package ihainan.me.androiduidesign.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.utils.GlobalVar;

/**
 * 主页 Tabs 的实现
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
        Fragment fragment = new DemoObjectFragment(mContext);
        Bundle args = new Bundle();
        args.putInt(DemoObjectFragment.TAB_KEY, position);
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


    public static class DemoObjectFragment extends Fragment {
        public final static String TAB_KEY = "FRAGEMENT_";
        private Context mContext;

        public DemoObjectFragment(Context mContext) {
            this.mContext = mContext;
        }

        public DemoObjectFragment(){
        }


        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            Bundle args = this.getArguments();

            // Categories Tab
            View rootView = null;
            if (args.getInt(TAB_KEY) == 1) {
                // Style Tab
                rootView = inflater.inflate(
                        R.layout.style_activity, container, false);

                LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.style_layout);
                linearLayout.setPadding(linearLayout.getPaddingLeft(), 300, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());   // TODO: 正确设置 Padding Top

                /* 配置 ListView & Adapter */
                ListView lvStyle = (ListView) linearLayout.findViewById(R.id.style_listview);
                lvStyle.setAdapter(new StyleListAdapter(mContext));
            } else if (args.getInt(TAB_KEY) == 0) {
                rootView = inflater.inflate(
                        R.layout.category_activity, container, false);

                LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.category_layout);
                linearLayout.setPadding(linearLayout.getPaddingLeft(), 300, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());   // TODO: 正确设置 Padding Top

                /* 配置 GridView */
                GridView gvCategory = (GridView) linearLayout.findViewById(R.id.category_gridview);
                gvCategory.setAdapter(new CategoryListAdapter(mContext));
            }
            return rootView;
        }
    }
}