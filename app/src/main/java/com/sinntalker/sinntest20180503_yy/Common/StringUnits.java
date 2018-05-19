package com.sinntalker.sinntest20180503_yy.Common;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/5/16.
 */

public class StringUnits {

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        boolean isEmpty = false;
        if (null == str || str.equals("") || str.equals("null") || str.trim().length() == 0) {
            isEmpty = true;
        }
        return isEmpty;
    }

    /**
     * 判断是否为正确手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern p = Pattern
                .compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[0-9])[0-9]{8}$");

        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 验证手机格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileTypeNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        联通：130、131、132、152、155、156、185、186
        电信：133、153、180、189、（1349卫通）/^0?1[3|4|5|7|8][0-9]\d{8}$/
        总结起来就是第一位必定为1，第二位必定为3或5或8或7（电信运营商），其他位置的可以为0-9
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
