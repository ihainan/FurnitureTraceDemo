package ihainan.me.androiduidesign.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import ihainan.me.androiduidesign.activities.ItemDetailActivity;
import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.model.Furniture;

/**
 * 家具列表 Adapter
 */
public class StaggeredItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mVi;
    private List<Furniture> mFurnitureList;
    private int originImageHeight = -1;
    private Random random = new Random();

    public StaggeredItemAdapter(Context context, List<Furniture> furnitureList) {
        this.mContext = context;
        this.mFurnitureList = furnitureList;
        if (mContext != null) {
            mVi = LayoutInflater.from(mContext);
        }
    }

    @Override
    public int getCount() {
        return mFurnitureList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFurnitureList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Furniture furniture = (Furniture) getItem(position);

        if (convertView == null)
            convertView = mVi.inflate(R.layout.content_list_item, null);

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.item_image);
        TextView itemName = (TextView) convertView.findViewById(R.id.item_name);
        TextView itemFactory = (TextView) convertView.findViewById(R.id.item_factory);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.item_price);

        if (furniture != null) {
            // 随机更改图片大小
            android.view.ViewGroup.LayoutParams layoutParams = itemImage.getLayoutParams();
            if (originImageHeight == -1) originImageHeight = layoutParams.height;
            layoutParams.height = (int) (originImageHeight * (random.nextFloat() / 2 + 1.0));
            itemImage.setLayoutParams(layoutParams);
            if (furniture.getPic() != null && furniture.getPic().size() != 0)
                Picasso.with(mContext).load(furniture.getPic().get(0).getPicAdd()).into(itemImage);
            else Picasso.with(mContext).load(R.drawable.categorybed).into(itemImage);

            // 更新其他 UI
            itemName.setText("" + furniture.getFur_name());
            itemFactory.setText("" + furniture.getFur_brand());
            itemPrice.setText("￥" + furniture.getFur_price());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(ItemDetailActivity.ITEM_ID_, furniture.getFur_id());
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }
}