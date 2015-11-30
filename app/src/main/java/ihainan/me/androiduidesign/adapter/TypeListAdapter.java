package ihainan.me.androiduidesign.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ihainan.me.androiduidesign.activities.ItemListStaggeredActivity;
import ihainan.me.androiduidesign.utils.CommonUtils;
import ihainan.me.androiduidesign.utils.GlobalVar;
import ihainan.me.androiduidesign.R;

/**
 * 类别列表 List Adapter.
 */
public class TypeListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mVi;

    /**
     * 用于优化 GridView
     */
    class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
    }

    public TypeListAdapter(Context c) {
        mContext = c;
        if (mContext != null)
            mVi = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return GlobalVar.TYPES_ENGLISH.size();
    }

    public Object getItem(int position) {
        return GlobalVar.TYPES_CHINESE.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final String p = (String) getItem(position);    // 获取类型信息

        /* 优化用 */
        ViewHolder holder = new ViewHolder();

        /* 更新 UI */
        if (convertView == null) {
            convertView = mVi.inflate(R.layout.content_type_item, null);
            holder.categoryImage = (ImageView) convertView.findViewById(R.id.type_image);
            holder.categoryName = (TextView) convertView.findViewById(R.id.type_name);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemListStaggeredActivity.class);
                    intent.putExtra(ItemListStaggeredActivity.TYPE_TAG, "TYPE");
                    intent.putExtra(ItemListStaggeredActivity.TEXT_TAG, p);
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /* 更新 UI */
        // TODO: 修复横屏 GridView Margin 不正确的 Bug
        if (holder.categoryName != null) holder.categoryName.setText(p);
        if (holder.categoryImage != null) {
            String uri = "@drawable/category" + GlobalVar.TYPES_ENGLISH.get(position).toLowerCase();
            Drawable res = CommonUtils.getDrawableByResourceName(mContext, uri);
            holder.categoryImage.setImageDrawable(res);
        }

        return convertView;
    }
}
