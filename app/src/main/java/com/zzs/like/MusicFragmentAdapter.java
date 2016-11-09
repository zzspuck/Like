package com.zzs.like;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐界面滑动适配器
 *
 * @author zzs
 * @date 2016.11.09
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */

public class MusicFragmentAdapter extends FragmentPagerAdapter {

    // 数据源
    private List<Fragment> mData = new ArrayList<>();
    /**
     * 构造方法
     *
     * @param fm Fragment管理器
     */
    public MusicFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return mData.get(position);
    }

    @Override
    public int getCount() {

        return mData.size();
    }

    /**
     * 添加数据
     *
     * @param fragment 数据
     */
    public void addData(Fragment fragment){
     mData.add(fragment);
    }
}
