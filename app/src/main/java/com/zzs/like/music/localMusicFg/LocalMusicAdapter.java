package com.zzs.like.music.localMusicFg;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 类描述           // TODO
 *
 * @author -       // TODO
 * @date -         // TODO
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class LocalMusicAdapter extends BaseQuickAdapter {
    public LocalMusicAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    public LocalMusicAdapter(List data) {
        super(data);
    }

    public LocalMusicAdapter(View contentView, List data) {
        super(contentView, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Object o) {

    }
}
