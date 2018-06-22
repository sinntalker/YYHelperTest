package com.sinntalker.sinntest20180503_yy.Sql;

/**
 * Created by Administrator on 2018/6/20.
 */

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import cn.bmob.v3.BmobObject;

/**
 * 数据库文件创建
 * 1. 保存药品闹钟数据
 * 2. 设置当前所有未读状态消息闹钟
 * 3. 当闹钟到来时，将对应的闹钟数据填充到另外一张已提示闹钟表中；
 * 4. 闹钟进行提醒，用药消息处提示有信息未读。
 */

@Entity
public class MessageDataBean extends BmobObject{

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "UserId")
    private String user; //当前用户
    @Property(nameInDb = "drugNum")
    private int drugNum; //药箱编号
    @Property(nameInDb = "drug")
    private String drug; //当前药物
    @Property(nameInDb = "dosage")
    private String dosage; //用法用量
    @Property(nameInDb = "num")
    private Integer num; //第几次提醒
    @Property(nameInDb = "timeH")
    private Integer timeH; //闹钟时间小时
    @Property(nameInDb = "timeM")
    private Integer timeM; //闹钟时间分钟
    @Property(nameInDb = "isTouched")
    private Boolean isTouched; //是否已读
    @Property(nameInDb = "touchedTime")
    private String touchedTime; //已读时间
    @Property(nameInDb = "requestCode")
    private Integer requestCode; //回调编码

    public MessageDataBean(String user, int drugNum, String drug,
                           String dosage, Integer num, Integer timeH, Integer timeM,
                           Boolean isTouched, String touchedTime, Integer requestCode) {
        this.user = user;
        this.drugNum = drugNum;
        this.drug = drug;
        this.dosage = dosage;
        this.num = num;
        this.timeH = timeH;
        this.timeM = timeM;
        this.isTouched = isTouched;
        this.touchedTime = touchedTime;
        this.requestCode = requestCode;
    }

    @Generated(hash = 1809113650)
    public MessageDataBean(Long id, String user, int drugNum, String drug,
            String dosage, Integer num, Integer timeH, Integer timeM,
            Boolean isTouched, String touchedTime, Integer requestCode) {
        this.id = id;
        this.user = user;
        this.drugNum = drugNum;
        this.drug = drug;
        this.dosage = dosage;
        this.num = num;
        this.timeH = timeH;
        this.timeM = timeM;
        this.isTouched = isTouched;
        this.touchedTime = touchedTime;
        this.requestCode = requestCode;
    }
    @Generated(hash = 1119038899)
    public MessageDataBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUser() {
        return this.user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getDrug() {
        return this.drug;
    }
    public void setDrug(String drug) {
        this.drug = drug;
    }
    public String getDosage() {
        return this.dosage;
    }
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    public Integer getNum() {
        return this.num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public Integer getTimeH() {
        return this.timeH;
    }
    public void setTimeH(Integer timeH) {
        this.timeH = timeH;
    }
    public Integer getTimeM() {
        return this.timeM;
    }
    public void setTimeM(Integer timeM) {
        this.timeM = timeM;
    }
    public Boolean getIsTouched() {
        return this.isTouched;
    }
    public void setIsTouched(Boolean isTouched) {
        this.isTouched = isTouched;
    }
    public String getTouchedTime() {
        return this.touchedTime;
    }
    public void setTouchedTime(String touchedTime) {
        this.touchedTime = touchedTime;
    }
    public Integer getRequestCode() {
        return this.requestCode;
    }
    public void setRequestCode(Integer requestCode) {
        this.requestCode = requestCode;
    }
    public int getDrugNum() {
        return this.drugNum;
    }
    public void setDrugNum(int drugNum) {
        this.drugNum = drugNum;
    }

    @Override
    public String toString() {
        return "MessageDataBean{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", drugNum=" + drugNum +
                ", drug='" + drug + '\'' +
                ", dosage='" + dosage + '\'' +
                ", num=" + num +
                ", timeH=" + timeH +
                ", timeM=" + timeM +
                ", isTouched=" + isTouched +
                ", touchedTime='" + touchedTime + '\'' +
                ", requestCode=" + requestCode +
                '}';
    }
}
