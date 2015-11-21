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

import ihainan.me.androiduidesign.activities.ItemDetail;
import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.model.Furniture;

/**
 * 商品列表 List Adapter
 */
public class ListItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<Furniture> mFurnitures;
    private LayoutInflater mVi;
    private Random mRandom = new Random();

    /**
     * 构造器
     *
     * @param c                 Activity 上下文
     * @param furnitureItemList 需要显示的初始家具列表
     */
    public ListItemAdapter(Context c, List<Furniture> furnitureItemList) {
        mContext = c;
        mFurnitures = furnitureItemList;
        mVi = LayoutInflater.from(mContext);
    }

    /**
     * 添加新数据并更新列表
     *
     * @param newData 添加到尾部的新数据
     */
    public void updateList(List<Furniture> newData) {
        this.mFurnitures.addAll(newData);
        notifyDataSetChanged();
    }

    /**
     * 获取列表条目大小
     *
     * @return 条目大小
     */
    public int getCount() {
        return mFurnitures.size();
    }

    /**
     * 获取列表中指定位置的数据
     *
     * @param position 指定位置，不可超过列表的大小
     * @return 指定位置的条目
     */
    public Furniture getItem(int position) {
        return mFurnitures.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    /**
     * 用于优化 ListView
     */
    class ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemPrice;
        TextView itemCategory;
        TextView itemStyle;
        TextView itemFactory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* 获取对应的家具信息 */
        final Furniture furniture = getItem(position);

        /* 优化用，重复利用 holder 和 convertView */
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = mVi.inflate(R.layout.list_item_card, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetail.class);
                    mContext.startActivity(intent);
                }
            });
            holder.itemImage = (ImageView) convertView.findViewById(R.id.item_image);
            holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
            holder.itemPrice = (TextView) convertView.findViewById(R.id.item_price);
            holder.itemCategory = (TextView) convertView.findViewById(R.id.item_category);
            holder.itemStyle = (TextView) convertView.findViewById(R.id.item_style);
            holder.itemFactory = (TextView) convertView.findViewById(R.id.item_factory);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        /* 更新 UI */
        if (furniture != null) {
            holder.itemName.setText(furniture.getFur_name());
            holder.itemPrice.setText("￥" + furniture.getFur_price());
            holder.itemStyle.setText("风格: " + furniture.getFur_style());
            holder.itemCategory.setText("类型: " + furniture.getFur_type());
            holder.itemFactory.setText("品牌：" + furniture.getFur_brand());
            if (furniture.getPic() != null && furniture.getPic().size() != 0) {
                Picasso.with(mContext).load(furniture.getPic().get(0).getPicAdd()).into(holder.itemImage);
            }
        }

        return convertView;
    }
}
