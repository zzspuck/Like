package com.zzs.like.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zzs.like.constants.AppPreferences;
import com.zzs.like.service.MusicPlayService;


/**
 * 监听耳机拔插事件
 *
 * @author zzs
 * @date 2016.11.04
 */
public class PhoneComingReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MusicPlayService.class);
        serviceIntent.setAction(AppPreferences.ACTION_MEDIA_PAUSE);
        context.startService(serviceIntent);
    }
}
