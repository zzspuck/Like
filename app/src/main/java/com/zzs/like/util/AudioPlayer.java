package com.zzs.like.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * mediaplayer封装类
 *
 * @author zzs
 * @date 2016.06.16
 */
public class AudioPlayer implements OnCompletionListener, OnPreparedListener, OnErrorListener, MusicFocusable {
    // 降低后的音量
    public static final float DUCK_VOLUME = 0.1f;
    // mediaplayer
    private MediaPlayer mPlayer = null;
    // 音频焦点帮助类
    private AudioFocusListener mAudioFocusListener = null;
    // player状态
    private State mState = State.Stopped;
    // 音频当前焦点
    private AudioFocus mAudioFocus = AudioFocus.NoFocusNoDuck;
    // 上下文
    private Context mContext = null;
    // 当前播放的url
    private String mCurrentPlayUri = null;
    // 当前播放对象
    private Object mCurrentPlayId = null;
    // player
    private static AudioPlayer mAudioPlayer = null;
    // player监听
    private List<PlayListener> mPlayListeners = new ArrayList<>();

    /**
     * 播放器的播放状态
     */
    private enum State {
        // 停止状态
        Stopped,
        // 播放器正在准备中
        Preparing,
        // 播放中（注意：在这个状态player也可能被暂停，如果没有获取音频焦点）
        Playing,
        // 暂停
        Paused;
    }

    /**
     * 音频焦点枚举
     */
    private enum AudioFocus {
        // 没有焦点，不能降低音量
        NoFocusNoDuck,
        // 没有焦点，但可以降低音量播放
        NoFocusCanDuck,
        // 有音频焦点
        Focused
    }

    /**
     * 获取player实例
     *
     * @param context 上下文
     * @return player
     */
    public static AudioPlayer getInstance(Context context) {
        if (mAudioPlayer == null) {
            mAudioPlayer = new AudioPlayer(context);
        }

        return mAudioPlayer;
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    private AudioPlayer(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 设置音频焦点监听
     */
    public void setAudioFousListener() {
        Log.i(getLogTag(), "onCreate()");
        Context context = getContext();
        mAudioFocusListener = new AudioFocusListener(context, this);
    }

    /**
     * 播放、暂停、切换请求
     */
    public void processTogglePlaybackRequest() {
        if (mState == State.Paused || mState == State.Stopped) {
            processPlayRequest(mCurrentPlayId, null);
        } else {
            processPauseRequest();
        }
    }

    /**
     * player播放请求
     *
     * @param playId 播放类
     * @param uri    播放地址
     */
    public void processPlayRequest(Object playId, String uri) {
        boolean playNewSong = mState == State.Stopped || mState == State.Playing
                || (mState == State.Paused && !TextUtils.isEmpty(mCurrentPlayUri) && !mCurrentPlayUri.equals(uri));
        boolean resumeFromPause = mState == State.Paused && !TextUtils.isEmpty(mCurrentPlayUri) && mCurrentPlayUri.equals(uri);

        if (playNewSong) {
            Log.i(getLogTag(), "Playing from URL/path: " + uri);
            tryToGetAudioFocus();
            loadFile(playId, uri);
        } else if (resumeFromPause) {
            tryToGetAudioFocus();
            mState = State.Playing;
            playSound();
        }
    }

    /**
     * player暂停请求
     */
    public void processPauseRequest() {
        if (mState == State.Playing) {
            // 暂停播放器，并将状态变为paused
            mState = State.Paused;
            mPlayer.pause();
            // 在暂停中，我们任然拥有player，不要放弃音频焦点
            relaxResources(false);
            for (PlayListener listener : mPlayListeners) {
                listener.onPaused(mCurrentPlayId, mCurrentPlayUri);
            }
        }
    }

    /**
     * player停止请求
     */
    public void processStopRequest() {
        processStopRequest(false);
    }

    /**
     * 停止请求
     *
     * @param force （true：强行停止播放，fale：不强制）
     */
    public void processStopRequest(boolean force) {
        if (mState == State.Playing || mState == State.Paused || force) {
            mState = State.Stopped;

            // 释放所有资源
            relaxResources(true);
            // 放弃音频焦点
            giveUpAudioFocus();
            for (PlayListener listener : mPlayListeners) {
                listener.onStopped(mCurrentPlayId, mCurrentPlayUri);
            }
            mCurrentPlayUri = null;
            mCurrentPlayId = null;
        }
    }

    /**
     * 添加player监听器
     *
     * @param listener
     */
    public void addPlayListener(PlayListener listener) {
        mPlayListeners.add(listener);
    }

    /**
     * 移除player监听
     *
     * @param listener
     */
    public void removePlayListener(PlayListener listener) {
        mPlayListeners.remove(listener);
    }

    /**
     * 获取当前播放audio的uri
     *
     * @return uri
     */
    public String getCurrentPlayUri() {
        return mCurrentPlayUri;
    }

    /**
     * 获取当前播放的audio
     *
     * @return audio
     */
    public Object getCurrentPlayId() {
        return mCurrentPlayId;
    }

    /**
     * 获取当前player的播放状态
     *
     * @return 状态值
     */
    public State getState() {
        return mState;
    }

    /**
     * audio是否在播放中
     *
     * @return true：在播放中，false：没有在播放中
     */
    public boolean isPlaying() {
        return mState == State.Playing;
    }

    /**
     * audio是否暂停
     *
     * @return true:暂停中，false：没有在暂停中
     */
    public boolean isPaused() {
        return mState == State.Paused;
    }

    /**
     * 释放资源
     *
     * @param releaseMediaPlayer true：释放　false:不释放
     */
    private void relaxResources(boolean releaseMediaPlayer) {
        if (releaseMediaPlayer && mPlayer != null) {
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**
     * 放弃音频焦点
     */
    private void giveUpAudioFocus() {
        if (mAudioFocus == AudioFocus.Focused && mAudioFocusListener != null
                && mAudioFocusListener.abandonFocus()) {
            mAudioFocus = AudioFocus.NoFocusNoDuck;
        }
    }

    /**
     * 确认并开始播放audio
     */
    private void playSound() {
        if (mAudioFocus == AudioFocus.NoFocusNoDuck) {
            // 如果没有音频焦点并且不允许减小音量，则需要暂停，即使我们的状态是playing
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                Log.w(getLogTag(), "configAndStartMediaPlayer()-> NoFocusNoDuck. pause");
                for (PlayListener listener : mPlayListeners) {
                    listener.onPaused(mCurrentPlayId, mCurrentPlayUri);
                }
            } else {
                // 没有音频焦点
                Log.e(getLogTag(), "configAndStartMediaPlayer()-> NoFocusNoDuck ");
                for (PlayListener listener : mPlayListeners) {
                    listener.onError(mCurrentPlayId, "NoFocusNoDuck");
                }
            }

            return;

        } else if (mAudioFocus == AudioFocus.NoFocusCanDuck) {
            // 没有焦点但是可以减小音量
            mPlayer.setVolume(DUCK_VOLUME, DUCK_VOLUME);
        } else {
            // 可以大声播放
            mPlayer.setVolume(1.0f, 1.0f);
        }

        if (!mPlayer.isPlaying()) {
            // 没有在播放中这播放音频
            Log.i(getLogTag(), "configAndStartMediaPlayer()-> start");
            mPlayer.start();
            for (PlayListener listener : mPlayListeners) {
                listener.onPlayed(mCurrentPlayId, mCurrentPlayUri);
            }
        } else {
            Log.i(getLogTag(), "configAndStartMediaPlayer()-> is playing");
        }
    }

    /**
     * 试着获取音频焦点
     */
    private void tryToGetAudioFocus() {
        if (mAudioFocus != AudioFocus.Focused && mAudioFocusListener != null
                && mAudioFocusListener.requestFocus()) {
            mAudioFocus = AudioFocus.Focused;
        }
    }

    /**
     * 加载文件
     *
     * @param playId    audio
     * @param manualUrl audio的uri
     */
    private void loadFile(Object playId, String manualUrl) {
        if (mState == State.Playing || mState == State.Paused) {
            for (PlayListener listener : mPlayListeners) {
                listener.onStopped(mCurrentPlayId, mCurrentPlayUri);
            }
            mCurrentPlayUri = null;
            mCurrentPlayId = null;
        }
        mState = State.Stopped;
        relaxResources(false);

        try {
            if (manualUrl != null) {
                createMediaPlayerIfNeeded();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(manualUrl);

                mCurrentPlayUri = manualUrl;
                mCurrentPlayId = playId;
            }
            else {
                Log.e(getLogTag(), "No available music to play. Place some music on your external storage \"\n" +
                        "\t\t\t\t\t\t\t\t\t+ \"device (e.g. your SD card) and try again.");
                processStopRequest(true); // stop everything!
                for (PlayListener listener : mPlayListeners) {
                    listener.onError(playId, "No available music to play");
                }
                return;
            }
            mState = State.Preparing;
            // 异步加载
            mPlayer.prepareAsync();
        } catch (IOException ex) {
            String msg = "IOException playing next song: " + ex.getMessage();
            Log.e(getLogTag(), msg);
            ex.printStackTrace();
            for (PlayListener listener : mPlayListeners) {
                listener.onError(mCurrentPlayId, msg);
            }
        }
    }

    /**
     * 如果需要创建player
     *
     * @note :避免在播放音频过程中cpu睡眠需要设置setWakeMode，
     * 记住在AndroidManifest中申明权限android.permission.WAKE_LOCK
     */
    private void createMediaPlayerIfNeeded() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();

            mPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK);

            // 注册监听
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnErrorListener(this);
        } else {
            mPlayer.reset();
        }
    }

    @Override
    public void onCompletion(MediaPlayer player) {
        Log.i(getLogTag(), "onCompletion");
        processStopRequest();
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        Log.i(getLogTag(), "onPrepared");
        // 开始播放
        mState = State.Playing;
        playSound();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        String msg = "Media player error! Resetting." + "Error: what=" + String.valueOf(what) + ", extra=" + String.valueOf(extra);
        Log.e(getLogTag(), msg);

        mState = State.Stopped;
        relaxResources(true);
        giveUpAudioFocus();

        for (PlayListener listener : mPlayListeners) {
            listener.onError(mCurrentPlayId, msg);
        }

        // 返回true：表明我们已经处理错误
        return true;
    }

    @Override
    public void onGainedAudioFocus() {
        Log.i(getLogTag(), "gained audio focus.");
        mAudioFocus = AudioFocus.Focused;

        // 获取音频焦点开始播放
        if (mState == State.Playing) {
            playSound();
        }
    }

    @Override
    public void onLostAudioFocus(boolean canDuck) {
        Log.e(getLogTag(), "lost audio focus." + (canDuck ? "can duck" : "no duck"));
        mAudioFocus = canDuck ? AudioFocus.NoFocusCanDuck : AudioFocus.NoFocusNoDuck;

        if (mPlayer != null && mPlayer.isPlaying())
            playSound();
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        processStopRequest(true);
    }

    /**
     * 获取TAG
     *
     * @return TAG
     */
    public String getLogTag() {

        return getClass().getSimpleName();
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    private Context getContext() {

        return mContext;
    }

    /**
     * 音频焦点监听
     */
    private static class AudioFocusListener implements AudioManager.OnAudioFocusChangeListener {
        // 音频manager
        AudioManager mAM;
        // 音乐焦点
        MusicFocusable mFocusable;

        public AudioFocusListener(Context ctx, MusicFocusable focusable) {
            mAM = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
            mFocusable = focusable;
        }

        /**
         * 获取焦点
         *
         * @return true:获取成功 false：失败
         */
        public boolean requestFocus() {

            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    mAM.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }

        /**
         * 放弃焦点
         *
         * @return true：放弃成功 false：失败
         */
        public boolean abandonFocus() {

            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAM.abandonAudioFocus(this);
        }

        /**
         * 音频焦点变化
         *
         * @param focusChange
         */
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (mFocusable == null)
                return;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    mFocusable.onGainedAudioFocus();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    mFocusable.onLostAudioFocus(false);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mFocusable.onLostAudioFocus(true);
                    break;
                default:
            }
        }
    }

    /**
     * 简单的播放监听
     */
    public static class SimplePlayListener implements PlayListener {
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
    }

    /**
     * 播放监听
     */
    public interface PlayListener {
        // 播放
        void onPlayed(Object playId, String uri);

        // 暂停
        void onPaused(Object playId, String uri);

        // 停止
        void onStopped(Object playId, String uri);

        // 出错
        void onError(Object playId, String error);
    }
}

/**
 * 音频焦点
 */
interface MusicFocusable {

    // 获取音频焦点
    void onGainedAudioFocus();

    /**
     * 失去音频焦点
     *
     * @param canDuck true:音频能够减少声音播放, false:音频停止播放
     */
    void onLostAudioFocus(boolean canDuck);
}
