package com.zw.music.servive;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.aman.utils.service.ZService;

/**
 * AppWidget 做播控功能用的Service
 * ZMusicPlayer 1.0
 * Created on 2018/6/26 4:06
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class WidgetService extends ZService {

    private String _targetAction;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        _targetAction = intent.getAction();
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    protected void receiveIntent(Context $context, Intent $intent) {

    }
}
