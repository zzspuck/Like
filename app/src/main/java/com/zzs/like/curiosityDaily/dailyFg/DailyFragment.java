package com.zzs.like.curiosityDaily.dailyFg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zzs.like.R;
import com.zzs.like.base.MVPBaseFragment;
import com.zzs.like.data.daily.DailyTimeLine;

import butterknife.Bind;

/**
 * DailyFragment
 *
 * @author zzs
 * @date 2016.09.27
 */
public class DailyFragment extends MVPBaseFragment<IDailyFgContract.IDailyFgView, DailyFgPresenter> implements IDailyFgContract.IDailyFgView {
    // 线性布局管理器
    private LinearLayoutManager mLayoutManager;
    @Bind(R.id.content_list)
    RecyclerView mContentList;

    @Override
    protected DailyFgPresenter createPresenter() {
        return new DailyFgPresenter(getContext());
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initView(View rootView) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mContentList.setLayoutManager(mLayoutManager);
    }

    @Override
    protected int provideSwipeRefershViewId() {
        // 下拉刷新
        return R.id.swipe_refresh;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataRefresh(true);
        mPresenter.getDailyTimeLine("0");
        mPresenter.scrollRecycleView(mContentList,mLayoutManager);
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        setDataRefresh(true);
        mPresenter.getDailyTimeLine("0");
    }

    @Override
    public void getDataSuc(DailyTimeLine dailyTimeLine) {
        // 获取数据成功
        mPresenter.disPlayDailyTimeLine(getActivity(),dailyTimeLine,mContentList);
    }

    @Override
    public void getDataFaild(Throwable throwable) {
        // 获取数据失败
        mPresenter.loadError(throwable);
    }

    @Override
    public void setDataRefresh(boolean refresh) {
        setRefresh(refresh);
    }
}
