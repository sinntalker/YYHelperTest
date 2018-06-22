package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/15.
 */

public class AlarmBean extends BmobObject {

    //空构造
    public AlarmBean() {}

    private AllUserBean user; //当前用户
    private int drugNum; //药箱编号
    private String drug; //当前药物
    private String dosage; //用法用量
    private Integer num; //第几次提醒
    private Integer timeH; //闹钟时间小时
    private Integer timeM; //闹钟时间分钟
    private Boolean isTouched; //是否已读
    private String touchedTime; //已读时间
    private Integer requestCode; //回调编码

    public int getDrugNum() {
        return drugNum;
    }

    public void setDrugNum(int drugNum) {
        this.drugNum = drugNum;
    }

    public AllUserBean getUser() {
        return user;
    }
    public String getDrug() {
        return drug;
    }
    public String getDosage() {
        return dosage;
    }
    public Integer getTimeH() {
        return timeH;
    }
    public Integer getTimeM() {
        return timeM;
    }
    public Boolean getTouched() {
        return isTouched;
    }
    public String getTouchedTime() {
        return touchedTime;
    }
    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Integer requestCode) {
        this.requestCode = requestCode;
    }

    public void setUser(AllUserBean user) {
        this.user = user;
    }
    public void setDrug(String drug) {
        this.drug = drug;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public void setTimeH(Integer timeH) {
        this.timeH = timeH;
    }
    public void setTimeM(Integer timeM) {
        this.timeM = timeM;
    }
    public void setTouched(Boolean touched) {
        isTouched = touched;
    }
    public void setTouchedTime(String touchedTime) {
        this.touchedTime = touchedTime;
    }

    @Override
    public String toString() {
        return "AlarmBean{" +
                "user=" + user +
                ", drug='" + drug + '\'' +
                ", dosage='" + dosage + '\'' +
                ", num=" + num +
                ", timeH=" + timeH +
                ", timeM=" + timeM +
                ", isTouched=" + isTouched +
                ", touchedTime='" + touchedTime + '\'' +
                ", requestCode=" + requestCode +
                ", drugNum=" + drugNum +
                '}';
    }
}
