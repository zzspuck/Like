package com.zzs.like.music.localMusicFg;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * 本地音乐界面接口
 *
 * @author zzs
 * @date 2016.11.01
 * @note This specifies the contract between the view and the presenter
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public interface ILocalMusicContract {

    /**
     * view Interface
     */
    interface ILocalMusicFgView{

        /**
         * item点击监听
         *
         * @param adapter 适配器
         * @param view VIEW
         * @param position 位置
         */
        void itemClickListener(BaseQuickAdapter adapter, View view, int position);
    }

    /**
     * presenter Interface
     */
    interface ILocalMusicPresenter{

        /**
         * 获取适配器
         *
         * @return 获取适配器
         */
       LocalMusicAdapter getAdapter();

        /**
         * 获取布局管理器
         *
         * @param context 上下文
         * @return 管理器
         */
        LinearLayoutManager getLayoutManager(Context context);

        /**
         * 设置Item点击事件
         *
         * @param recyclerView Rv
         */
        void setItemClickListener(RecyclerView recyclerView);
    }
}
