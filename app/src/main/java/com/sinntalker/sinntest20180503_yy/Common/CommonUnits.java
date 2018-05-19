package com.sinntalker.sinntest20180503_yy.Common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/5/16.
 */

public class CommonUnits {

    /**
     * 显示Toast消息
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
