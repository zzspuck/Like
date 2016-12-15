package com.zzs.like.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Shared Preference 数据类
 * 
 * @author mos
 * @date   2015.05.11
 * @note   使用方法：
 *         1.使用全局配置：
 *             // 仅在Application中初始化一次即可
 *             SharedPrefData.init(getApplicationContext, "GlobalPrefName"); 
 *             
 *             String useString = SharedPrefData.getString(key, defValue); 
 *             SharedPrefData.putString(key, value);
 *             
 *         2.使用自定义配置: 
 *             SharedPrefData spd = SharedPrefData.getPref(Context, "MyPref");
 *             String useString = spd.getStringEx(key, defValue); 
 *             spd.putStringEx(key, value);
 */
public class SharedPrefData {   
    // 上下文
    private static Context mGlobalContext;
    // 全局Shared Preference文件名
    private static String mGlobalSPName;
    // 局部文件名
    private String mSharedPrefName;
    // 局部上下文
    private Context mContext;
    
    /**
     * 构造函数
     * 
     * @param context           上下文
     * @param sharedPrefName    SP名
     */
    public SharedPrefData(Context context, String sharedPrefName) {
        mContext = context;
        mSharedPrefName = sharedPrefName;
    }
    
    /**
     * 初始化
     * 
     * @param appContext    应用上下文
     * @param globalSPName  全局SP名
     */
    public static void init(Context appContext, String globalSPName) {
        if (mGlobalContext == null && mGlobalSPName == null) {
            mGlobalContext = appContext;
            mGlobalSPName = globalSPName;
        }
    }
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public static int getInt(String key, int defValue) {
        
        return getSP(mGlobalContext, mGlobalSPName).getInt(key, defValue);      
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public static void putInt(String key, int value) {
        Editor editor = getEditor(mGlobalContext, mGlobalSPName);
        
        editor.putInt(key, value);
        
        editor.commit();
    }
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public static float getFloat(String key, float defValue) {
        
        return getSP(mGlobalContext, mGlobalSPName).getFloat(key, defValue);        
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public static void putFloat(String key, float value) {
        Editor editor = getEditor(mGlobalContext, mGlobalSPName);
        
        editor.putFloat(key, value);
        
        editor.commit();
    }   
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public static long getLong(String key, long defValue) {
        
        return getSP(mGlobalContext, mGlobalSPName).getLong(key, defValue);
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public static void putLong(String key, long value) {
        Editor editor = getEditor(mGlobalContext, mGlobalSPName);
        
        editor.putLong(key, value);
        
        editor.commit();
    }   
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public static boolean getBoolean(String key, boolean defValue) {
        
        return getSP(mGlobalContext, mGlobalSPName).getBoolean(key, defValue);
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public static void putBoolean(String key, boolean value) {
        Editor editor = getEditor(mGlobalContext, mGlobalSPName);
        
        editor.putBoolean(key, value);
        
        editor.commit();
    }   
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public static String getString(String key, String defValue) {
        
        return getSP(mGlobalContext, mGlobalSPName).getString(key, defValue);
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public static void putString(String key, String value) {
        Editor editor = getEditor(mGlobalContext, mGlobalSPName);
        
        editor.putString(key, value);
        
        editor.commit();
    }   
    
    /**
     * 是否包含key
     * 
     * @param key  键   
     * @return true -- 包含  false -- 不包含
     */
    public static boolean hasKey(String key) {
        
        return getSP(mGlobalContext, mGlobalSPName).contains(key);
    }    
    
    /**
     * 获取Shared Preference
     * 
     * @param context           上下文
     * @param sharedPrefName    SP名
     * @return SP对象
     */
    private static SharedPreferences getSP(Context context, String sharedPrefName) {
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
    }
    
    /**
     * 获取Editor对象
     * 
     * @param context           上下文
     * @param sharedPrefName    SP名
     * @return Editor对象
     */
    private static Editor getEditor(Context context, String sharedPrefName) {
        
        return getSP(context, sharedPrefName).edit();
    }
    
    /**
     * 获取SharedPrefData对象
     * 
     * @param context           上下文
     * @param sharedPrefName    SP名
     * @return SharedPrefData对象
     */
    public static SharedPrefData getPref(Context context, String sharedPrefName) {
        
        return new SharedPrefData(context, sharedPrefName);
    }
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public int getIntEx(String key, int defValue) {
        
        return getSP(mContext, mSharedPrefName).getInt(key, defValue);        
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public void putIntEx(String key, int value) {
        Editor editor = getEditor(mContext, mSharedPrefName);
        
        editor.putInt(key, value);
        
        editor.commit();
    }
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public float getFloatEx(String key, float defValue) {
        
        return getSP(mContext, mSharedPrefName).getFloat(key, defValue);      
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public void putFloatEx(String key, float value) {
        Editor editor = getEditor(mContext, mSharedPrefName);
        
        editor.putFloat(key, value);
        
        editor.commit();
    }   
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public long getLongEx(String key, long defValue) {
        
        return getSP(mContext, mSharedPrefName).getLong(key, defValue);
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public void putLongEx(String key, long value) {
        Editor editor = getEditor(mContext, mSharedPrefName);
        
        editor.putLong(key, value);
        
        editor.commit();
    }   
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public boolean getBooleanEx(String key, boolean defValue) {
        
        return getSP(mContext, mSharedPrefName).getBoolean(key, defValue);
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public void putBooleanEx(String key, boolean value) {
        Editor editor = getEditor(mContext, mSharedPrefName);
        
        editor.putBoolean(key, value);
        
        editor.commit();
    }   
    
    /**
     * 获取值
     * 
     * @param key       键
     * @param defValue  默认值
     * @return 值
     */
    public String getStringEx(String key, String defValue) {
        
        return getSP(mContext, mSharedPrefName).getString(key, defValue);
    }
    
    /**
     * 设置值
     * 
     * @param key   键
     * @param value 值
     */
    public void putStringEx(String key, String value) {
        Editor editor = getEditor(mContext, mSharedPrefName);
        
        editor.putString(key, value);
        
        editor.commit();
    }
    
    /**
     * 是否包含key
     * 
     * @param key  键   
     * @return true -- 包含  false -- 不包含
     */
    public boolean hasKeyEx(String key) {
        
        return getSP(mContext, mSharedPrefName).contains(key);
    }    
}
