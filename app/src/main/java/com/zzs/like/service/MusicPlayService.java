package com.zzs.like.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zzs.like.base.MVPBaseActivity;
import com.zzs.like.constants.AppPreferences;
import db.MusicInfoBean;
import com.zzs.like.receiver.PhoneComingReceiver;
import com.zzs.like.util.MusicPlayer;
import com.zzs.like.util.MusicScanUntils;
import com.zzs.like.util.SharedPrefData;
import com.zzs.like.util.SysLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐播放本地服务，未使用aidl远程服务形式
 *
 * @author zzs
 * @date 2016.11.04
 */
public class MusicPlayService extends Service implements MusicPlayer.PlayListener{
    // TAG
    private static final String TAG = MusicPlayService.class.getSimpleName();
    // 音乐信息列表
    private static List<MusicInfoBean> mMusicList = new ArrayList<>();
    // activity容器
    private static final List<MVPBaseActivity> mActivityStack = new ArrayList<>();
    //private String path = "http://ws.stream.qqmusic.qq.com/104779440.m4a?fromtag=46";
    private static MusicInfoBean mPlayingMusic = new MusicInfoBean();
    // 音乐播放的位置
    private static int mPlayingMusicPosition;
    // 监听耳机插拔广播
    private PhoneComingReceiver mNoisyReceiver = new PhoneComingReceiver();
    // 音乐播放器
    private MusicPlayer mMusicPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Mybinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SysLog.i(TAG, "Service onCreate");
        updateMusicList();
        mMusicPlayer = MusicPlayer.getInstance(this);
        mMusicPlayer.setAudioFousListener();
        mMusicPlayer.addPlayListener(this);
        IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mNoisyReceiver, mNoisyFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }
        SysLog.i(TAG, "service onStartCommand");
        switch (intent.getAction()) {
            case AppPreferences.ACTION_MEDIA_PLAY_PAUSE:
                mMusicPlayer.processTogglePlaybackRequest(mPlayingMusic.getUri());
                break;
            case AppPreferences.ACTION_MEDIA_NEXT:
                playNext();
                break;
            case AppPreferences.ACTION_MEDIA_PREVIOUS:
                preMusic();
                break;
            case AppPreferences.ACTION_MEDIA_PAUSE:
                mMusicPlayer.processPauseRequest();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取音乐列表
     *
     * @return 音乐列表
     */
    public static List<MusicInfoBean> getMusicList() {

        return mMusicList;
    }

    /**
     * 每次启动时扫描音乐
     */
    public void updateMusicList() {

        // 获取是否已经扫描过本地音乐
        boolean scanMusic = SharedPrefData.getBoolean(AppPreferences.DP_SCAN_LOCAL_MUSIC, false);
        if (!scanMusic) {
            // 没有扫描过则扫描
            List<MusicInfoBean> musicInfoList = MusicScanUntils.scanMusic(this);
            if (musicInfoList != null) {
                // 不为空则保存音乐数据到数据库，并设置扫描音乐为true
                SharedPrefData.putBoolean(AppPreferences.DP_SCAN_LOCAL_MUSIC, true);
                for (MusicInfoBean musicInfoBean : musicInfoList) {
                    boolean save = musicInfoBean.save();
                    if (save) {
                        SysLog.i(TAG, "音乐信息保存成功");
                    } else {
                        SysLog.e(TAG, "音乐信息保存失败");
                    }
                }
            }
        }

        if (getMusicList().isEmpty()) {
            SysLog.w(TAG, "getMusicList().isEmpty()");

            return;
        }
        for (MusicInfoBean k : mMusicList) {
            SysLog.i(TAG, k.getTitle() + k.getCoverUri());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMusicPlayer != null) {
            mMusicPlayer.onDestroy();
        }
        unregisterReceiver(mNoisyReceiver);
    }

    @Override
    public void onPlayed(Object playId, String uri) {
    }

    @Override
    public void onPaused(Object playId, String uri) {
    }

    @Override
    public void onStopped(Object playId, String uri) {

    }

    @Override
    public void onError(Object playId, String error) {

    }

    @Override
    public void onCompletion(Object playId, String uri) {
        // 播放完成则，播放下一首音乐
        playNext();
    }

    @Override
    public void onPrepared(Object playId, String uri) {
    }


    /**
     * Mybinder类
     */
    public class Mybinder extends Binder {
        /**
         * 获取音乐播放服务
         *
         * @return 服务
         */
        public MusicPlayService getservice() {
            return MusicPlayService.this;
        }
    }

    /**
     * 播放下一首音乐
     *
     * @return 当前音乐的播放位置
     */
    public int playNext() {
        playMusic(mPlayingMusicPosition + 1);

        return mPlayingMusicPosition;
    }

    /**
     * 播放上一首
     *
     * @return 当前音乐的播放位置
     */
    public int preMusic() {
        playMusic(mPlayingMusicPosition - 1);

        return mPlayingMusicPosition;
    }

    /**
     * 获取当前播放音乐的信息
     *
     * @return 音乐信息
     */
    public MusicInfoBean getPlayingMusic() {
        if (mMusicPlayer == null) {
            mPlayingMusic = mMusicList.get(0);
        }
        return mPlayingMusic;
    }

    /**
     * 获取播放音乐的位置
     *
     * @return 当前音乐的播放位置
     */
    public static int getPlayingMusicPosition() {

        return mPlayingMusicPosition;
    }

    /**
     * 播放音乐
     *
     * @param position 播放位置
     */
    private void playMusic(int position) {
        if (mMusicList == null) return;

        if (position < 0) {
            position = mMusicList.size();
        } else if (position > mMusicList.size()) {
            position = 0;
        }

        mPlayingMusicPosition = position;
        mPlayingMusic = mMusicList.get(position);
        mMusicPlayer.processPlayRequest(mPlayingMusic.getId(), mPlayingMusic.getUri());
    }

    /**
     * 添加activity进栈
     *
     * @param activity activity
     */
    public static void addToStack(MVPBaseActivity activity) {
        mActivityStack.add(activity);
    }

    /**
     * 从栈中移除Activity
     *
     * @param activity activity
     */
    public static void removeFromStack(MVPBaseActivity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 获取Activity栈
     *
     * @return Activity栈数据
     */
    public static List<MVPBaseActivity> getActivityStack() {

        return mActivityStack;
    }

    /**
     * 获取音乐播放器
     *
     * @return 音乐播放器
     */
    public MusicPlayer getMusicPlayer() {

        return mMusicPlayer;
    }
}
