package com.sinntalker.sinntest20180503_yy.Fragment.family.util;

/**
 * Created by Administrator on 2018/5/31.
 */

public class Util {

    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

}
