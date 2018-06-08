package com.sinntalker.sinntest20180503_yy.Fragment.user;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/4.
 */

public class FamilyBean extends BmobObject {

    private AllUserBean user;
//    private String avatar;
    private String relations;
    private int age;
    private String area;
    private String phone;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfo() {
        return "当前家庭成员为：" + relations + "， 年龄：" + age + "， 居住地:" + area + "， 手机号：" + phone;
    }

    //详细信息 包括 出生日期（birthday）、性别（sex）、所在地区（area）、身高（height）、证件类型（IDcardType）、证件号码（IDNumber）
    String name; //用户名称
    String birth; //yyyy-MM-dd
    String sex; //男 0， 女 1
//    String area; // 省-市-县/区
    String height;
    String IDCardType; //身份证 0， 护照 1
    String IDNumber;
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBirth() {
        return this.birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
//    public String getArea() {
//        return this.area;
//    }
//    public void setArea(String area) {
//        this.area = area;
//    }
    public String getHeight() {
        return this.height;
    }
    public void setHeight(String height) {
        this.height = height;
    }
    public String getIDCardType() {
        return this.IDCardType;
    }
    public void setIDCardType(String IDCardType) {
        this.IDCardType = IDCardType;
    }
    public String getIDNumber() {
        return this.IDNumber;
    }
    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }
}
