package com.zzs.like.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzs.like.R;
import db.MusicInfoBean;
import com.zzs.like.service.MusicPlayService;
import com.zzs.like.util.MusicPlayer;
import com.zzs.like.util.SysLog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 播放控件
 *
 * @author zzs
 * @date 2016.11.09
 */
public class PlayerBar extends FrameLayout implements View.OnClickListener {
    // TAG
    private static final String TAG = PlayerBar.class.getSimpleName();
    @Bind(R.id.iv_play_bar_cover)
    ImageView mImageCover;
    @Bind(R.id.tv_play_bar_title)
    TextView mMusicTitle;
    @Bind(R.id.tv_play_bar_artist)
    TextView mMusicArtist;
    @Bind(R.id.iv_play_bar_play)
    ImageView mImagePlayButton;
    @Bind(R.id.iv_play_bar_next)
    ImageView mImagePlayNext;
    @Bind(R.id.pb_play_bar)
    ProgressBar mProgress;    //之前 绑定错id出错
    // 上下文
    private Context mContext;
    // 音乐信息
    private MusicInfoBean mMusicInfoBean;

    // 音乐服务
    private MusicPlayService musicPlayService;
    // 播放音乐监听
    private ShowPlayingFragmentListener mPlayingFragmentListener;

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param attrs 属性
     * @param defStyleAttr 默认属性
     */
    public PlayerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param attrs 属性
     */
    public PlayerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public PlayerBar(Context context) {
        super(context);
        init(context, null);
    }
    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs 属性
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mybindService();
    }

    /**
     * 连接服务
     */
    private ServiceConnection connet = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            SysLog.i(TAG, "onServiceConnected");
            musicPlayService = ((MusicPlayService.Mybinder) binder).getservice();
            //    servicebinder.initPlayer();
        }

        //当启动源和service连接意外丢失时会调用
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            SysLog.w(TAG, "onServiceDisconnected");
        }
    };

    /**
     * 绑定服务
     */
    private void mybindService() {
        Intent intent = new Intent();
        intent.setClass(mContext, MusicPlayService.class);
        mContext.bindService(intent, connet, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onFinishInflate() {
        ButterKnife.bind(this);
        super.onFinishInflate();

        mImagePlayButton.setClickable(true);
        mImagePlayNext.setClickable(true);
        mImageCover.setClickable(true);

        mImageCover.setOnClickListener(this);
        mImagePlayButton.setOnClickListener(this);
        mImagePlayNext.setOnClickListener(this);
        this.setClickable(true);
        this.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_play_bar_play: {
                SysLog.i(TAG, "iv_play_bar_play 点击事件 暂停或者播放");
                MusicPlayer musicPlayer = musicPlayService.getMusicPlayer();
                MusicInfoBean playingMusic = musicPlayService.getPlayingMusic();
                musicPlayer.processTogglePlaybackRequest(playingMusic.getUri());

                if (musicPlayer.getState() == MusicPlayer.State.Playing) {
                    mImagePlayButton.setImageResource(R.drawable.ic_play_bar_btn_pause);
                } else if (musicPlayer.getState() == MusicPlayer.State.Paused){
                    mImagePlayButton.setImageResource(R.drawable.ic_play_bar_btn_play);
                }

                break;
            }
            case R.id.iv_play_bar_next: {
                SysLog.i(TAG, "iv_play_bar_next 点击事件，下一首");
                musicPlayService.playNext();
                setInfo(MusicPlayService.getMusicList().get(MusicPlayService.getPlayingMusicPosition()));
                break;
            }

            default: {
                SysLog.i(TAG, "onclick 去播放界面");
                if (mPlayingFragmentListener != null) {
                    mPlayingFragmentListener.ShowPlayingFragment(mMusicInfoBean);
                } else {
                    SysLog.i(TAG, "mPlayingFragmentListener = null");
                }
            }

        }

    }

    /**
     * 设置信息
     *
     * @param musicInfoBean 音乐信息
     */
    public void setInfo(MusicInfoBean musicInfoBean) {
        mMusicInfoBean = musicInfoBean;
        Glide.with(mContext)
                .load(musicInfoBean.getCoverUri())
                .error(R.drawable.default_cover)
                .into(mImageCover);
        mMusicTitle.setText(musicInfoBean.getTitle());
        mMusicArtist.setText(musicInfoBean.getArtist());
        MusicPlayer musicPlayer = musicPlayService.getMusicPlayer();
        if (musicPlayer.getState() == MusicPlayer.State.Playing) {
            mImagePlayButton.setImageResource(R.drawable.ic_play_bar_btn_pause);
        } else if (musicPlayer.getState() == MusicPlayer.State.Paused){
            mImagePlayButton.setImageResource(R.drawable.ic_play_bar_btn_play);
        }
    }

    /**
     * 显示播放音乐Fragment监听接口
     */
    public interface ShowPlayingFragmentListener {
        /**
         * 显示音乐播放
         *
         * @param mMusicInfoBean 音乐信息
         */
        public void ShowPlayingFragment(MusicInfoBean mMusicInfoBean);
    }

    /**
     * 设置播放音乐界面监听
     *
     * @param listener 监听
     */
    public void setShowPlayingFragmentListener(ShowPlayingFragmentListener listener) {
        mPlayingFragmentListener = listener;
    }
}
