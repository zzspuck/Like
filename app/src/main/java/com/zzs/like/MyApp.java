package com.zzs.like;

import android.app.Application;
import android.content.Context;

/**
 * Aplication类
 *
 * @author zzs
 * @date 2016.09.27
 */
public class MyApp extends Application {
    private static final String DB_NAME = "weibo.db";

    private static MyApp mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static MyApp getContext() {

        return mContext;
    }
}
