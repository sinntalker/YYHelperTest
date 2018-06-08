package com.sinntalker.sinntest20180503_yy.Fragment.health.BloodPressure;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/4.
 */

public class FamilyBloodDataBean extends BmobObject{

    private AllUserBean user;
    private String relations;
    private String BloodDiastolic; //舒张压 字符串 用于获取控件中字符串完成保存功能
    private String BloodSystolic; //收缩压 字符串 用于获取控件中字符串完成保存功能
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

    public String getBloodDiastolic() {
        return BloodDiastolic;
    }

    public void setBloodDiastolic(String bloodDiastolic) {
        BloodDiastolic = bloodDiastolic;
    }

    public String getBloodSystolic() {
        return BloodSystolic;
    }

    public void setBloodSystolic(String bloodSystolic) {
        BloodSystolic = bloodSystolic;
    }

    public String getSetTime() {
        return setTime;
    }

    public void setSetTime(String setTime) {
        this.setTime = setTime;
    }
}
