package com.witcher.videoplayerlib.event;

/**
 * Created by witcher on 2017/3/1 0001.
 */

public class BatteryStateEvent {
    /**
     * 0正在充电,1低电量,2正常
     */
    public int batteryState;

    public BatteryStateEvent(int batteryState) {
        this.batteryState = batteryState;
    }
}
