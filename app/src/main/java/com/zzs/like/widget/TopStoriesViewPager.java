package com.zzs.like.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zzs.like.data.daily.TopStories;
import com.zzs.like.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 头部滚动Viewpager
 *
 * @author zzs
 * @date 2016.09.27
 */
public class TopStoriesViewPager extends RelativeLayout {
	// 上下文
	private Context mContext;
	// viewpager
	private ViewPager mViewPager;
	// 点击监听
	private ViewPagerClickListenner mListenner;
	// ImageViewpager当前页面的index
	private int mCurrentItem = 0;
	private int oldItem = 0;
	// 滚动图片集合
	private List<ImageView> mImages;
	// 执行周期性或定时任务
	private ScheduledExecutorService mScheduledExecutorService;
	// handler
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mViewPager.setCurrentItem(mCurrentItem);
		}
	};

	/**
	 * 构造方法
	 *
	 * @param context 上下文
     */
	public TopStoriesViewPager(Context context) {
		super(context);
		this.mContext = context;
		setView();
	}

	/**
	 * 构造方法
	 *
	 * @param context 上下文
	 * @param attrs 属性
     */
	public TopStoriesViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		setView();
	}

	/**
	 * 设置view
	 */
	private void setView() {
		mViewPager = new ViewPager(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mViewPager.setLayoutParams(params);

		LinearLayout dotLayout = new LinearLayout(mContext);
		LayoutParams dotParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		dotParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		dotParams.setMargins(0, 0, 0, ScreenUtil.instance(mContext).dip2px(10));
		dotLayout.setLayoutParams(dotParams);
		dotLayout.setGravity(Gravity.CENTER_HORIZONTAL);

		this.addView(mViewPager);
		this.addView(dotLayout);

	}

	/**
	 * 初始化
	 *
	 * @param items 条目
	 * @param tv 标题
	 * @param clickListenner 监听
     */
	public void init(final List<TopStories> items, final TextView tv,
					 ViewPagerClickListenner clickListenner) {
		this.mListenner = clickListenner;
		mImages = new ArrayList<>();
		List<View> dotList = new ArrayList<>();

		for (int i = 0; i < items.size(); i++) {
			final TopStories item = items.get(i);
			final ImageView mImageView = new ImageView(
					mContext);
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			mImageView.setLayoutParams(layoutParams);
			mImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (null != mListenner) {
						mListenner.onClick(item);
					}
				}
			});

			// 得到屏幕的宽度
			ScreenUtil screenUtil = ScreenUtil.instance(mContext);
			int width = screenUtil.getScreenWidth();

			Glide.with(mContext).load(item.getImage())
					.centerCrop()
					.into(mImageView);
			mImages.add(mImageView);
		}

		mViewPager.setAdapter(new MyPagerAdapter(mImages));
		tv.setText(items.get(0).getTitle());
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				tv.setText(items.get(position).getTitle());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	/**
	 * 开启定时任务
	 */
	public void startAutoRun(){
		mScheduledExecutorService = Executors
				.newSingleThreadScheduledExecutor();
		/**循环
		 * 创建并执行一个在给定初始延迟后首次启用的定期操作， 后续操作具有给定的周期；也就是将在 initialDelay 后开始执行，
		 * 然后在initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行， 依此类推
		 */
		mScheduledExecutorService.scheduleAtFixedRate(new ViewPagerTask(), 5,
				5, TimeUnit.SECONDS);
	}

	/**
	 * 关闭定时任务
	 */
	public void stopAutoRun(){
		if (mScheduledExecutorService!=null) {
			mScheduledExecutorService.shutdown();
		}
	}

	/**
	 * 发消息改变页数
	 */
	class ViewPagerTask implements Runnable {

		@Override
		public void run() {
			if (mImages != null) {
				mCurrentItem = (mCurrentItem + 1) % mImages.size();
				mHandler.obtainMessage().sendToTarget();
			}
		}
	}

	/**
	 * 获取资源id
	 *
	 * @param resourceName 资源名
	 * @return 资源id
     */
	public int getResourceId(String resourceName) {
		int resId = mContext.getResources().getIdentifier(resourceName,
				"drawable", mContext.getPackageName());
		return resId;
	}

	/**
	 * viewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		// view集合
		private List<? extends View> views;

		/**
		 * 构造方法
		 *
		 * @param views view集合
         */
		MyPagerAdapter(List<? extends View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			// return Integer.MAX_VALUE;
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			if (views.size() > 0
					&& views.get(position % views.size()).getParent() != null) {
				((ViewPager) views.get(position % views.size()).getParent())
						.removeView(views.get(position % views.size()));
			}
			try {
				((ViewPager) container).addView(
						views.get(position % views.size()), 0);
			} catch (Exception e) {
			}
			return views.get(position % views.size());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position
					% views.size()));
		}

	}

	/**
	 * 点击事件监听器接口
	 */
	public interface ViewPagerClickListenner {
		/**
		 * item点击事件监听
		 */
		void onClick(TopStories item);
	}
	
}
