package ihainan.me.androiduidesign.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.etsy.android.grid.StaggeredGridView;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.adapter.TypeListAdapter;
import ihainan.me.androiduidesign.adapter.StyleListAdapter;
import ihainan.me.androiduidesign.utils.CommonUtils;

/**
 * Tab 页适配.
 */
public class HomePageObjectFragment extends Fragment {
    public final static String TAB_KEY = "FRAGEMENT_";
    private Context mContext;

    public HomePageObjectFragment(Context mContext) {
        this.mContext = mContext;
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle args = this.getArguments();

        // ToolBar Height
        float actionBarHeight = CommonUtils.getToolBarHeight(mContext);

        // Type Tab
        View rootView = null;
        if (args.getInt(TAB_KEY) == 0) {
            rootView = inflater.inflate(
                    R.layout.page_type, container, false);

            /* 设置 MarginTop */
            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.type_layout);
            linearLayout.setPadding(linearLayout.getPaddingLeft(), (int) actionBarHeight, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());

            /* 配置 GridView */
            StaggeredGridView gvCategory = (StaggeredGridView) linearLayout.findViewById(R.id.grid_view);
            gvCategory.setAdapter(new TypeListAdapter(mContext));
        } else if (args.getInt(TAB_KEY) == 1) {
            // Style Tab
            rootView = inflater.inflate(
                    R.layout.page_style, container, false);

            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.style_layout);
            linearLayout.setPadding(linearLayout.getPaddingLeft(), (int) actionBarHeight, linearLayout.getPaddingRight(), linearLayout.getPaddingBottom());

            /* 配置 GridView & Adapter */
            StaggeredGridView gvCategory = (StaggeredGridView) linearLayout.findViewById(R.id.grid_view);
            gvCategory.setAdapter(new StyleListAdapter(mContext));
        }

        return rootView;
    }
}