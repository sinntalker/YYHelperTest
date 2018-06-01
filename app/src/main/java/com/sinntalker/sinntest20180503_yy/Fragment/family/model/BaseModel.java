package com.sinntalker.sinntest20180503_yy.Fragment.family.model;

import android.content.Context;

import com.sinntalker.sinntest20180503_yy.Fragment.family.all.BmobIMApplication;

/**
 * Created by Administrator on 2018/5/31.
 */

public class BaseModel {

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return BmobIMApplication.INSTANCE();
    }

}
