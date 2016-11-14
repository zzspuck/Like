package com.zzs.like.music.localMusicFg;

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
    }

    /**
     * presenter Interface
     */
    interface ILocalMusicPresenter{

        /**
         * 设置本地音乐适配器
         */
        void setLocalAdapter();
    }
}
