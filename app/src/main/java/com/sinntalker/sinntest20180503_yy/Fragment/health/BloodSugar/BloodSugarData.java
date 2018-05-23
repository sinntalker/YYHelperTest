package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by Administrator on 2018/5/23.
 */

@Table("Sugar")
public class BloodSugarData {
    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("Time")
    private String time;
    @Column("SugarValue")
    private String sugarValue;
    @Column("During")
    private String during;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSugarValue() {
        return sugarValue;
    }

    public void setSugarValue(String sugarValue) {
        this.sugarValue = sugarValue;
    }

    public String getDuring() {
        return during;
    }

    public void setDuring(String during) {
        this.during = during;
    }

    @Override
    public String toString() {
        return "BloodSugarData{" +
                "id=" + id +
                ", Time='" + time + '\'' +
                ", SugarValue='" + sugarValue + '\'' +
                ", During='" + during + '\'' +
                '}';
    }
}
