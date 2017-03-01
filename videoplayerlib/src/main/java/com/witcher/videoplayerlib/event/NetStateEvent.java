package com.witcher.videoplayerlib.event;

/**
 * Created by witcher on 2017/3/1 0001.
 */

public class NetStateEvent {
    /**
     * 没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     */
    public int netState;

    public NetStateEvent(int netState) {
        this.netState = netState;
    }
}
