package com.zzs.like.base;


import com.zzs.like.api.ApiFactory;
import com.zzs.like.api.DailyApi;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Base of Presenter
 *
 * @author zzs
 * @date 2016.09.21
 */
public abstract class BasePresenter<V> {

    // 弱引用
    private Reference<V> mViewRef;
    protected static final DailyApi dailyApi = ApiFactory.getDailyApiSingleton();

    void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    protected V getView() {
        return mViewRef.get();
    }

    protected boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}