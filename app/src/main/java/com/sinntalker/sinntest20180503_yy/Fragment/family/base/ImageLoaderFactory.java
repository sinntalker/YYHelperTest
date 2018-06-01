package com.sinntalker.sinntest20180503_yy.Fragment.family.base;

/**
 * Created by Administrator on 2018/5/31.
 */

public class ImageLoaderFactory {

    private static volatile ILoader sInstance;

    private ImageLoaderFactory() {}

    public static ILoader getLoader() {
        if (sInstance == null) {
            synchronized (ImageLoaderFactory.class) {
                if (sInstance == null) {
                    sInstance = new UniversalImageLoader();
                }
            }
        }
        return sInstance;
    }

}
