package com.sinntalker.sinntest20180503_yy;

import android.content.Context;
import android.widget.ImageView;

import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriend;

import java.net.IDN;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2018/5/24.
 */

public class AllUserBean extends BmobUser{

    public AllUserBean(){}

    public AllUserBean(NewFriend friend){
        setObjectId(friend.getUid());
        setUserNick(friend.getName());
        setUserAvatar(friend.getAvatar());
    }

    //通用 用户登陆方式 -- 手机号登陆、微博、QQ、微信登陆
    String snsType; //登陆方式 phone、weibo、 qq、 weixin
    public String getSnsType() {
        return this.snsType;
    }
    public void setSnsType(String snsType) {
        this.snsType = snsType;
    }

    //登陆
    //主要包括 登陆手机号、登陆密码（其中用户名称与登陆手机号相同）
    //-- 即 userName \ userPassword \ userPhone ，另 注册所需的验证码 code 保存
//    private String username; BombUser中已声明
//    private String password; BombUser中已声明
//    private String mobilePhoneNumber; BombUser中已声明
    Integer code;
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

    //第三方信息
    //主要包括 用户接口凭证（accessToken）、token的有效时间（expiresIn）、用户身份的唯一标识（userId）
    String accessToken; //接口调用凭证
    Long expiresIn; //token的有效时间
    String userId; //用户身份的唯一标识
    public String getAccessToken() {
        return this.accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public Long getExpiresIn() {
        return this.expiresIn;
    }
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    //个人信息 包括用户昵称（userNick）、用户头像（userAvatar）、个性签名（signature）
    String userNick;
    String userAvatar;
    String signature;
    public String getUserNick() {
        return this.userNick;
    }
    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
    public String getUserAvatar() {
        return this.userAvatar;
    }
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
    public String getSignature() {
        return this.signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }

    //详细信息 包括 出生日期（birthday）、性别（sex）、所在地区（area）、身高（height）、证件类型（IDcardType）、证件号码（IDNumber）
    String name; //用户名称
    String birth; //yyyy-MM-dd
    String sex; //男 0， 女 1
    String area; // 省-市-县/区
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
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
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
