package com.zzs.like.util;

import android.util.Log;

/**
 * 日志工具封装类
 * 
 * @author zzs
 * @date   2016.09.01
 * @note   使用LEVEL配置日志级别，大于等于配置级别的日志均被输出
 */
public class SysLog {
    // VERBOSE等级
    private static final int     LV_VERBOSE = 1;
    
    // DEBUG等级
    private static final int     LV_DEBUG   = 2;
    
    // INFO等级
    private static final int     LV_INFO    = 3;
    
    // WARN等级
    private static final int     LV_WARN    = 4;
    
    // ERROR等级
    private static final int     LV_ERROR   = 5;
    
    // 不输出    
    @SuppressWarnings("all")
    private static final int     LV_NONE    = 6;    
    
    // 调试TAG前缀
    private static final String  ATTACH_TAG = "SH-";
    
    // 调试等级
    private static int           LEVEL      = LV_VERBOSE;    
    
    /**
     * VERBOSE日志
     * 
     * @param TAG   tag
     * @param log   输出的信息
     */   
    public static void v(String TAG, String log) {
        if (LEVEL <=  LV_VERBOSE) {
            Log.v(ATTACH_TAG + TAG, log);
        }       
    }    
    
    /**
     * DEBUG日志
     * 
     * @param TAG   tag
     * @param log   输出的信息
     */
    public static void d(String TAG, String log) {
        if (LEVEL <=  LV_DEBUG) {
            Log.d(ATTACH_TAG + TAG, log);
        }       
    }
    
    /**
     * INFO日志
     * 
     * @param TAG   tag
     * @param log   输出的信息
     */ 
    public static void i(String TAG, String log) {
        if (LEVEL <=  LV_INFO) {
            Log.i(ATTACH_TAG + TAG, log);
        }
    }   
    
    /**
     * WARN日志
     * 
     * @param TAG   tag
     * @param log   输出的信息
     */ 
    public static void w(String TAG, String log) {
        if (LEVEL <=  LV_WARN) {
            Log.w(ATTACH_TAG + TAG, log);
        }
    }      
    
    /**
     * ERROR日志
     * 
     * @param TAG   tag
     * @param log   输出的信息
     */ 
    public static void e(String TAG, String log) {
        if (LEVEL <=  LV_ERROR) {
            Log.e(ATTACH_TAG + TAG, log);
        }
    } 
}
