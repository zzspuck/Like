package com.zzs.like.curiosityDaily.dailyFeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzs.like.R;
import com.zzs.like.base.MVPBaseActivity;
import com.zzs.like.data.daily.DailyTimeLine;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zzs.like.R.id.swipe_refresh;

/**
 * 展示 好奇心日报 type = feed 的 item 详细内容
 *
 * @author zzs
 * @date 2016.09.28
 */
public class DailyFeedActivity extends MVPBaseActivity<IDailyFeedContract.IDailyFeedView,DailyFeedPresenter> implements IDailyFeedContract.IDailyFeedView {
    // feed ID
    private static final String FEED_ID = "feed_id";
    // feed 描述
    private static final String FEED_DESC = "feed_desc";
    // feed 标题
    private static final String FEED_TITLE = "feed_title";
    // feed 图片
    private static final String FEED_IMG = "feed_img";
    // Id
    private String mId;
    // 描述
    private String mDesc;
    // 标题
    private String mTitle;
    // 图片
    private String mImg;

    @Bind(R.id.iv_feed_img)
    ImageView iv_feed_img;
    @Bind(R.id.tv_feed_title)
    TextView tv_feed_title;
    @Bind(R.id.tv_feed_desc)
    TextView tv_feed_desc;
    @Bind(R.id.feed_list)
    RecyclerView feed_list;
    // 网格布局管理器
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected DailyFeedPresenter createPresenter() {
        return new DailyFeedPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_daily_feed;
    }

    @Override
    protected int provideSwipeRefershViewId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        parseIntent();
        initView();
        mPresenter.getDailyFeedDetail(mId,"0");
        mPresenter.scrollRecycleView(feed_list, mGridLayoutManager);
    }

    /**
     * 跳转函数
     *
     * @param context 上下文
     * @param id feedId
     * @param desc feed描述
     * @param title feed标题
     * @param img feed图片
     * @return intent
     */
    public static Intent newIntent(Context context, String id, String desc, String title, String img) {
        Intent intent = new Intent(context, DailyFeedActivity.class);
        intent.putExtra(DailyFeedActivity.FEED_ID, id);
        intent.putExtra(DailyFeedActivity.FEED_DESC, desc);
        intent.putExtra(DailyFeedActivity.FEED_TITLE, title);
        intent.putExtra(DailyFeedActivity.FEED_IMG, img);
        return intent;
    }

    /**
     * 解析intent传的值
     */
    private void parseIntent() {
        mId = getIntent().getStringExtra(FEED_ID);
        mDesc = getIntent().getStringExtra(FEED_DESC);
        mTitle = getIntent().getStringExtra(FEED_TITLE);
        mImg = getIntent().getStringExtra(FEED_IMG);
    }

    /**
     * 初始化控件
     */
    private void initView(){
        tv_feed_title.setText(mTitle);
        tv_feed_desc.setText(mDesc);
        Glide.with(this).load(mImg).centerCrop().into(iv_feed_img);

        mGridLayoutManager = new GridLayoutManager(this,2);
        feed_list.setLayoutManager(mGridLayoutManager);
    }

    @Override
    public void requestDataRefresh() {
        super.requestDataRefresh();
        mPresenter.getDailyFeedDetail(mId,"0");
    }


    @Override
    public boolean canBack() {
        // 有返回键
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(iv_feed_img);
    }

    @Override
    public void getDataSuc(DailyTimeLine dailyTimeLine) {
        // 获取数据成功展示数据
        mPresenter.disPlayDailyTimeLine(DailyFeedActivity.this, dailyTimeLine, feed_list);
    }

    @Override
    public void getDataFaild(Throwable throwable) {
        // 获取失败
        mPresenter.loadError(throwable);
    }

    @Override
    public void setDataRefresh(boolean refresh) {
        setRefresh(refresh);
    }
}
