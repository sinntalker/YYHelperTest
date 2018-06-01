package com.sinntalker.sinntest20180503_yy.Fragment.family.base;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 抽象的图片加载接口
 * Created by Administrator on 2018/5/31.
 */

public interface ILoader {

    /**
     * 加载圆形头像
     * @param iv
     * @param url
     * @param defaultRes
     */
    void loadAvator(ImageView iv, String url, int defaultRes);

    /**
     * 加载图片，添加监听器
     * @param iv
     * @param url
     * @param defaultRes
     * @param listener
     */
    void load(ImageView iv,String url,int defaultRes,ImageLoadingListener listener);

}
