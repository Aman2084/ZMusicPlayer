package com.zw.music.servive;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.aman.utils.UIUtils;
import com.zw.MainActivity;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayProgress;
import com.zw.music.ui.MusicWidgetProvider;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/26 1:28
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicServiceBase_Widget extends MusicServiceBase_Notification {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i = super.onStartCommand(intent, flags, startId);
        updataWidget();
        return i;
    }

    protected void updataWidget(){
        Context c = getApplicationContext();
        RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.music_widget);
        PlayModel p = AppInstance.model.play;
        SongListItem item = AppInstance.model.getCurrectSongListItem();

        Bitmap bmp = item==null ?null : item.song.getBmp(c);
        if(bmp==null){
            v.setImageViewResource(R.id.img , R.drawable.music_page_defaulticon);
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
                int value = 100*(p.progress.position / p.progress.duration);
                v.setProgressBar(R.id.progress ,100 ,value , false);
            }
        }
        UIUtils.setOnClickPendingIntent(c , v , R.id.btn_pre , IntentActions.PlayPrev);
        UIUtils.setOnClickPendingIntent(c , v , R.id.btn_next , IntentActions.PlayNext);
        UIUtils.setOnClickPendingIntent(c , v , R.id.btn_play , IntentActions.PlayOrPause);
        Intent it = new Intent(c,MainActivity.class);
        PendingIntent pd = PendingIntent.getActivity(c, 0,  it, PendingIntent.FLAG_UPDATE_CURRENT);
        v.setOnClickPendingIntent(R.id.root, pd);


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
        appWidgetManager.updateAppWidget(new ComponentName(c, MusicWidgetProvider.class), v);
    }

    protected void updataWidget_song(){
        Context c = getApplicationContext();
        RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.music_widget);
        PlayModel p = AppInstance.model.play;
        SongListItem item = AppInstance.model.getCurrectSongListItem();

        Bitmap bmp = item==null ?null : item.song.getBmp(c);
        if(bmp==null){
            v.setImageViewResource(R.id.img , R.drawable.music_page_defaulticon);
        }else{
            v.setImageViewBitmap(R.id.img , bmp);
        }

        v.setProgressBar(R.id.progress ,100 , 0 , false);
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

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
        appWidgetManager.updateAppWidget(new ComponentName(c, MusicWidgetProvider.class), v);
    }

    protected void updataWidget_state(){
        Context c = getApplicationContext();
        RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.music_widget);
        PlayModel p = AppInstance.model.play;

        int id = p.isPlaying ? R.drawable.music_notify_pause : R.drawable.music_notify_play;
        v.setImageViewResource(R.id.btn_play , id);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
        appWidgetManager.updateAppWidget(new ComponentName(c, MusicWidgetProvider.class), v);
        appWidgetManager.updateAppWidget(new ComponentName(c, MusicWidgetProvider.class), v);
    }

    protected void updataWidget_progress(){
        Context c = getApplicationContext();
        RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.music_widget);
        PlayModel p = AppInstance.model.play;

        v.setProgressBar(R.id.progress ,100 , 100 , false);
        if(p!=null && p.progress.duration!=0){
            PlayProgress g = p.progress;
            int value = UIUtils.calcProgressBarValue(g.position , g.duration , 100);
            v.setProgressBar(R.id.progress ,100 ,value , false);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c);
        appWidgetManager.updateAppWidget(new ComponentName(c, MusicWidgetProvider.class), v);
    }
}
