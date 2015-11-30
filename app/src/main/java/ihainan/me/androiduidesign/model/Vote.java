package ihainan.me.androiduidesign.model;

import java.util.Date;

/**
 * 投票实体类
 */
public class Vote {
    private int vot_id;
    private int fur_id;
    private int userid;
    private int vot_approve;
    private int vot_oppose;
    private String vot_des;
    private Date vot_date;

    public int getVot_id() {
        return vot_id;
    }

    public void setVot_id(int vot_id) {
        this.vot_id = vot_id;
    }

    public int getFur_id() {
        return fur_id;
    }

    public void setFur_id(int fur_id) {
        this.fur_id = fur_id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getVot_approve() {
        return vot_approve;
    }

    public void setVot_approve(int vot_approve) {
        this.vot_approve = vot_approve;
    }

    public int getVot_oppose() {
        return vot_oppose;
    }

    public void setVot_oppose(int vot_oppose) {
        this.vot_oppose = vot_oppose;
    }

    public String getVot_des() {
        return vot_des;
    }

    public void setVot_des(String vot_des) {
        this.vot_des = vot_des;
    }

    public Date getVot_date() {
        return vot_date;
    }

    public void setVot_date(Date vot_date) {
        this.vot_date = vot_date;
    }
}
