package ihainan.me.androiduidesign.model;

import java.util.Date;

/**
 * 追溯日志类
 */
public class Logistics {
    private int log_id;
    private int fur_id;
    private Date log_date;
    private String log_des;

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public int getFur_id() {
        return fur_id;
    }

    public void setFur_id(int fur_id) {
        this.fur_id = fur_id;
    }

    public Date getLog_date() {
        return log_date;
    }

    public void setLog_date(Date log_date) {
        this.log_date = log_date;
    }

    public String getLog_des() {
        return log_des;
    }

    public void setLog_des(String log_des) {
        this.log_des = log_des;
    }
}
