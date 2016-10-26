package com.zzs.like.flashLight;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zzs.like.R;
import com.zzs.like.base.BasePresenter;
import com.zzs.like.data.flash.FlashLight;

/**
 * 手电presenter
 *
 * @author zzs
 * @date 2016.09.19
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LightFgPresenter extends BasePresenter<ILightContract.ILightFgView> implements ILightContract.ILightPresenter {
    // TAG
    private static final String TAG = LightFgPresenter.class.getSimpleName();
    // flash
    private FlashLight mFlashLight = new FlashLight();
    // lightfgView
    private ILightContract.ILightFgView mLightFgView;

    @Override
    public void releaseLight() {
        // 释放相机资源
        if (mFlashLight != null && mLightFgView != null) {
            mFlashLight.release();
            mLightFgView.flashStatus(mFlashLight.getFlashStatus());
        }
    }

    @Override
    public void initLight() {
        // 初始化相机资源
        if (isViewAttached() && null != getView() && mFlashLight != null) {
            mFlashLight.init();
            mLightFgView = getView();
            mLightFgView.flashStatus(mFlashLight.getFlashStatus());
        } else {
            Log.e(TAG, "View 没有绑定或者View为空");
        }
    }

    @Override
    public void toggleLight() {
        if (mFlashLight != null && mLightFgView != null) {
            int flashStatus = mFlashLight.getFlashStatus();

            if (flashStatus == FlashLight.FLASH_CLOSE || flashStatus == FlashLight.FLASH_INIT) {
                // 打开手电
                mFlashLight.open();
                mLightFgView.flashStatus(flashStatus);
            } else if (flashStatus == FlashLight.FLASH_OPEN) {
                // 关闭手电
                mFlashLight.close();
                mLightFgView.flashStatus(flashStatus);
            }
        }
    }

    @Override
    public void openLight() {
        int flashStatus = mFlashLight.getFlashStatus();
        if (mFlashLight != null && (flashStatus == FlashLight.FLASH_INIT ||
                flashStatus == FlashLight.FLASH_CLOSE) && mLightFgView != null) {

            mFlashLight.open();
            mLightFgView.flashStatus(flashStatus);

        }
    }
}