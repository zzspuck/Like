package com.zzs.like.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zzs.like.base.MVPBaseActivity;
import com.zzs.like.constants.AppPreferences;
import com.zzs.like.data.music.MusicInfoBean;
import com.zzs.like.receiver.PhoneComingReceiver;
import com.zzs.like.util.MusicScanUntils;
import com.zzs.like.util.SysLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐播放本地服务，未使用aidl远程服务形式
 *
 * @author zzs
 * @date 2016.11.04
 */
public class MusicPlayService extends Service implements
        MediaPlayer.OnPreparedListener
        , MediaPlayer.OnCompletionListener
        , MediaPlayer.OnBufferingUpdateListener
        , AudioManager.OnAudioFocusChangeListener {

    // TAG
    private static final String TAG = MusicPlayService.class.getSimpleName();
    // 播放器
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    // 音乐信息列表
    private static List<MusicInfoBean> mMusicList = new ArrayList<MusicInfoBean>();
    // activity容器
    private static final List<MVPBaseActivity> mActivityStack = new ArrayList<>();
    //private String path = "http://ws.stream.qqmusic.qq.com/104779440.m4a?fromtag=46";
    private static MusicInfoBean mPlayingMusic = new MusicInfoBean();
    // 音乐播放的位置
    private static int mPlayingMusicPosition;
    // 监听耳机插拔广播
    private PhoneComingReceiver mNoisyReceiver = new PhoneComingReceiver();
    // 音频管理器
    private AudioManager mAudioManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Mybinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SysLog.i(TAG, "Service onCreate");
        if (mediaPlayer.isPlaying()) {
            stopPlayer();
        }
        mediaPlayer.setOnCompletionListener(this);
        updateMusicList();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mNoisyReceiver, mNoisyFilter);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        SysLog.i(TAG, "mediaPlayer onPrepared");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
    }

    /**
     * 一首歌曲播放完毕了，该播放下一首
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null || intent.getAction() == null) {
            return START_NOT_STICKY;
        }
        SysLog.i(TAG, "service onStartCommand");
        switch (intent.getAction()) {
            case AppPreferences.ACTION_MEDIA_PLAY_PAUSE:
                playPause();
                break;
            case AppPreferences.ACTION_MEDIA_NEXT:
                next();
                break;
            case AppPreferences.ACTION_MEDIA_PREVIOUS:
                preMusic();
                break;
            case AppPreferences.ACTION_MEDIA_PAUSE:
                pause();
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

        MusicScanUntils.scanMusic(this, getMusicList());

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
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        unregisterReceiver(mNoisyReceiver);
    }

    /**
     * 监听音频焦点
     *
     * @param focusChange
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        Log.i("iii", "onAudioFocusChange");
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    pause();
                }
                break;
        }
    }

    public class Mybinder extends Binder {
        public MusicPlayService getservice() {
            return MusicPlayService.this;
        }
    }

    /**
     *
     */
    public void playPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            pause();
        } else if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            resume();
        }
    }

    public int pause() {
        if (!mediaPlayer.isPlaying()) {
            return -1;
        }
        mediaPlayer.pause();
        mAudioManager.abandonAudioFocus(this);
        Log.i("iii", "mediaPlayer.pause();");
        return 0;

    }

    public int resume() {
        if (mediaPlayer.isPlaying()) {
            return -1;
        }
        mediaPlayer.start();
        Log.i("iii", "mediaPlayer.start();");
        return 0;
    }


    public void play(MusicInfoBean music) {
        /**
         * 若重复点击该歌曲 不重复播放
         */
        if (mPlayingMusic != null && mPlayingMusic.getUri() != null
                && mPlayingMusic.getUri().equals(music.getUri())) {
            Log.w("iii", "music.getUri().equals(music.getUri()");
            if (!mediaPlayer.isPlaying()) {
                resume();
            }
            return;
        }
        mPlayingMusic = music;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mPlayingMusic.getUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(int pos) {
        if (getMusicList() == null) {         //
            return;
        }
        /**
         * 若重复点击该歌曲 不重复播放
         * 同时先做下标判断，下标溢出置零
         */
        if (pos < 0) {
            pos += getMusicList().size();
        }

        if (pos >= getMusicList().size()) {
            pos %= getMusicList().size();
        }
        if (mPlayingMusicPosition == pos) {
            if (!mediaPlayer.isPlaying()) {
                resume();
            }
            return;
        }
        mPlayingMusicPosition = pos;
        mPlayingMusic = getMusicList().get(pos);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getMusicList().get(pos).getUri());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initPlayer() {
        if (mediaPlayer.isPlaying()) {
            stopPlayer();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);

        // playUrl(path);
        Log.i("iii", "service initPlayer");
    }


    /**
     * @param url url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
            mediaPlayer.setOnPreparedListener(this);//注册一个监听器
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int next() {
        play(mPlayingMusicPosition + 1);
        return mPlayingMusicPosition;
    }

    public int preMusic() {
        play(mPlayingMusicPosition - 1);
        return mPlayingMusicPosition;
    }


    public void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public MusicInfoBean getPlayingMusic() {
        return mPlayingMusic;
    }

    public static int getPlayingMusicPosition() {
        return mPlayingMusicPosition;
    }

    public static boolean getPlayingState() {
        return mediaPlayer.isPlaying();
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


}
