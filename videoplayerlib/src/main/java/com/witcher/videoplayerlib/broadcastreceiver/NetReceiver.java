package com.witcher.videoplayerlib.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.witcher.videoplayerlib.Util.Util;
import com.witcher.videoplayerlib.event.NetStateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by witcher on 2016/4/15.
 */
public class NetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            EventBus.getDefault().post(new NetStateEvent(Util.getNetType(context)));
        }
    }

}