package com.zw;

import android.app.Application;

import com.aman.media.ZAudioPlayer;
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

    public ZAudioPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        ZLocalBroadcast.applicationContext = this;
        player = new ZAudioPlayer();
        _app = this;
    }

    public static MainApplication getInstance(){
        return _app;
    }
}
