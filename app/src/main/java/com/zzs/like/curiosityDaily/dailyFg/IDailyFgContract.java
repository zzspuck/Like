package com.zzs.like.curiosityDaily.dailyFg;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zzs.like.data.daily.DailyTimeLine;

/**
 * 接口
 *
 * @author zzs
 * @date 2016.09.28
 * @note This specifies the contract between the view and the presenter
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public interface IDailyFgContract {
    /**
     * view接口
     */
    interface IDailyFgView {
        /**
         * 获取数据成功
         *
         * @param dailyTimeLine 数据
         */
        void getDataSuc(DailyTimeLine dailyTimeLine);

        /**
         * 获取数据失败
         *
         * @param throwable 异常
         */
        void getDataFaild(Throwable throwable);

        /**
         * 设置是否刷新
         * @param refresh true:刷新 false：停止刷新
         */
        void setDataRefresh(boolean refresh);
    }

    /**
     * presenter接口
     */
    interface IDailyFgPresenter {
        /**
         * 获取DailyTimeLine详情页
         *
         * @param num 页数
         */
        void getDailyTimeLine(String num);

        /**
         * 展示日报
         *
         * @param context       上下文
         * @param dailyTimeLine daily
         * @param recyclerView  rv
         */
        void disPlayDailyTimeLine(Context context, DailyTimeLine dailyTimeLine, RecyclerView recyclerView);

        /**
         * recycleView滚动监听
         */
        void scrollRecycleView(RecyclerView recyclerView, LinearLayoutManager linearLayoutManager);

        /**
         * 加载失败
         *
         * @param throwable 异常
         */
        void loadError(Throwable throwable);
    }
}
