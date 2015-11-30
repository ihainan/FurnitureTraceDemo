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

import ihainan.me.androiduidesign.activities.ItemListStaggered;
import ihainan.me.androiduidesign.utils.GlobalVar;
import ihainan.me.androiduidesign.R;

/**
 * 类别列表 List Adapter.
 */
public class CategoryListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mVi;

    /**
     * 用于优化 GridView
     */
    class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
    }

    public CategoryListAdapter(Context c) {
        mContext = c;
        if (mContext != null)
            mVi = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return GlobalVar.CATEGORIES_ENGLISH.size();
    }

    public Object getItem(int position) {
        return GlobalVar.CATEGORIES_CHINESE.get(position);
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
            convertView = mVi.inflate(R.layout.content_category_item, null);
            holder.categoryImage = (ImageView) convertView.findViewById(R.id.category_image);
            holder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(holder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemListStaggered.class);
                    intent.putExtra(ItemListStaggered.TYPE_TAG, "TYPE");
                    intent.putExtra(ItemListStaggered.TEXT_TAG, p);
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /* 更新 UI */
        if (holder.categoryName != null) holder.categoryName.setText(p);
        if (holder.categoryImage != null) {
            String uri = "@drawable/category" + GlobalVar.CATEGORIES_ENGLISH.get(position).toLowerCase();
            int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
            Drawable res = mContext.getResources().getDrawable(imageResource);
            holder.categoryImage.setImageDrawable(res);
        }

        return convertView;
    }
}
