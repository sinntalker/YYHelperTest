package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

/**
 * Created by Administrator on 2018/5/25.
 */

public class DrugUsingDataBean extends DrugDataBean {

    //用药提醒 -- 提醒开关、吃药数量、时间段（1~4） -- 6项
    String remindUsingDrug;
    String usingDrugNumber;
    String usingDrugTimeNo1;
    String usingDrugTimeNo2;
    String usingDrugTimeNo3;
    String usingDrugTimeNo4;
    public String getRemindUsingDrug() {
        return remindUsingDrug;
    }
    public void setRemindUsingDrug(String remindUsingDrug) {
        this.remindUsingDrug = remindUsingDrug;
    }
    public String getUsingDrugNumber() {
        return usingDrugNumber;
    }
    public void setUsingDrugNumber(String usingDrugNumber) {
        this.usingDrugNumber = usingDrugNumber;
    }
    public String getUsingDrugTimeNo1() {
        return usingDrugTimeNo1;
    }
    public void setUsingDrugTimeNo1(String usingDrugTimeNo1) {
        this.usingDrugTimeNo1 = usingDrugTimeNo1;
    }
    public String getUsingDrugTimeNo2() {
        return usingDrugTimeNo2;
    }
    public void setUsingDrugTimeNo2(String usingDrugTimeNo2) {
        this.usingDrugTimeNo2 = usingDrugTimeNo2;
    }
    public String getUsingDrugTimeNo3() {
        return usingDrugTimeNo3;
    }
    public void setUsingDrugTimeNo3(String usingDrugTimeNo3) {
        this.usingDrugTimeNo3 = usingDrugTimeNo3;
    }
    public String getUsingDrugTimeNo4() {
        return usingDrugTimeNo4;
    }
    public void setUsingDrugTimeNo4(String usingDrugTimeNo4) {
        this.usingDrugTimeNo4 = usingDrugTimeNo4;
    }

    //复购提醒 -- 提醒开关、提醒开始时间、倒计时天数 -- 3项
    String remindBuyDrug;
    String remindBuyDate;
    String dayLeft;
    public String getRemindBuyDrug() {
        return this.remindBuyDrug;
    }
    public void setRemindBuyDrug(String remindBuyDrug) {
        this.remindBuyDrug = remindBuyDrug;
    }
    public String getRemindBuyDate() {
        return remindBuyDate;
    }
    public void setRemindBuyDate(String remindBuyDate) {
        this.remindBuyDate = remindBuyDate;
    }
    public String getDayLeft() {
        return dayLeft;
    }
    public void setDayLeft(String dayLeft) {
        this.dayLeft = dayLeft;
    }

}
