package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodSugar;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/4.
 */

public class FamilySugarValueBean extends BmobObject{

    private AllUserBean user;
    private String relations;
    private String sugarValue; //血压值
    private String during; //测量时间
    private String setTime;

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

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }
}
