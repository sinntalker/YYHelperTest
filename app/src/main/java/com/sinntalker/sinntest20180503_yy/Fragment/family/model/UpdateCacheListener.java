package com.sinntalker.sinntest20180503_yy.Fragment.family.model;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Administrator on 2018/5/31.
 */

public abstract class UpdateCacheListener extends BmobListener1 {

    public abstract void done(BmobException e);

    @Override
    protected void postDone(Object o, BmobException e) {
        done(e);
    }

}