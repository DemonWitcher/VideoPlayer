package com.witcher.videoplayerlib.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.witcher.videoplayerlib.Constant;
import com.witcher.videoplayerlib.event.BatteryStateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by witcher on 2017/3/1 0001.
 */

public class BatteryChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_CHANGED)) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    EventBus.getDefault().post(new BatteryStateEvent(Constant.BatteryState.CHARGEING));
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    EventBus.getDefault().post(new BatteryStateEvent(Constant.BatteryState.NORMAL));
                    break;
                default:
                    break;
            }
        } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_LOW)) {
            EventBus.getDefault().post(new BatteryStateEvent(Constant.BatteryState.LOW));
        } else if (action.equalsIgnoreCase(Intent.ACTION_BATTERY_OKAY)) {
            EventBus.getDefault().post(new BatteryStateEvent(Constant.BatteryState.NORMAL));
        }
    }
}
