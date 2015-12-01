package ihainan.me.androiduidesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.model.Furniture;

/**
 * 推荐结果 List Adapter
 */
public class RecommendationListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Furniture> collocation;
    private LayoutInflater mVi;

    public RecommendationListAdapter(Context context, List<Furniture> furnitures) {
        mContext = context;
        this.collocation = furnitures;
        if (mContext != null)
            mVi = LayoutInflater.from(mContext);
    }

    /**
     * 用于优化 GridView
     */
    class ViewHolder {

    }


    @Override
    public int getCount() {
        return collocation.size();
    }

    @Override
    public Object getItem(int position) {
        return collocation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Furniture furniture = (Furniture) getItem(position);    // 获取家具信息
        /* 优化用 */
        ViewHolder holder = new ViewHolder();

        /* 更新 UI */
        if (convertView == null) {
            convertView = mVi.inflate(R.layout.content_recommendation_item, null);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
