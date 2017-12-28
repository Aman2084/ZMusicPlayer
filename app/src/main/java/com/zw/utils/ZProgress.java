package com.zw.utils;

import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.aman.utils.message.ZIntent;
import com.aman.utils.observer.ZObservable;
import com.zw.global.AppInstance;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/28 9:13
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZProgress extends ZObservable {
    public ZProgress(@Nullable Object $owner, @Nullable String $name) {
        super($owner, $name);
    }

    protected void sendIntent(String $action , @Nullable Object $data){
        LocalBroadcastManager m = LocalBroadcastManager.getInstance(AppInstance.mainActivity);
        ZIntent o = new ZIntent($action , $data);
        m.sendBroadcast(o);
    }
}
