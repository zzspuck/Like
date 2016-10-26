package com.zzs.like.flashLight;

/**
 *  接口
 *
 * @author zzs
 * @date 2016.09.21
 * @note This specifies the contract between the view and the presenter
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public interface ILightContract {

    /**
     * view interface
     */
    interface ILightFgView {
        /**
         * 手电状态
         *
         * @param status 状态值
         */
        void flashStatus(int status);
    }

    /**
     * presenter interface
     */
    interface ILightPresenter {
        /**
         * 释放手电
         */
        void releaseLight();

        /**
         * 初始化手电
         */
        void initLight();

        /**
         * 设置电筒打开状态监听
         */
        void toggleLight();

        /**
         * 打开手电
         */
        void openLight();

    }

}
