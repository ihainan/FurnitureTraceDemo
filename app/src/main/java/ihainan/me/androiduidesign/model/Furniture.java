package ihainan.me.androiduidesign.model;

import java.util.Date;
import java.util.List;

/**
 * 价格实体类
 */
public class Furniture {
    private int fur_id;
    private String fur_name;
    private String fur_brand;
    private String fur_type;
    private String fur_style;
    private float fur_price;
    private String fur_material;
    private Date fur_date;
    private List<Pic> pic;

    public int getFur_id() {
        return fur_id;
    }

    public void setFur_id(int fur_id) {
        this.fur_id = fur_id;
    }

    public String getFur_name() {
        return fur_name;
    }

    public void setFur_name(String fur_name) {
        this.fur_name = fur_name;
    }

    public String getFur_brand() {
        return fur_brand;
    }

    public void setFur_brand(String fur_brand) {
        this.fur_brand = fur_brand;
    }

    public String getFur_type() {
        return fur_type;
    }

    public void setFur_type(String fur_type) {
        this.fur_type = fur_type;
    }

    public String getFur_style() {
        return fur_style;
    }

    public void setFur_style(String fur_style) {
        this.fur_style = fur_style;
    }

    public float getFur_price() {
        return fur_price;
    }

    public void setFur_price(float fur_price) {
        this.fur_price = fur_price;
    }

    public String getFur_material() {
        return fur_material;
    }

    public void setFur_material(String fur_factory) {
        this.fur_material = fur_factory;
    }

    public Date getFur_date() {
        return fur_date;
    }

    public void setFur_date(Date fur_date) {
        this.fur_date = fur_date;
    }

    public List<Pic> getPic() {
        return pic;
    }

    public void setPic(List<Pic> pic) {
        this.pic = pic;
    }
}
