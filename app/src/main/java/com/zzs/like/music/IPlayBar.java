package com.zzs.like.music;

import com.zzs.like.data.music.MusicInfoBean;

/**
 * 设置播放按钮
 *
 * @author zzs
 * @date 2016.11.16
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public interface IPlayBar {
    /**
     * 设置播放按钮
     *
     * @param musicInfoBean 音乐信息
     */
    void setPlayBar(MusicInfoBean musicInfoBean);
}
