package com.zzs.like.flashLight;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import com.zzs.like.R;
import com.zzs.like.base.MVPBaseFragment;
import com.zzs.like.data.flash.FlashLight;

/**
 * 手电Fragment
 *
 * @author zzs
 * @date 2016.09.19
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LightFragment extends MVPBaseFragment<ILightContract.ILightFgView, LightFgPresenter> implements ILightContract.ILightFgView {
    // 图片
    private ImageView mImage;
    // fab
    private FloatingActionButton mFloatBtn;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mImage = (ImageView) rootView.findViewById(R.id.fl_img);
        mFloatBtn = (FloatingActionButton) rootView.findViewById(R.id.fl_fab_switch);

        setFabClick();
    }

    @Override
    protected int provideSwipeRefershViewId() {
        // 不需要下拉刷新则传入0
        return 0;
    }

    /**
     * 设置点击监听
     */
    private void setFabClick() {
        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.toggleLight();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 已经入就打开手电
        mPresenter.openLight();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化手电
        mPresenter.initLight();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 关闭手电
        mPresenter.releaseLight();
    }

    @Override
    protected LightFgPresenter createPresenter() {
        return new LightFgPresenter();
    }

    protected int createViewLayoutId() {
        return R.layout.fragment_light;
    }

    @Override
    public void flashStatus(int status) {
        // 获取手电状态
        switch (status) {
            case FlashLight.FLASH_INIT:
                // 当flash_init时 mImage还没有初始化
                //mImage.setImageResource(R.drawable.img_cq_light_one);
                break;
            case FlashLight.FLASH_REALSE:
                mImage.setImageResource(R.drawable.img_cq_light_two);
                break;
            case FlashLight.FLASH_OPEN:
                mImage.setImageResource(R.drawable.img_cq_night_three);
                break;
            case FlashLight.FLASH_CLOSE:
                mImage.setImageResource(R.drawable.img_cq_night_four);
                break;
        }
    }
}
