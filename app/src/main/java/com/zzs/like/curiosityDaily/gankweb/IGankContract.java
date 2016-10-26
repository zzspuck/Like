package com.zzs.like.curiosityDaily.gankweb;

import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 接口
 *
 * @author zzs
 * @date 2016.09.28
 * @note @note This specifies the contract between the view and the presenter
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public interface IGankContract {
    /**
     * view接口
     */
    interface IGankWebView {
        /**
         * web加载状态
         *
         * @param status （0：加载中 1：加载完成）
         */
        void webLoadStatus(int status);

        /**
         * 设置加载进度
         *
         * @param newProgress 设置进度
         */
        void setLoadProgress(int newProgress);

    }

    /**
     * presenter接口
     */
    interface IGankPresenter {
        /**
         * 设置webview
         *
         * @param url 加载的url
         * @param webView webview
         */
        void setWebView(String url, WebView  webView);
    }
}
