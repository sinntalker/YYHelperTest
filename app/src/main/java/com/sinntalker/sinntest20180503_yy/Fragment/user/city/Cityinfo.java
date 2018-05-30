package com.sinntalker.sinntest20180503_yy.Fragment.user.city;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/30.
 */

public class Cityinfo implements Serializable {

    private String id;
    private String city_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    @Override
    public String toString() {
        return "Cityinfo [id=" + id + ", city_name=" + city_name + "]";
    }

}
