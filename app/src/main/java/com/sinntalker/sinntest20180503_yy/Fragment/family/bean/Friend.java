package com.sinntalker.sinntest20180503_yy.Fragment.family.bean;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/31.
 */

public class Friend extends BmobObject{

    private AllUserBean user;
    private AllUserBean friendUser;

    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public AllUserBean getUser() {
        return user;
    }

    public void setUser(AllUserBean user) {
        this.user = user;
    }

    public AllUserBean getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(AllUserBean friendUser) {
        this.friendUser = friendUser;
    }

}
