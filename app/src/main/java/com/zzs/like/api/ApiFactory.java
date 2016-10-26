package com.zzs.like.api;

/**
 * ApiFactory工厂
 *
 * @author zzs
 * @date 2016.09.26
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class ApiFactory {
    private static final Object monitor = new Object();
    // 日报API
    private static DailyApi mDailyApiSingleton;

    /**
     * 返回DailyApi的单例
     *
     * @return DailyApi
     */
    public static DailyApi getDailyApiSingleton() {
        if (mDailyApiSingleton == null) {
            synchronized (monitor) {
                if (mDailyApiSingleton == null) {
                    mDailyApiSingleton = new ApiRetrofit().getDailyRetrofit();
                }
            }
        }

        return mDailyApiSingleton;
    }
}
