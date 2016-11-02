package com.zzs.like.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 改变状态栏颜色工具类
 *
 * @author zzs
 * @date 2016.10.31
 */

public class ChangeStatusBarUtil {
    // 状态栏
    private static View mStatusBarView;

    /**
     * 改变状态栏的颜色
     *
     * @param activity Activity
     * @param color 颜色
     */
    public static void changedStatusBarColor(Activity activity, int color) {
        final int sdk = Build.VERSION.SDK_INT;
        Window window = activity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        if (sdk == Build.VERSION_CODES.KITKAT) {
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            // 设置透明状态栏
            if ((params.flags & bits) == 0) {
                params.flags |= bits;
                window.setAttributes(params);
            }

            // 设置状态栏颜色
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            setupStatusBarView(activity, contentLayout, color);

            // 设置Activity layout的fitsSystemWindows
            View contentChild = contentLayout.getChildAt(0);
            contentChild.setFitsSystemWindows(true);
        }

    }

    /**
     * 设置状态栏的颜色
     *
     * @param contentLayout layout
     * @param color 颜色
     */
    private static void setupStatusBarView(Context context, ViewGroup contentLayout, int color) {
        if (mStatusBarView == null) {
            View statusBarView = new View(context);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(context));
            contentLayout.addView(statusBarView, lp);

            mStatusBarView = statusBarView;
        }

        mStatusBarView.setBackgroundColor(color);
    }

    /**
     * 获取状态栏的高度
     *
     * @param context 上下文
     * @return 高度
     */
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
