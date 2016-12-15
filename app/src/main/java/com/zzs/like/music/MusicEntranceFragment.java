package com.zzs.like.music;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zzs.like.R;
import com.zzs.like.base.MVPBaseFragment;
import db.MusicInfoBean;
import com.zzs.like.music.OnlineMusicFg.OnlineMusicFragment;
import com.zzs.like.music.localMusicFg.LocalMusicFgPresenter;
import com.zzs.like.music.localMusicFg.LocalMusicFragment;
import com.zzs.like.service.MusicPlayService;
import com.zzs.like.util.SysLog;
import com.zzs.like.widget.PlayerBar;

import butterknife.Bind;

/**
 * 音乐界面入口Fragment
 *
 * @author zzs
 * @date 2016.11.01
 * @note 这个Fragment不需要实现MVP模式
 */
public class MusicEntranceFragment extends MVPBaseFragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener, IPlayBar, PlayerBar.ShowPlayingFragmentListener {
    // TAG
    private static final String TAG = MusicEntranceFragment.class.getSimpleName();
    @Bind(R.id.ft_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rb_local_music)
    RadioButton mLocalMusRb;
    @Bind(R.id.rb_online_music)
    RadioButton mOnlineMusRb;
    @Bind(R.id.rg_layout)
    RadioGroup mGroup;
    @Bind(R.id.fem_viewpager)
    ViewPager mViewpager;
    // 音乐服务
    private MusicPlayService mServicebinder;
    // 播放bar
    private PlayerBar mPlayBar;
    // 适配器
    private MusicFragmentAdapter mAdapter;


    /**
     * 构造方法
     */
    public MusicEntranceFragment() {
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mPlayBar = (PlayerBar) rootView.findViewById(R.id.fl_play_bar);
        mPlayBar.setShowPlayingFragmentListener(this);
        mToolbar.setNavigationIcon(R.mipmap.ic_favorite_white_24dp);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_local_music:
                        // 本地音乐
                        mViewpager.setCurrentItem(0);
                        break;
                    case R.id.rb_online_music:
                        // 在线音乐
                        mViewpager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null)
        mAdapter = new MusicFragmentAdapter(getActivity().getSupportFragmentManager());

        mAdapter.addData(new LocalMusicFragment());
        mAdapter.addData(OnlineMusicFragment.newInstance("", ""));
        mViewpager.setAdapter(mAdapter);
        mViewpager.addOnPageChangeListener(this);

        // 绑定音乐服务
        bindService();
    }

    /**
     * 获取音乐服务
     *
     * @return 服务
     */
    public MusicPlayService getMusicService() {

        return mServicebinder;
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MusicPlayService.class);
        getActivity().bindService(intent, connet, Context.BIND_AUTO_CREATE);
    }

    /**
     * 连接服务
     */
    private ServiceConnection connet = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SysLog.i(TAG, "onServiceConnected");
            mServicebinder = ((MusicPlayService.Mybinder) iBinder).getservice();
        }

        //当启动源和service连接意外丢失时会调用
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            SysLog.i(TAG, "onServiceDisconnected");
        }
    };

    /**
     * MusicEntranceFragment 创建
     *
     * @return MusicEntranceFragment
     */
    public static MusicEntranceFragment newInstance() {

        return new MusicEntranceFragment();
    }

    @Override
    protected LocalMusicFgPresenter createPresenter() {

        // 不实现MVP
        return null;
    }

    @Override
    protected int createViewLayoutId() {

        return R.layout.fragment_entrance_music;
    }

    @Override
    protected int provideSwipeRefershViewId() {
        // 不需要刷新
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mLocalMusRb.setChecked(true);
        } else if (position == 1) {
            mOnlineMusRb.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(connet);
    }

    @Override
    public void setPlayBar(MusicInfoBean musicInfoBean) {
        mPlayBar.setInfo(musicInfoBean);
    }

    @Override
    public void ShowPlayingFragment(MusicInfoBean mMusicInfoBean) {
        // TODO: 2016/11/16  
        //每次点击都得刷新fragment，而hide和show不走生命周期
       /* mPlayFragment = new PlayFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_up, 0)
                .replace(android.R.id.content, mPlayFragment)
                .show(mPlayFragment)
                .commit();
        mIsPlayingFragment = true;*/
    }
}
