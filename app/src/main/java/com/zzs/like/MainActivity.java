package com.zzs.like;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.zzs.like.base.BasePresenter;
import com.zzs.like.base.MVPBaseActivity;
import com.zzs.like.curiosityDaily.dailyFg.DailyFragment;
import com.zzs.like.flashLight.LightFragment;
import com.zzs.like.music.MusicEntranceFragment;
import com.zzs.like.util.ChangeStatusBarUtil;

public class MainActivity extends MVPBaseActivity implements BottomNavigationBar.OnTabSelectedListener {
    // 底部导航
    private BottomNavigationBar mBottomBar;
    // 底部最后选择的位置
    private int mLastSelectedPosition = 0;
    // badge
    private BadgeItem mNumberBadgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化控件
        initBottomNavigation();
        // 设置点击监听
        mBottomBar.setTabSelectedListener(this);
        // 初始化为手电界面
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new LightFragment())
                .commitAllowingStateLoss();
    }

    @Override
    protected BasePresenter createPresenter() {
        // 主activity不用mvp则返回空
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int provideSwipeRefershViewId() {
        // 没有下拉刷新所以传入0
        return 0;
    }

    /**
     * 初始化底部导航
     */
    private void initBottomNavigation() {

        mBottomBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomBar.clearAll();
        // bottomNavigationBar.setFab(fabHome, BottomNavigationBar.FAB_BEHAVIOUR_TRANSLATE_AND_STICK);
        // mBottomBar.setFab(mFloatBtn);

        mNumberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.blue)
                .setText("" + mLastSelectedPosition)
                .setHideOnSelect(false);
        // 给底部设置模式
        mBottomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        // 设置底部背景样式
        mBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        // 设置底部按钮个数及图片
        mBottomBar.addItem(new BottomNavigationItem(R.mipmap.ic_home_white_24dp, "light").setActiveColorResource(R.color.orange).setBadgeItem(mNumberBadgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.ic_music_note_white_24dp, "news").setActiveColorResource(R.color.teal))
                .addItem(new BottomNavigationItem(R.mipmap.ic_favorite_white_24dp, "favority").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.ic_light_on_24dp, "music").setActiveColorResource(R.color.brown))
                .addItem(new BottomNavigationItem(R.mipmap.ic_ligth_off_24dp, "Games").setActiveColorResource(R.color.grey))
                .setFirstSelectedPosition(mLastSelectedPosition)
                .initialise();
    }

    @Override
    public void onTabSelected(int position) {
        mLastSelectedPosition = position;
        if (mNumberBadgeItem != null) {
            mNumberBadgeItem.setText(Integer.toString(position));
        }
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new LightFragment();
                break;
            case 1:
                fragment = new DailyFragment();
                break;
            case 2:
                fragment = MusicEntranceFragment.newInstance();
                break;
            case 3:
                fragment = new LightFragment();
                break;
            case 4:
                fragment = new LightFragment();
                break;
            default:
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}

