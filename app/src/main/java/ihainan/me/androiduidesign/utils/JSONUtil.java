package ihainan.me.androiduidesign.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import ihainan.me.androiduidesign.model.Furniture;
import ihainan.me.androiduidesign.model.User;
import ihainan.me.androiduidesign.model.Vote;

/**
 * JSON 解析工具类
 *
 * @author LiNan
 */
public class JSONUtil {
    /**
     * 从 JSON 数据中解析得到投票信息
     *
     * @param jsonData JSON 文本数据
     * @return Vote 类列表
     */
    public static List<Vote> parseVoteList(String jsonData){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        List<Vote> voteList = gson.fromJson(jsonData, new TypeToken<List<Vote>>(){}.getType());
        return voteList;
    }

    /**
     * 从 JSON 数据中解析得到用户验证结果信息
     *
     * @param jsonData JSON 文本数据
     * @return User 类实例
     */
    public static User parseUser(String jsonData){
        Gson gson = new Gson();
        User user = gson.fromJson(jsonData, User.class);
        return user;
    }

    /**
     * 从 JSON 数据中解析得到家具信息
     *
     * @param jsonData JSON 文本数据
     * @return Furniture 类实例
     */
    public static Furniture parseFur(String jsonData) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Furniture furniture = gson.fromJson(jsonData, Furniture.class);
        return furniture;
    }

    /**
     * 从 JSON 数据中解析得到家具列表信息
     *
     * @param jsonData JSON 文本数据
     * @return Furniture 类列表
     */
    public static List<Furniture> parseFurList(String jsonData) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        List<Furniture> furList = gson.fromJson(jsonData,
                new TypeToken<List<Furniture>>() {
                }.getType());
        return furList;
    }

    /**
     * 从 JSON 数据中解析得到投票信息
     *
     * @param jsonData JSON 文本数据
     * @return Vote 类实例
     */
    public static Vote parseVote(String jsonData){
        Gson gson = new Gson();
        Vote vote = gson.fromJson(jsonData, Vote.class);
        return vote;
    }
}
