package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by Administrator on 2018/5/22.
 */

@Table("Pressure")
public class BloodPressureData {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("today")
    private String today;
    @Column("maxPressure")
    private String maxPressure;
    @Column("minPressure")
    private String minPressure;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getMaxPressure() {
        return maxPressure;
    }

    public void setMaxPressure(String maxPressure) {
        this.maxPressure = maxPressure;
    }

    public String getMinPressure() {
        return minPressure;
    }

    public void setMinPressure(String minPressure) {
        this.minPressure = minPressure;
    }

    @Override
    public String toString() {
        return "BloodPressureData{" +
                "id=" + id +
                ", today='" + today + '\'' +
                ", maxPressure='" + maxPressure + '\'' +
                ", minPressure='" + minPressure + '\'' +
                '}';
    }
}
