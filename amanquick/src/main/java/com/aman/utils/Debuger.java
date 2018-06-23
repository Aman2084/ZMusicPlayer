package com.aman.utils;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * AmanQucik 1.0
 * Created on 2018/6/10 18:13
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class Debuger {

    public static void traceTime(String $title){
        long t = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("hh:mm:ss-SSS");
        String s = f.format(t);
        Log.d($title + "===>" , s);
    }

}
