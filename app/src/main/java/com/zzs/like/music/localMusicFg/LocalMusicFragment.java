package com.zzs.like.music.localMusicFg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.zzs.like.R;
import com.zzs.like.base.MVPBaseFragment;

import butterknife.Bind;

/**
 * 本地音乐fragment，位于主界面的viewpager中
 *
 * @author zzs
 * @date 2016.11.14
 */
public class LocalMusicFragment extends MVPBaseFragment<ILocalMusicContract.ILocalMusicFgView, LocalMusicFgPresenter> implements ILocalMusicContract.ILocalMusicFgView{
    @Bind(R.id.rv_local_music)
    RecyclerView mRvLocalMusic;
    @Bind(R.id.tv_empty)
    TextView mTvEmpty;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalMusicAdapter adapter = mPresenter.getAdapter();
        LinearLayoutManager layoutManager = mPresenter.getLayoutManager(getActivity());
        mRvLocalMusic.setLayoutManager(layoutManager);
        mRvLocalMusic.setAdapter(adapter);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected LocalMusicFgPresenter createPresenter() {
        return new LocalMusicFgPresenter();
    }

    @Override
    protected int createViewLayoutId() {
        return R.layout.fragment_local_music;
    }

    @Override
    protected int provideSwipeRefershViewId() {
        // 暂时不需要下拉刷新
        return 0;
    }

    /**
     * item点击事件监听
     * 
     * @param adapter 适配器
     * @param view VIEW
     * @param position 位置
     */
    @Override
    public void itemClickListener(BaseQuickAdapter adapter, View view, int position) {
    }
}
