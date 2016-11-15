package com.zzs.like.music.localMusicFg;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zzs.like.R;
import com.zzs.like.base.BasePresenter;
import com.zzs.like.service.MusicPlayService;

/**
 * 本地音乐界面Presenter
 *
 * @author zzs
 * @date 2016.11.14
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class LocalMusicFgPresenter extends BasePresenter<ILocalMusicContract.ILocalMusicFgView> implements ILocalMusicContract.ILocalMusicPresenter {

    @Override
    public LocalMusicAdapter getAdapter() {
        LocalMusicAdapter adapter = new LocalMusicAdapter(R.layout.item_rv_music, MusicPlayService.getMusicList());
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        return adapter;
    }

    @Override
    public LinearLayoutManager getLayoutManager(Context context) {

        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void setItemClickListener(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                ILocalMusicContract.ILocalMusicFgView fgView = getView();
                if (fgView != null) {
                    fgView.itemClickListener(baseQuickAdapter, view, position);
                }
            }
        });

    }
}
