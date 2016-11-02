package com.zzs.like.music;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.zzs.like.R;
import com.zzs.like.base.MVPBaseFragment;

import butterknife.Bind;

/**
 * 音乐界面入口Fragment
 *
 * @author zzs
 * @date 2016.11.01
 */
public class MusicEntranceFragment extends MVPBaseFragment<IMusicEntranceContract.IMusicEntranceFgView, MusicEntranceFgPresenter> implements View.OnClickListener {

    // TAG
    private static final String TAG = MusicEntranceFragment.class.getSimpleName();
    @Bind(R.id.ft_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ft_btn_one)
    Button mLocalMusBtn;
    @Bind(R.id.ft_btn_two)
    Button mOnlineMusBtn;

    /**
     * 构造方法
     */
    public MusicEntranceFragment() {
    }


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mLocalMusBtn.setOnClickListener(this);
        mOnlineMusBtn.setOnClickListener(this);
        mToolbar.setNavigationIcon(R.mipmap.ic_favorite_white_24dp);
        mToolbar.setTitle("音乐");
    }

    /**
     * MusicEntranceFragment 创建
     *
     * @return MusicEntranceFragment
     */
    public static MusicEntranceFragment newInstance() {

        return new MusicEntranceFragment();
    }

    @Override
    protected MusicEntranceFgPresenter createPresenter() {

        return new MusicEntranceFgPresenter();
    }

    @Override
    protected int createViewLayoutId() {

        return R.layout.fragment_entrance_music;
    }

    @Override
    protected int provideSwipeRefershViewId() {
        // 不需要刷新
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ft_btn_one:
                // 本地音乐
                // TODO: 2016/11/1
                break;
            case R.id.ft_btn_two:
                // 网络音乐
                // TODO: 2016/11/1
                break;
        }

    }
}
