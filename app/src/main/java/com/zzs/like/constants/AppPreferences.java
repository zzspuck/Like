package com.zzs.like.constants;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Environment;

/**
 * 偏好定义类
 * 
 * @author mos
 * @date   2015.06.02
 */
public class AppPreferences {
    /**
     * 静态配置(static preference)的放这里
     */       
    // 慧居入口设备(所有入口设备，全局推送时使用)
    public static final String SP_SH_ENTRANCE_DEV = "D_SH_ENTRANCE_DEV";
	public static final String ACTION_MEDIA_PLAY_PAUSE = "music.ACTION_MEDIA_PLAY_PAUSE";
	public static final String ACTION_MEDIA_NEXT = "music.ACTION_MEDIA_NEXT";
	// 暂停声音ACTION
	public static final String ACTION_MEDIA_PAUSE= "music.ACTION_MEDIA_PAUSE";
	public static final String ACTION_MEDIA_PREVIOUS = "music.ACTION_MEDIA_PREVIOUS";
	public static final String VOLUME_CHANGED_ACTION = "media.VOLUME_CHANGED_ACTION";

	
	/**
	 * 动态配置(dynamic preference)的放这里
	 * 
	 * @note 动态配置都放在shared preference中
	 */
	
	// 凌晨3点重启偏移时间
	public static final String DP_REBOOT_AT_3_OFFSET = "rebootAt3Offset";

}
