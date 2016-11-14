package com.zzs.like.music.localMusicFg;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zzs.like.R;
import com.zzs.like.base.MVPBaseFragment;

import butterknife.Bind;

/**
 * 本地音乐fragment，位于主界面的viewpager中
 *
 * @author zzs
 * @date 2016.11.14
 */
public class LocalMusicFragment extends MVPBaseFragment<ILocalMusicContract.ILocalMusicFgView, LocalMusicFgPresenter> {
    @Bind(R.id.rv_local_music)
    ListView mListLocalMusic;
    @Bind(R.id.tv_empty)
    TextView mTvEmpty;

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
}
