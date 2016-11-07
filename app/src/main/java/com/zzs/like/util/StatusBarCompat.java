package com.zzs.like.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

public class StatusBarCompat {
    // 无用值
    private static final int INVALID_VAL = -1;
    // 默认颜色
    private static final int COLOR_DEFAULT = Color.parseColor("#303F9F");

    /**
     * 兼容状态栏
     *
     * @param activity Activity
     * @param statusColor 状态栏颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
            // 设置Activity layout的fitsSystemWindows
            View contentChild = contentView.getChildAt(0);
            contentChild.setFitsSystemWindows(true);
        }

    }

    /**
     * 兼容
     *
     * @param activity Activity
     */
    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }


    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 高度
     */
    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}