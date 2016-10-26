package com.zzs.like.curiosityDaily.dailyFeed;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zzs.like.R;
import com.zzs.like.base.BasePresenter;
import com.zzs.like.data.daily.DailyTimeLine;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * DailyFeedPresenter
 *
 * @author zzs
 * @date 2016.09.28
 */
class DailyFeedPresenter extends BasePresenter<IDailyFeedContract.IDailyFeedView> implements IDailyFeedContract.IDailyFeedPresenter{
    // 上下文
    private Context mContext;
    // mDailyFeedView
    private IDailyFeedContract.IDailyFeedView mDailyFeedView;
    //dt
    private DailyTimeLine mTimeLine;
    // 适配器
    private DailyFeedAdapter mAdapter;
    // 最后可见选项
    private int mLastVisibleItem;
    // 更多
    private String mHasMore;
    // 下一页
    private String mNextPager;
    // id
    private String mDid;
    // 是否加载过更多
    private boolean mIsLoadMore = false;

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public DailyFeedPresenter(Context context) {
        this.mContext = context;
    }

    /**
     * 获取日报feed详情
     *
     * @param id  id
     * @param num 页数
     */
    @Override
    public void getDailyFeedDetail(String id, String num) {
        mDid = id;
        mDailyFeedView = getView();
        if (mDailyFeedView != null) {
            dailyApi.getDailyFeedDetail(id, num)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DailyTimeLine>() {
                        @Override
                        public void call(DailyTimeLine dailyTimeLine) {
                            mDailyFeedView.getDataSuc(dailyTimeLine);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            loadError(throwable);
                            mDailyFeedView.getDataFaild(throwable);
                        }
                    });
        }
    }

    /**
     * 加载出错
     *
     * @param throwable throwable
     */
    @Override
    public void loadError(Throwable throwable) {
        throwable.printStackTrace();
        mDailyFeedView.setDataRefresh(false);
        Toast.makeText(mContext, R.string.load_error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 展示日报
     *
     * @param context       上下文
     * @param dailyTimeLine daily
     * @param recyclerView  rv
     */
    @Override
    public void disPlayDailyTimeLine(Context context, DailyTimeLine dailyTimeLine, RecyclerView recyclerView) {
        if (dailyTimeLine.getResponse().getLast_key() != null) {
            mNextPager = dailyTimeLine.getResponse().getLast_key();
            mHasMore = dailyTimeLine.getResponse().getHas_more();
        }
        if (mIsLoadMore) {
            if (dailyTimeLine.getResponse().getOptions() == null) {
                mDailyFeedView.setDataRefresh(false);
                return;
            } else {
                mTimeLine.getResponse().getOptions().addAll(dailyTimeLine.getResponse().getOptions());
            }
            mAdapter.notifyDataSetChanged();
        } else {
            mTimeLine = dailyTimeLine;
            mAdapter = new DailyFeedAdapter(context, mTimeLine.getResponse().getOptions());
            recyclerView.setAdapter(mAdapter);
        }
        mDailyFeedView.setDataRefresh(false);
    }

    /**
     * recyclerView Scroll listener , maybe in here is wrong ?
     */
    @Override
    public void scrollRecycleView(RecyclerView recyclerView, final GridLayoutManager gridLayoutManager) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastVisibleItem = gridLayoutManager
                            .findLastVisibleItemPosition();
                    if (gridLayoutManager.getItemCount() == 1) {
                        return;
                    }
                    if (mLastVisibleItem + 1 == gridLayoutManager
                            .getItemCount()) {
                        if (mHasMore.equals("true")) {
                            mIsLoadMore = true;
                            mDailyFeedView.setDataRefresh(true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getDailyFeedDetail(mDid, mNextPager);
                                }
                            }, 1000);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
