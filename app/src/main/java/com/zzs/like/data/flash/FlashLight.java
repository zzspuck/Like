package com.zzs.like.data.flash;

import android.hardware.Camera;

/**
 * 手电类
 *
 * @author zzs
 * @date 2016.09.21
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class FlashLight {
    // 0：资源释放
    public static final int FLASH_REALSE = 0;
    // 1：手电初始化
    public static final int FLASH_INIT = 1;
    // 2：手电打开
    public static final int FLASH_OPEN = 2;
    // 3：手电关闭
    public static final int FLASH_CLOSE = 3;
    // camera
    private Camera camera = null;
    // cameraParameters
    private Camera.Parameters cameraParameters;
    // previousFlashMode
    private String previousFlashMode = null;
    // 手电标志位（0：资源释放 1：手电初始化 2：手电打开 3：手电关闭）
    private int mLightStatus = FLASH_REALSE;

    /**
     * 初始化
     */
    public synchronized void init() {
        camera = Camera.open();
        if (camera != null) {
            cameraParameters = camera.getParameters();
            previousFlashMode = cameraParameters.getFlashMode();
            mLightStatus = FLASH_INIT;
        }
        if (previousFlashMode == null) {
            // could be null if no flash, i.e. emulator
            previousFlashMode = Camera.Parameters.FLASH_MODE_OFF;
        }
    }

    /**
     * 释放
     */
    public synchronized void release() {
        if (camera != null) {
            cameraParameters.setFlashMode(previousFlashMode);
            camera.setParameters(cameraParameters);
            camera.release();
            camera = null;
            mLightStatus = FLASH_REALSE;
        }
    }

    /**
     * 打开
     */
    public synchronized void open() {
        if (camera != null) {
            cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(cameraParameters);
            camera.startPreview();
            mLightStatus = FLASH_OPEN;
        }
    }

    /**
     * 关闭
     */
    public synchronized void close() {
        if (camera != null) {
            cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(cameraParameters);
            camera.stopPreview();
            mLightStatus = FLASH_CLOSE;
        }
    }

    /**
     * 获取手电状态
     *
     * @return 手电标志位（0：资源释放 1：手电初始化 2：手电打开 3：手电关闭）
     */
    public int getFlashStatus() {

        return mLightStatus;
    }
}
