package com.sinntalker.sinntest20180503_yy;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2018/5/16.
 */

public class UserAuthBean extends BmobObject{
//    1、snsType:只能是三种取值中的一种：weibo、qq、weixin
//
//2、accessToken：接口调用凭证
//
//3、expiresIn：access_token的有效时间
//
//4、userId:用户身份的唯一标识，对应微博授权信息中的uid,对应qq和微信授权信息中的openid

    //获取第三方登陆信息
    String snsType; //登陆方式 weibo、 qq、 weixin
    String accessToken; //接口调用凭证
    Long expiresIn; //token的有效时间
    String userId; //用户身份的唯一标识
    public String nick; //昵称
    String userName;
    String userPassword;

    public String getSnsType() { return this.snsType; }
    public String getAccessToken() { return this.accessToken; }
    public Long getExpiresIn() { return this.expiresIn; }
    public String getUserId() { return this.userId; }
    public String getUserName() { return this.userName; }
    public String getUserPassword() { return this.userPassword; }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        if(this.snsType == "weibo" ){
            this.nick = "微博用户"+this.userId;
        }else if(this.snsType == "qq" ){
            this.nick = "QQ用户"+this.userId;
        }else if(this.snsType == "weixin" ){
            this.nick = "微信用户"+this.userId;
        }else {
            this.nick = "undefined";
        }
    }

    public void setSnsType(String snsType) { this.snsType = snsType; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUserName(String snsType) {
        if(this.snsType == "weibo" ){
            this.userName = "微博用户"+this.userId;
        }else if(this.snsType == "qq" ){
            this.userName = "QQ用户"+this.userId;
        }else if(this.snsType == "weixin" ){
            this.userName = "微信用户"+this.userId;
        }else {
            this.userName = "undefined";
        }
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
