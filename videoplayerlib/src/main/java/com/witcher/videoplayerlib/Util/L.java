package com.witcher.videoplayerlib.Util;

import android.util.Log;

import com.witcher.videoplayerlib.Constant;

/**
 * Created by witcher on 2017/2/27 0027.
 */

public class L {

    public static void i(String str){
        if(Constant.DEBUG){
            Log.i(Constant.TAG,str);
        }
    }

}
