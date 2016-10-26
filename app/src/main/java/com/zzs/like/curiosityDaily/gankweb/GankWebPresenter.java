package com.zzs.like.curiosityDaily.gankweb;

import android.app.Activity;
import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zzs.like.base.BasePresenter;

/**
 * GankWebPresenter
 *
 * @author zzs
 * @date 2016.09.27
 */
public class GankWebPresenter extends BasePresenter<IGankContract.IGankWebView> implements IGankContract.IGankPresenter{
    // mActivity
    private Activity mActivity;

    /**
     * 构造函数
     *
     * @param activity activity
     */
    public GankWebPresenter(Activity activity) {
        mActivity = activity;
    }

    /**
     * 设置webView
     *
     * @param url url
     * @param webView web
     */
    @Override
    public void setWebView(String url, WebView webView){

        final IGankContract.IGankWebView urlView =  getView();

        WebSettings settings = webView.getSettings();
        // 支持JS
        settings.setJavaScriptEnabled(true);
        // 显示放大缩小按钮
        settings.setBuiltInZoomControls(true);
        // 支持双击放大缩小
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
                System.out.println("网页开始加载");
                // 网页开始加载
                urlView.webLoadStatus(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("网页加载结束");
                // 网页加载完成
                urlView.webLoadStatus(1);
            }

            /**
             * 所有跳转的链接都在此方法中回调
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                urlView.setLoadProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("网页title："+title);
                mActivity.setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });

        webView.loadUrl(url);
    }
}
