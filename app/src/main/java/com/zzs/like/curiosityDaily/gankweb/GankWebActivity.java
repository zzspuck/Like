package com.zzs.like.curiosityDaily.gankweb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zzs.like.R;
import com.zzs.like.base.MVPBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * GankWebActivity
 *
 * @author zzs
 * @date 2016.09.27
 */
public class GankWebActivity extends MVPBaseActivity<IGankContract.IGankWebView,GankWebPresenter> implements IGankContract.IGankWebView {
    // 获取web地址的url
    public static final String GANK_URL = "mGankUrl";
    @Bind(R.id.pb_progress)
    ProgressBar mPbProgress;
    @Bind(R.id.url_web)
    WebView mUrlWeb;
    // 地址
    private String mGankUrl;

    @Override
    protected GankWebPresenter createPresenter() {

        return new GankWebPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_gank_web;
    }

    @Override
    protected int provideSwipeRefershViewId() {
        // 不需要下拉刷新
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        parseIntent();
        mPresenter.setWebView(mGankUrl, mUrlWeb);
    }

    /**
     * 跳转函数
     *
     * @param context 上下文
     * @param url url
     * @return intent
     */
    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, GankWebActivity.class);
        intent.putExtra(GankWebActivity.GANK_URL, url);
        return intent;
    }

    /**
     * 得到Intent传递的数据
     */
    private void parseIntent() {
        mGankUrl = getIntent().getStringExtra(GANK_URL);
    }

    @Override
    public boolean canBack() {

        return true;
    }

    @Override
    public void webLoadStatus(int status) {
        switch (status) {
            case 0:
                // 网页开始加载
                mPbProgress.setVisibility(View.VISIBLE);
            case 1:
                // 网易加载完成
                mPbProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void setLoadProgress(int newProgress) {
        // 加载的进度
        mPbProgress.setProgress(newProgress);
    }
}
