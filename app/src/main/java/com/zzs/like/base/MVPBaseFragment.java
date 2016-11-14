package com.zzs.like.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzs.like.R;

import butterknife.ButterKnife;

/**
 * Base of Fragment
 *
 * @author zzs
 * @date 2016.09.18
 */
public abstract class MVPBaseFragment<V, T extends BasePresenter<V>> extends Fragment {
    // prsenter
    protected T mPresenter;
    // 是否刷新
    private boolean mIsRequestDataRefresh = false;
    // 刷新控件
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 允许为空，不是所有都要实现MVP模式
        if (createPresenter() != null) {
            mPresenter = createPresenter();
            mPresenter.attachView((V)this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(createViewLayoutId(),container,false);
        ButterKnife.bind(this,rootView);
        initView(rootView);
        if(isSetRefresh()) {
            setupSwipeRefresh(rootView);
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
        mPresenter.detachView();
    }

    /**
     * 设置下拉刷新
     *
     * @param view view
     */
    private void setupSwipeRefresh(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(provideSwipeRefershViewId());
        if(mRefreshLayout != null){
            mRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1,
                    R.color.refresh_progress_2,R.color.refresh_progress_3);
            mRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,getResources().getDisplayMetrics()));
            mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    requestDataRefresh();
                }
            });
        }
    }

    /**
     * 申请数据刷新
     */
    public void requestDataRefresh() {
        mIsRequestDataRefresh = true;
    }

    /**
     * 设置刷新
     *
     * @param requestDataRefresh 申请刷新
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

    protected abstract T createPresenter();

    protected abstract int createViewLayoutId();

    protected  void initView(View rootView){}
    // 引入下拉加载控件id
    abstract protected int provideSwipeRefershViewId();

    /**
     * 是否设置刷新
     *
     * @return true：设置 false：不设置（默认设置）
     */
    public Boolean isSetRefresh(){
        if (provideSwipeRefershViewId()== 0) {
            return false;
        }

        return true;
    }

}

