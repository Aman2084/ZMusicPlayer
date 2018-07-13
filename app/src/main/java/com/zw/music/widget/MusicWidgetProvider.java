package com.zw.music.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.aman.utils.UIUtils;
import com.aman.utils.message.ZLocalBroadcast;
import com.zw.MainActivity;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayProgress;
import com.zw.music.ZMusicService;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/24 19:18
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicWidgetProvider extends AppWidgetProvider {
    public MusicWidgetProvider() {
        super();
    }

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context $c, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate($c, appWidgetManager, appWidgetIds);

        RemoteViews v = new RemoteViews($c.getPackageName(), R.layout.music_widget);

        PlayModel p = AppInstance.model.play;
        SongListItem item = AppInstance.model.getCurrectSongListItem();
        Bitmap bmp = item==null ?null : item.song.getBmp($c);
        if(bmp==null){
            v.setImageViewResource(R.id.img , R.drawable.main_music_defaulticon);
        }else{
            v.setImageViewBitmap(R.id.img , bmp);
        }
        int id = p.isPlaying ? R.drawable.music_notify_pause : R.drawable.music_notify_play;
        v.setImageViewResource(R.id.btn_play , id);

        v.setProgressBar(R.id.progress ,100 , 100 , false);
        if(item==null){
            v.setTextViewText(R.id.txt_name , "");
        }else{
            v.setTextViewText(R.id.txt_name , item.song.getDisplayName());
            if(p.progress.duration!=0){
                PlayProgress g = p.progress;
                int value = UIUtils.calcProgressBarValue(g.position , g.duration , 100);
                v.setProgressBar(R.id.progress ,100 ,value , false);
            }
        }

        UIUtils.setOnClickPendingIntent($c , v , R.id.btn_pre , IntentActions.PlayPrev);
        UIUtils.setOnClickPendingIntent($c , v , R.id.btn_next , IntentActions.PlayNext);
        UIUtils.setOnClickPendingIntent($c , v , R.id.btn_play , IntentActions.PlayOrPause);
//        Intent tit = new Intent($c , MusicWidgetProvider.class);
//        tit.setAction(IntentActions.PlayOrPause);
//        PendingIntent tpd = PendingIntent.getBroadcast($c , 0 , tit , 0);
//        v.setOnClickPendingIntent(R.id.btn_play , tpd);

        Intent it = new Intent($c,MainActivity.class);
        PendingIntent pd = PendingIntent.getActivity($c, 0,  it, PendingIntent.FLAG_UPDATE_CURRENT);
        v.setOnClickPendingIntent(R.id.root, pd);

        /*it = new Intent($c,MusicWidgetProvider.class);
        pd = PendingIntent.getService($c, 0,  it, PendingIntent.FLAG_UPDATE_CURRENT);
        v.setOnClickPendingIntent(R.id.btn_play, pd);
        */

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, v);
        }
    }


//Tools
    private void control(String $s , Context $c){
        boolean b = ZLocalBroadcast.sendAppIntent($s);
        if(b){  //主线程调用成功，退出
            return;
        }else{  //Application 未启动，启动Service
            Intent it = new Intent($c , ZMusicService.class);
            it.setAction($s);
            $c.startService(it);
        }
    }

//收消息
    private int _index = 0;

    /**
     * 收原生通道内的消息
     */
    @Override
    public void onReceive(Context $c, Intent $it) {
        super.onReceive($c, $it);
        /*switch ($it.getAction()){
            case IntentActions.PlayPrev:
                _index++;
                RemoteViews v = new RemoteViews($c.getPackageName(), R.layout.music_widget);
                v.setTextViewText(R.id.txt_name , String.valueOf(_index));

            case IntentActions.PlayNext:
            case IntentActions.PlayOrPause:
                control($it.getAction() , $c);
                break;
        }*/
    }


}
