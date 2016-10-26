package com.zzs.like.curiosityDaily.dailyFg;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zzs.like.R;
import com.zzs.like.base.BasePresenter;
import com.zzs.like.data.daily.DailyTimeLine;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 日报Presenter
 *
 * @author zzs
 * @date 2016.09.26
 */
public class DailyFgPresenter extends BasePresenter<IDailyFgContract.IDailyFgView> implements IDailyFgContract.IDailyFgPresenter {
    // 上下文
    private Context mContext;
    // view
    private IDailyFgContract.IDailyFgView mDailyFgView;
    // 日报
    private DailyTimeLine mTimeLine;
    // 日报适配器
    private DailyListAdapter mAdapter;
    private int mLastVisibleItem;
    // 更多
    private String mHasMore;
    // 下一页
    private String mNextPager;
    // 是否加载过更多
    private boolean mIsLoadMore = false;

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public DailyFgPresenter(Context context) {
        this.mContext = context;
    }

    /**
     * 获取日报信息
     *
     * @param num 页数
     */
    @Override
    public void getDailyTimeLine(String num) {
        mDailyFgView = getView();
        if (mDailyFgView != null) {
            Subscription option = dailyApi.getDailyTimeLine(num)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DailyTimeLine>() {
                        @Override
                        public void call(DailyTimeLine dailyTimeLine) {
                            if (dailyTimeLine.getMeta().getMsg().equals("success")) {
                                mDailyFgView.getDataSuc(dailyTimeLine);
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            //loadError(throwable);
                            mDailyFgView.getDataFaild(throwable);
                        }
                    });
        }
    }

    /**
     * 加载出错
     *
     * @param throwable 异常
     */
    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        mDailyFgView.setDataRefresh(false);
        Toast.makeText(mContext, R.string.load_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 展示日报
     *
     * @param context       上下文
     * @param dailyTimeLine 日报
     * @param recyclerView  re
     */
    @Override
    public void disPlayDailyTimeLine(Context context, DailyTimeLine dailyTimeLine, RecyclerView recyclerView) {
        if (dailyTimeLine.getResponse().getLast_key() != null) {
            mNextPager = dailyTimeLine.getResponse().getLast_key();
        }
        mHasMore = dailyTimeLine.getResponse().getHas_more();
        if (mIsLoadMore) {
            if (dailyTimeLine.getResponse().getFeeds() == null) {
                mAdapter.updateLoadStatus(DailyListAdapter.LOAD_NONE);
                mDailyFgView.setDataRefresh(false);
                return;
            } else {
                mTimeLine.getResponse().getFeeds().addAll(dailyTimeLine.getResponse().getFeeds());
            }
            mAdapter.notifyDataSetChanged();
        } else {
            mTimeLine = dailyTimeLine;
            mAdapter = new DailyListAdapter(context, mTimeLine.getResponse());
            recyclerView.setAdapter(mAdapter);
        }
        mDailyFgView.setDataRefresh(false);
    }

    /**
     * recyclerView Scroll listener , maybe in here is wrong ?
     */
    @Override
    public void scrollRecycleView(RecyclerView recyclerView, final LinearLayoutManager linearLayoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (linearLayoutManager.getItemCount() == 1) {
                        mAdapter.updateLoadStatus(DailyListAdapter.LOAD_NONE);
                        return;
                    }
                    if (mLastVisibleItem + 1 == linearLayoutManager
                            .getItemCount()) {
                        mAdapter.updateLoadStatus(DailyListAdapter.LOAD_PULL_TO);
                        if (mHasMore.equals("true")) {
                            mIsLoadMore = true;
                        }
                        mAdapter.updateLoadStatus(DailyListAdapter.LOAD_MORE);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getDailyTimeLine(mNextPager);
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
