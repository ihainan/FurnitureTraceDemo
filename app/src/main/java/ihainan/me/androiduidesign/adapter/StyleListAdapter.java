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

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.activities.ItemListStaggered;
import ihainan.me.androiduidesign.utils.GlobalVar;

/**
 * 风格列表 Adapter.
 */
public class StyleListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mVi;

    public StyleListAdapter(Context c) {
        mContext = c;
        if (mContext != null)
            mVi = LayoutInflater.from(mContext);
    }

    public int getCount() {
        return GlobalVar.STYLES_ENGLISH.size();
    }

    public String getItem(int position) {
        return GlobalVar.STYLES_CHINESE.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String p = getItem(position);

        if (convertView == null)
            convertView = mVi.inflate(R.layout.content_style_item, null);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemListStaggered.class);
                intent.putExtra(ItemListStaggered.TYPE_TAG, "STYLE");
                intent.putExtra(ItemListStaggered.TEXT_TAG, p);
                mContext.startActivity(intent);
            }
        });

        if (p != null) {
            ImageView iv = (ImageView) convertView.findViewById(R.id.style_image);
            TextView tv = (TextView) convertView.findViewById(R.id.style_name);
            TextView tvShadow = (TextView) convertView.findViewById(R.id.style_name_shadow);
            TextView tvDescription = (TextView) convertView.findViewById(R.id.style_description);

            if (tv != null) tv.setText(p);
            if (tvShadow != null) tvShadow.setText(p);
            if (tvDescription != null) tvDescription.setText(GlobalVar.STYLE_DESC.get(position));
            if (iv != null) {
                String uri = "@drawable/style" + GlobalVar.STYLES_ENGLISH.get(position).toLowerCase();
                int imageResource = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
                Drawable res = mContext.getResources().getDrawable(imageResource);
                iv.setImageDrawable(res);
            }
        }

        return convertView;
    }
}
