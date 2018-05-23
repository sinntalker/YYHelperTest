package com.sinntalker.sinntest20180503_yy.Fragment.health.Weight;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by Administrator on 2018/5/23.
 */

@Table("Weight")
public class WeightData {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column("Time")
    private String time;
    @Column("Weight")
    private String weight;

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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WeightData{" +
                "id=" + id +
                ", Time='" + time + '\'' +
                ", Weight='" + weight + '\'' +
                '}';
    }

}
