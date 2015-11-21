package ihainan.me.androiduidesign.model;

/**
 * 图像实体类
 */
public class Pic {
    private int pic_id;
    private int fur_id;
    private String pic_add;
    private String pic_des;

    public int getPicId() {
        return pic_id;
    }

    public void setPicId(int pic_id) {
        this.pic_id = pic_id;
    }

    public int getFurId() {
        return fur_id;
    }

    public void setFurId(int fur_id) {
        this.fur_id = fur_id;
    }

    public String getPicAdd() {
        return pic_add;
    }

    public void setPicAdd(String pic_add) {
        this.pic_add = pic_add;
    }

    public String getPicDes() {
        return pic_des;
    }

    public void setPicDes(String pic_des) {
        this.pic_des = pic_des;
    }
}