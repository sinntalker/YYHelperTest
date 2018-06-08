package com.sinntalker.sinntest20180503_yy.Fragment.health.Weight;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/6.
 */

public class FamilyWeightBean extends BmobObject {

    private AllUserBean user;
    private String relations;
    private String setTime;
    private String weight;

    public AllUserBean getUser() {
        return user;
    }

    public void setUser(AllUserBean user) {
        this.user = user;
    }

    public String getRelations() {
        return relations;
    }

    public void setRelations(String relations) {
        this.relations = relations;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getInfo() {
        return "当前数据为：user:" + user
                + ", setTime:" + setTime
                + ", weight:" + weight + "\n";
    }

}
