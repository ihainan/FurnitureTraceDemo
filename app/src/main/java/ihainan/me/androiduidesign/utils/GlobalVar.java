package ihainan.me.androiduidesign.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 全局变量
 */
public class GlobalVar {
    /**
     * 家具类别
     */
    public final static List<String> CATEGORIES_ENGLISH = new ArrayList<>();
    public final static List<String> CATEGORIES_CHINESE = new ArrayList<>();

    /**
     * 家具风格
     */
    public final static List<String> STYLES_CHINESE = new ArrayList<>();
    public final static List<String> STYLES_ENGLISH = new ArrayList<>();

    /**
     * 家具风格描述
     */
    public final static List<String> STYLE_DESC = new ArrayList<>();

    static {
        Collections.addAll(CATEGORIES_ENGLISH, new String[]{"bed", "chest", "sofa", "tvbench", "teatable", "dinnertable", "dinnerchair", "bookshelf"});
        Collections.addAll(CATEGORIES_CHINESE, new String[]{"床", "衣柜", "沙发", "电视柜", "茶几", "餐桌", "餐椅", "书柜"});
        Collections.addAll(STYLES_ENGLISH, new String[]{"Modern", "European", "Chinese", "Pastoralism"});
        Collections.addAll(STYLES_CHINESE, new String[]{"简约现代", "欧式古典", "中式现代", "美式乡村"});
        Collections.addAll(STYLE_DESC, new String[]{
                "以少胜多，以简胜繁，安静与祥和",
                "华丽装饰，浓烈色彩，惬意与浪漫",
                "格调雅致，内涵丰富，山水与泼墨",
                "贴近自然，清纯脱俗，朴实与亲切",
                "Description of Children Style",
                "Description of Pastoralism Style"});
    }
}
