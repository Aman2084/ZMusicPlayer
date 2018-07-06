package com.zw.music.servive;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.aman.utils.UIUtils;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.service.ZService;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/26 2:10
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicServiceBase_Notification extends ZService {

    private Handler _handler;

    @Override
    public void onCreate() {
        super.onCreate();
        _handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String[] a = {
                IntentActions.PlayNext
                ,IntentActions.PlayPrev
                ,IntentActions.PlayOrPause
                ,IntentActions.Play
                ,IntentActions.Pause
                ,ZNotifcationNames.Click
        };
        IntentFilter f = new IntentFilter();
        for (int i = 0; i<a.length ; i++) {
            String s = a[i];
            f.addAction(s);
        }
        this.registerReceiver(onNotifcation , f);
        updataNotifcation();

        return super.onStartCommand(intent, flags, startId);
    }

    private Runnable _run_notification = new Runnable() {
        @Override
        public void run() {
            PlayModel p = AppInstance.model.play;
            Context c = MusicServiceBase_Notification.this;

            String str = "com.zw.music.sign";
            SongListItem s = AppInstance.model.getCurrectSongListItem();
            boolean playing = p.isPlaying;
            String song = s==null ? "" : s.song.getDisplayName();
            String singer = s==null ? "" : s.song.getDisplaySinger();
            Bitmap b = s==null ? null : s.song.getBmp(c);
            Notification notify = getNotify(c , str , playing , song , singer , b);
            startForeground(101 , notify);
        }
    };

    private Notification getNotify(Context $c , String $evt ,
                                   boolean $isPlay , String $song , String $singer , Bitmap $bmp){
        RemoteViews notify = new RemoteViews($c.getPackageName() , R.layout.music_notification);
        int id = $isPlay ? R.drawable.music_notify_pause : R.drawable.music_notify_play;
        notify.setImageViewResource(R.id.btn_play , id);
        notify.setTextViewText(R.id.txt_name , $song);
        notify.setTextViewText(R.id.txt_singer , $singer);
        if($bmp==null){
            notify.setImageViewResource(R.id.img , R.drawable.music_page_defaulticon);
        }else{
            notify.setImageViewBitmap(R.id.img , $bmp);
        }

        UIUtils.setOnClickPendingIntent($c , notify , R.id.btn_next , IntentActions.PlayNext);
        UIUtils.setOnClickPendingIntent($c , notify , R.id.btn_pre , IntentActions.PlayPrev);
        String s = $isPlay ? IntentActions.Pause : IntentActions.Play;
        UIUtils.setOnClickPendingIntent($c , notify , R.id.btn_play , s);
        Intent it = new Intent(ZNotifcationNames.Click);
        PendingIntent p = PendingIntent.getBroadcast($c , 0 ,
                it , PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder($c);
        b.setContent(notify).setSmallIcon(R.drawable.my_main_ic_music);
        b.setContentIntent(p);

        Notification n = b.build();
        return n;
    }

    private BroadcastReceiver onNotifcation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context $c, Intent $it) {
            onNotifcation($it);
        }
    };

    protected void updataNotifcation(){
        _handler.post(_run_notification);
    }

    protected void onNotifcation(Intent $it){

    }


    @Override
    protected void receiveIntent(Context $context, Intent $intent) {}
}
