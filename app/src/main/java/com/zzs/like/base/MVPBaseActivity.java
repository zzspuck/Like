package com.zzs.like.base;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;

import com.zzs.like.R;
import com.zzs.like.util.ChangeStatusBarUtil;
import com.zzs.like.util.StatusBarCompat;

import butterknife.ButterKnife;

/**
 * Base of Activity
 *
 * @author zzs
 * @date 2016..9.18
 */
public abstract class MVPBaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {
    // presenter
    protected T mPresenter;
    // appBar
    protected AppBarLayout mAppBar;
    // toolbar
    protected Toolbar mToolbar;
    // 刷新控件
    private SwipeRefreshLayout mRefreshLayout;
    // 是否需要刷新
    private boolean mIsRequestDataRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //允许为空，不是所有都要实现MVP模式
        if (createPresenter() != null) {
            mPresenter = createPresenter();
            mPresenter.attachView((V) this);
        }
        //布局
        setContentView(provideContentViewId());
        ButterKnife.bind(this);
        mAppBar = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null && mAppBar != null) {
            //把Toolbar当做ActionBar给设置
            setSupportActionBar(mToolbar);
            if (canBack()) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null)
                    //设置ActionBar一个返回箭头，主界面没有，次级界面有
                    actionBar.setDisplayHomeAsUpEnabled(true);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                mAppBar.setElevation(10.6f);//Z轴浮动
            }
        }

        if (isSetRefresh()) {
            setupSwipeRefresh();
        }

        // 改变状态栏的颜色
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 设置下拉刷新
     */
    private void setupSwipeRefresh() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(provideSwipeRefershViewId());
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1,
                    R.color.refresh_progress_2, R.color.refresh_progress_3);
            mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                }
            });
        }
    }

    /**
     * 申请刷新
     */
    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    /**
     * 设置刷新
     *
     * @param requestDataRefresh 是否刷新（true：刷新，false：不刷新）
     */
    public void setRefresh(boolean requestDataRefresh) {
        if (mRefreshLayout == null) {
            return;
        }
        if (!requestDataRefresh) {
            mIsRequestDataRefresh = false;
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mRefreshLayout != null) {
                        mRefreshLayout.setRefreshing(false);
                    }
                }
            }, 1000);
        } else {
            mRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 此时android.R.id.home即为返回箭头
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 判断当前 Activity 是否允许返回
     * 主界面不允许返回，次级界面允许返回
     *
     * @return false
     */
    public boolean canBack() {
        return false;
    }

    /**
     * 判断子Activity是否需要刷新功能
     *
     * @return false
     */
    public Boolean isSetRefresh() {
        if (provideSwipeRefershViewId() == 0) {
            return false;
        }
        return true;
    }

    protected abstract T createPresenter();

    //用于引入布局文件
    abstract protected int provideContentViewId();

    // 引入下拉加载控件id
    abstract protected int provideSwipeRefershViewId();

}
