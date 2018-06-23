package com.zw;

import android.app.Application;
import android.os.Handler;

import com.aman.utils.Debuger;
import com.aman.utils.message.ZLocalBroadcast;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/4/17 15:56
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MainApplication extends Application {

    private static MainApplication _app;

    private Handler handler = new Handler();

    private Runnable runner = new Runnable() {
        @Override
        public void run() {
            trace("onTime");
            handler.postDelayed(runner , 3000);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        ZLocalBroadcast.applicationContext = this;
        _app = this;
//        this.getApplicationContext().startService()
        handler.post(runner);
    }

    public void trace(String $s){
        $s = "Application--" + $s;
        Debuger.traceTime($s);
    }

    public static MainApplication getInstance(){
        return _app;
    }
}
