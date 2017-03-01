package com.witcher.videoplayerlib.Util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.witcher.videoplayerlib.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static com.witcher.videoplayerlib.Constant.BatteryState;
import static com.witcher.videoplayerlib.Constant.NetState;

/**
 * Created by witcher on 2017/2/27 0027.
 */

public class Util {
    /**
     * 时长格式化显示
     */
    public static String formatTime(long time) {
        long total_seconds = time / 1000;
        long hours = total_seconds / 3600;
        long minutes = (total_seconds % 3600) / 60;
        long seconds = total_seconds % 60;
        if (time <= 0) {
            return "00:00";
        }
        if (hours >= 100) {
            return String.format(Locale.CHINA, "%d:%02d:%02d", hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format(Locale.CHINA, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.CHINA, "%02d:%02d", minutes, seconds);
        }
    }

    public static long formatBuffer(long duration, int percentage) {
        return duration * percentage / 100;
    }

    /**
     * 修改当前屏幕亮度
     *
     * @param activity   activity
     * @param brightness new brightness
     */
    public static void setBrightness(Activity activity, float brightness) {
        if (brightness > 1) {
            brightness = 1.0f;
        } else if (brightness < 0.01) {
            brightness = 0.01f;
        }
        WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
        lpa.screenBrightness = brightness;
        activity.getWindow().setAttributes(lpa);
    }

    /**
     * 获取当前屏幕亮度
     *
     * @param activity activity
     * @return int 0-100 百分比
     */
    public static float getBrightness(Activity activity) {
        return activity.getWindow().getAttributes().screenBrightness;
    }

    /**
     * 设置当前音量
     *
     * @param manager audioMgr
     * @param volume  int 新的音量 校验数据正确在调用之前就做好
     */
    public static void setVolume(AudioManager manager, int volume) {
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    /**
     * 获取当前音量
     *
     * @param manager audioMgr
     * @return int
     */
    public static int getVolume(AudioManager manager) {
        return manager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context ctx
     * @return int width
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return format.format(date);
    }

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context ctx
     * @return int
     */
    public static int getNetType(Context context) {
        int netType = NetState.NO_NET;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NetState.WIFI;
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE && !telephonyManager.isNetworkRoaming()) {
                netType = NetState.G4;
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NetState.G3;
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    && !telephonyManager.isNetworkRoaming()) {
                netType = NetState.G2;
            } else {
                netType = NetState.G2;
            }
        }
        return netType;
    }

    public static String getNetState(Context context) {
        return getNetState(context, Util.getNetType(context));
    }

    public static String getNetState(Context context, int netType) {
        int resId = R.string.no_net;
        switch (netType) {
            case NetState.NO_NET: {
                resId = R.string.no_net;
            }
            break;
            case NetState.WIFI: {
                resId = R.string.wifi;
            }
            break;
            case NetState.G2: {
                resId = R.string.g2;
            }
            break;
            case NetState.G3: {
                resId = R.string.g3;
            }
            break;
            case NetState.G4: {
                resId = R.string.g4;
            }
            break;
        }
        return context.getResources().getString(resId);
    }

    public static int getBatteryStateRes(int batteryState) {
        switch (batteryState) {
            case BatteryState.CHARGEING: {
                return R.drawable.player_battery_charging_icon;
            }
            case BatteryState.LOW: {
                return R.drawable.player_battery_red_icon;
            }
            case BatteryState.NORMAL: {
                return R.drawable.player_battery_icon;
            }
            default:
                return R.drawable.player_battery_icon;
        }
    }

}
