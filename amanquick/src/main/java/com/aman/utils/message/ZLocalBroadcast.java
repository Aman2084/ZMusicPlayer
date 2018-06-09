package com.aman.utils.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * 本地消息工具函数
 * ZMusicPlayer 1.0
 * Created on 2018/5/12 13:23
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZLocalBroadcast {

    /**Application 上下文*/
    public static Context applicationContext = null;

    public static boolean sendAppIntent(String $action){
        return sendAppIntent($action , null);
    }


    public static boolean sendAppIntent(String $action , Object $data){
        return sendLoacalIntent(applicationContext , $action , $data);
    }

    public static boolean sendLoacalIntent(Context $c , String $action){
        return sendLoacalIntent($c , $action , null);
    }

    public static boolean sendLoacalIntent(Context $c , String $action , Object $data){
        if($c==null || $action==null){
            return false;
        }

        LocalBroadcastManager m = LocalBroadcastManager.getInstance($c);
        if(m!=null){
            ZIntent t = new ZIntent($action , $data);
            m.sendBroadcast(t);
        }
        return true;
    }

    public static void registerAppReceiver(String[] $a , BroadcastReceiver $r) {
        registerReceiver(applicationContext , $a , $r);
    }

    public static void registerReceiver(Context $c , String[] $a , BroadcastReceiver $r) {
        if($r==null){
            Log.e("registerReceiver" , "报空！");
        }
        IntentFilter f = new IntentFilter();
        for (int i = 0; i<$a.length ; i++) {
            String s = $a[i];
            f.addAction(s);
        }
        LocalBroadcastManager m = LocalBroadcastManager.getInstance($c);
        m.registerReceiver($r, f);
    }

    public static void unregisterAppReceiver(BroadcastReceiver $r){
        unregisterReceiver(applicationContext , $r);
    }


    public static void unregisterReceiver(Context $c , BroadcastReceiver $r){
        LocalBroadcastManager m = LocalBroadcastManager.getInstance($c);
        m.unregisterReceiver($r);
    }
}
