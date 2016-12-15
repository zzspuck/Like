package com.zzs.like;

import android.app.Application;
import android.content.SharedPreferences;

import com.zzs.like.constants.AppPreferences;
import com.zzs.like.util.SharedPrefData;

import org.litepal.LitePal;

/**
 * Aplication类
 *
 * @author zzs
 * @date 2016.09.27
 */
public class LikeApplication extends Application {
    // 上下文
    private static LikeApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化LitePal数据库
        LitePal.initialize(this);
        mContext = this;
        // 得到音乐信息数据库
        LitePal.getDatabase();
        // 初始化SharedPreferences
        SharedPrefData.init(mContext, AppPreferences.PREF_FILE_NAME);
    }

    /**
     * 获取上下文
     * 
     * @return 上下文
     */
    public static LikeApplication getContext() {

        return mContext;
    }
}
