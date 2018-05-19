package com.sinntalker.sinntest20180503_yy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sinntalker.sinntest20180503_yy.Common.Constant;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2018/5/17.
 */

public class AppRegister extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(Constant.APP_ID_WX);
    }
}
