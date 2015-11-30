package ihainan.me.androiduidesign.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车单例（伪）
 */
public class ShoppingCart {
    private ShoppingCart() {
        shoppingCartItemList = new ArrayList<ShoppingCartItem>();
    }

    private static ShoppingCart instance;
    private List<ShoppingCartItem> shoppingCartItemList;

    public static ShoppingCart getInstance() {
        if (instance == null) {
            newInstance();
        }
        return instance;
    }

    /**
     * 获取购物车中的第 i 个条目
     *
     * @param i 指定条目
     * @return 第 i 个条目
     */
    public ShoppingCartItem getItem(int i) {
        return shoppingCartItemList.get(i);
    }

    /**
     * 获取购物车大小（重复算一个）
     *
     * @return 购物车大小
     */
    public int getCount() {
        return shoppingCartItemList.size();
    }

    /**
     * 获取购物车所有物品总量（重复算两个）
     *
     * @return 购物车所有物品总量（重复算两个）
     */
    public int getSum() {
        int sum = 0;
        for (ShoppingCartItem item : shoppingCartItemList) sum += item.getQuantity();
        return sum;
    }

    private static synchronized void newInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
    }

    /**
     * 清除所有物品
     */
    public void clearAll(){
        shoppingCartItemList.clear();
    }

    /**
     * 添加商品 / 修改购物车商品数量
     *
     * @param furID    需要添加 / 修改的家具的编号
     * @param quantity 需要添加 / 修改的家具数量
     */
    public void setShoppingCartItem(int furID, int quantity) {
        for (ShoppingCartItem item : shoppingCartItemList) {
            if (furID == item.getFurID()) {
                item.setQuantity(quantity);
                return;
            }
        }

        shoppingCartItemList.add(new ShoppingCartItem(furID, quantity));
    }

    /**
     * 获取指定 ID 商品在购物车中的数量
     *
     * @param furId 商品 ID
     * @return 购物车中的数量
     */
    public int getQuantity(int furId) {
        for (ShoppingCartItem item : shoppingCartItemList) {
            if (furId == item.getFurID()) {
                return item.getQuantity();
            }
        }

        return 0;
    }

    /**
     * 购物车单个商品
     */
    public static class ShoppingCartItem {
        private int mFurID;
        private int mQuantity;

        public int getFurID() {
            return mFurID;
        }

        public int getQuantity() {
            return mQuantity;
        }

        public ShoppingCartItem(int furID, int quantity) {
            mFurID = furID;
            mQuantity = quantity;
        }

        /**
         * 设置商品数量
         *
         * @param quantity 商品数量
         */
        public void setQuantity(int quantity) {
            mQuantity = quantity;
        }
    }

}
