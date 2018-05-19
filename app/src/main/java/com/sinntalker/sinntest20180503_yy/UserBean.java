package com.sinntalker.sinntest20180503_yy;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2018/5/16.
 */

public class UserBean extends BmobUser{

    Boolean sex; //性别
    Integer age; //年龄
    Integer code;
//    private String userName;
//    private String userPassword;

    //    1、snsType:只能是三种取值中的一种：weibo、qq、weixin
//
//2、accessToken：接口调用凭证
//
//3、expiresIn：access_token的有效时间
//
//4、userId:用户身份的唯一标识，对应微博授权信息中的uid,对应qq和微信授权信息中的openid

    //获取第三方登陆信息
    private String snsType; //登陆方式 weibo、 qq、 weixin
    private String accessToken; //接口调用凭证
    private Long expiresIn; //token的有效时间
    private String userId; //用户身份的唯一标识
    private String nick; //昵称
//    String userName;
//    String userPassword;

    public String getSnsType() { return this.snsType; }
    public String getAccessToken() { return this.accessToken; }
    public Long getExpiresIn() { return this.expiresIn; }
    public String getUserId() { return this.userId; }
//    public String getUserName() { return this.userName; }
//    public String getUserPassword() { return this.userPassword; }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        if(this.snsType.equals("weibo") ){
            this.nick = "微博用户"+this.userId;
        }else if(this.snsType.equals("qq")){
            this.nick = "QQ用户"+this.userId;
        }else if(this.snsType.equals("weixin")){
            this.nick = "微信用户"+this.userId;
        }else {
            this.nick = "undefined";
        }
    }

    public void setSnsType(String snsType) { this.snsType = snsType; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    public void setUserId(String userId) { this.userId = userId; }
//    public void setUserName(String snsType) {
//        if(this.snsType == "weibo" ){
//            this.userName = "微博用户"+this.userId;
//        }else if(this.snsType == "qq" ){
//            this.userName = "QQ用户"+this.userId;
//        }else if(this.snsType == "weixin" ){
//            this.userName = "微信用户"+this.userId;
//        }else {
//            this.userName = "undefined";
//        }
//    }
//    public void setUserPassword(String userPassword) {
//        this.userPassword = userPassword;
//    }

    public boolean getSex() {
        return this.sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

//    public String getUserName() {
//        return this.userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserPassword() {
//        return this.userPassword;
//    }
//
//    public void setUserPassword(String userPassword) {
//        this.userPassword = userPassword;
//    }

}
