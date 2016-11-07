package com.zzs.like.music;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    @Bind(R.id.rb_local_music)
    RadioButton mLocalMusRb;
    @Bind(R.id.rb_online_music)
    RadioButton mOnlineMusRb;
    @Bind(R.id.rg_layout)
    RadioGroup mGroup;


    /**
     * 构造方法
     */
    public MusicEntranceFragment() {
    }


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mToolbar.setNavigationIcon(R.mipmap.ic_favorite_white_24dp);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_local_music:
                        // 本地音乐
                        mLocalMusRb.setChecked(true);
                        break;
                    case R.id.rb_online_music:
                        // 在线音乐
                        mOnlineMusRb.setChecked(true);
                        break;
                }
            }
        });
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
        }
    }
}
