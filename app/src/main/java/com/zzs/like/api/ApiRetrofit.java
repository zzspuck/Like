package com.zzs.like.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiRetrofit
 *
 * @author zzs
 * @date 2016.09.26
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class ApiRetrofit {
    // daily 的基础地址
    public static final String DAILY_BASE_URL = "http://app3.qdaily.com/app3/";
    private final DailyApi mDailyApi;

    /**
     * 构造方法
     */
    public ApiRetrofit() {
        Retrofit dailyRetrofit = new Retrofit.Builder()
                .baseUrl(DAILY_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mDailyApi = dailyRetrofit.create(DailyApi.class);
    }

    /**
     * 获取DailyApi
     *
     * @return  DailyApi
     */
    public DailyApi getDailyRetrofit() {

        return mDailyApi;
    }
}

