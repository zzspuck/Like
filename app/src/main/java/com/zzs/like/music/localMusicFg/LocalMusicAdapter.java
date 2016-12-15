package com.zzs.like.music.localMusicFg;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zzs.like.R;
import db.MusicInfoBean;

import java.util.List;

/**
 * 本地音乐适配器
 *
 * @author zzs
 * @date 2016.11.15
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class LocalMusicAdapter extends BaseQuickAdapter<MusicInfoBean, BaseViewHolder> {

    /**
     * 构造方法
     *
     * @param layoutResId 布局id
     * @param data 数据
     */
    public LocalMusicAdapter(int layoutResId, List<MusicInfoBean> data) {
        super(layoutResId, data);
    }

    /**
     * 构造方法
     *
     * @param data 数据
     */
    public LocalMusicAdapter(List<MusicInfoBean> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicInfoBean musicInfoBean) {
        // 设置音乐名
        helper.setText(R.id.item_tv_title, musicInfoBean.getTitle());
        // 艺人
        helper.setText(R.id.item_tv_artist, musicInfoBean.getArtist());
        // 图片
        Glide.with(mContext).load(musicInfoBean.getCoverUri()).crossFade().into((ImageView) helper.getView(R.id.item_iv_cover));
        // 设置点击监听
        helper.addOnClickListener(R.id.item_music_layout);
    }
}
