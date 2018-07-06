package com.aman.utils.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aman.utils.message.ZLocalBroadcast;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/2 10:16
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

abstract public class ZService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);

        String[] a = getActions_application();
        if( a!=null && a.length>0){
            ZLocalBroadcast.registerReceiver(getApplicationContext() , a , receiver);
        }
        return result;
    }

    @Override
    public void onDestroy() {
        ZLocalBroadcast.unregisterReceiver(getApplicationContext() , receiver);
        super.onDestroy();

    }

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context $context, Intent $intent) {
            receiveIntent($context, $intent);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    abstract protected void receiveIntent(Context $context, Intent $intent);
    protected String[] getActions_application(){return null;}
}
