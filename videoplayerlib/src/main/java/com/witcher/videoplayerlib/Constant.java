package com.witcher.videoplayerlib;

/**
 * Created by witcher on 2017/2/27 0027.
 */

public class Constant {

    /**
     * 可以调整横竖屏,大小屏
     * 前后滑动调整进度,左侧上下滑动调整亮度,右侧上下滑动调整声音
     * 单击显示/隐藏菜单,双击暂停/开始,
     * 标题,时间,网络状态,电量状态,清晰度调整,当前时间/总时长,进度条,
     * 可以切换视频/直播,直播无进度条与进度调整
     */
    public static final String TAG = "witcher";
    public static final boolean DEBUG = true;
    /**
     * 正常操作的HUD隐藏时间
     */
    public static final int HUD_AUTO_HIDE = 5000;
    /**
     * 只做手势滑动进度时的seekBar自动隐藏时间
     */
    public static final int SBHUD_AUTO_HIDE = 2000;
    /**
     * 声音,亮度,中间文字的自动隐藏时间
     */
    public static final int VOLUME_HUD_AUTO_HIDE = 1000;
    /**
     * 视频初始化的亮度
     */
    public static final float INIT_BRIGHTNESS = 0.8f;
    /**
     * 滑动一个videoview的宽度所变化的秒数,为了手势滑动seekTo
     */
    public static final int WIDTH_SECOND = 90;

    public interface NetState{
        /**
         * 无网
         */
        int NO_NET = 0;
        /**
         * WIFI
         */
        int WIFI = 1;
        /**
         * 2G网络
         */
        int G2 = 2;
        /**
         * 3G网络
         */
        int G3 = 3;
        /**
         * 4G网络
         */
        int G4 = 4;
    }
    public interface BatteryState{
        /**
         * 充电中
         */
        int CHARGEING = 0;
        /**
         * 低电量
         */
        int LOW = 1;
        /**
         * 正常
         */
        int NORMAL = 2;
    }
}
