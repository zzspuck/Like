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
	// Shared Preference文件名
	public static final String PREF_FILE_NAME = AppPreferences.class.getSimpleName();

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
	
	// 扫描本地音乐
	public static final String DP_SCAN_LOCAL_MUSIC = "scanLocalMusic";

}
