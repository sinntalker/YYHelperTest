package com.sinntalker.sinntest20180503_yy.Fragment.family.model;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2018/5/31.
 */

public abstract class QueryUserListener extends BmobListener1<AllUserBean> {

    public abstract void done(AllUserBean s, BmobException e);

    @Override
    protected void postDone(AllUserBean o, BmobException e) {
        done(o, e);
    }
}
