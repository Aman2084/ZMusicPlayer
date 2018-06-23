package com.zw.test;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.zw.MainActivity;
import com.zw.R;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/21 15:30
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class TestService extends Service {

    public class LocalBinder extends Binder {
        public TestService getServices(){
            return TestService.this;
        }
    }

    private final IBinder _binder = new LocalBinder();
    private Handler _handler;

    public void onCreate() {
        _handler = new Handler();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent $intent, int $flags, int $startId) {
//        getPlayer().addObserver(onPlayer);
        _baseTime = SystemClock.elapsedRealtime();
        _handler.postDelayed(_run_notification , 20);
        return super.onStartCommand($intent, $flags, $startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

//Notification
    private long _baseTime;
    private int _process = 0;

    private Runnable _run_notification = new Runnable() {
        @Override
        public void run() {
            _process = _process<100 ?  _process+2 : 0;
            String s = "com.zw.test.pause";
            Notification notify = getNotify(TestService.this , s  , false , "歌曲Song" , "作者Singer");
            startForeground(2 , notify);
            _handler.postDelayed(this , 100);
}
};

    private Notification getNotify(Context $c , String $evt ,
                                   boolean $isPlay , String $song , String $singer){
        Intent it = new Intent($evt);
        PendingIntent itp = PendingIntent.getBroadcast($c , R.string.app_name ,
                it , PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews notify = new RemoteViews($c.getPackageName() , R.layout.music_notification);

        int id = $isPlay ? R.drawable.music_notify_pause : R.drawable.music_notify_play;
        notify.setImageViewResource(R.id.btn_play , id);
        notify.setTextViewText(R.id.txt_name , $song);
        notify.setTextViewText(R.id.txt_singer , $singer);


//        notify.setOnClickPendingIntent(R.id.btn_play , itp);
        Intent itn = new Intent($c , MainActivity.class);

        PendingIntent pit = PendingIntent.getActivity($c , R.string.app_name , itn ,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder($c);
        b.setContentIntent(pit).setContent(notify).setTicker($song).setSmallIcon(R.drawable.my_main_icon);
        Notification n = b.build();
        return n;
    }
}
