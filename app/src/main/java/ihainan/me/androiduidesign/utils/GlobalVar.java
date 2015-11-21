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
        Collections.addAll(CATEGORIES_ENGLISH, new String[]{"Chair", "Bed", "Sofa", "Stool", "Chair", "Bed", "Sofa", "Stool"});
        Collections.addAll(CATEGORIES_CHINESE, new String[]{"椅子", "床", "沙发", "凳子", "椅子", "床", "沙发", "凳子"});
        Collections.addAll(STYLES_ENGLISH, new String[]{"Modern", "European", "Chinese",  "Pastoralism", "Children"});
        Collections.addAll(STYLES_CHINESE, new String[]{"简约现代", "欧式古典", "中式", "韩式田园",  "儿童"});
        Collections.addAll(STYLE_DESC, new String[]{
                "简单与务实",
                "华丽与高雅",
                "传统与现代",
                "Description of Classicism Style",
                "Description of Children Style",
                "Description of Pastoralism Style"});
    }
}
