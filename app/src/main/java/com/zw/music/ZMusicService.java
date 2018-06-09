package com.zw.music;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.aman.media.ZAudioNotification;
import com.aman.media.ZAudioPlayer;
import com.aman.utils.message.ZIntent;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.aman.utils.service.ZService;
import com.zw.MainApplication;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.IntentNotice;
import com.zw.global.enums.SharedPreferencesKeys;
import com.zw.global.model.AppModel;
import com.zw.global.model.MySongModel;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayProgress;
import com.zw.global.model.data.SongGroup;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.my.SongListModel;

import java.util.ArrayList;

import static com.zw.global.AppInstance.model;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/4/18 1:42
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZMusicService extends ZService {

    private SongMenu _menu;

    /**
     * 切换未完成时记录
     */
    private int _seekTime = -1;
    private boolean _lock_seek = false;

    @Override
    public void onCreate() {
        _menu = new SongMenu();

        //初始化数据
        PlayModel p = getPlayModel();
        SongListModel l = AppInstance.model.song.songList;
        SharedPreferences p1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        p.index = p1.getInt(SharedPreferencesKeys.CurrectMusicIndex , 0);
        p.progress.position = p1.getInt(SharedPreferencesKeys.CurrectMusicPosition , 0);
        p.playModel = p1.getString(SharedPreferencesKeys.PlayModel , SongMenu.Order);
        p.isLoop = p1.getBoolean(SharedPreferencesKeys.PlayLoop , true);
        p.menu = _menu;
        _menu.setData(l.list_play , p.index);
        _menu.setMode(p.playModel);
        _menu.setIsLoop(p.isLoop);
        l.list_play.setPauseItemByIndex(p.index);

        SongListItem item = _menu.getCurrectSong();
        if(item!=null){
            ZAudioPlayer player = getPlayer();
            player.prepare(item.song.getPath());
        }
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent $intent, int $flags, int $startId) {
        getPlayer().addObserver(onPlayer);
        return super.onStartCommand($intent, $flags, $startId);
    }

    @Override
    protected String[] getActions_application() {
        String[] a = new String[]{
            IntentActions.PlaySongs
            ,IntentActions.Stop
            ,IntentActions.Play
            ,IntentActions.Pause
            ,IntentActions.Seek
            ,IntentActions.PlayNext
            ,IntentActions.Jump2RelationId
            ,IntentActions.ChangPlayLoop
            ,IntentActions.ChangPlayModel
        };
        return a;
    }

    @Override
    protected void receiveIntent(Context $context, Intent $intent) {
        Object data = null;
        if($intent instanceof ZIntent){
            data = ((ZIntent)$intent).data;
        }

        ZAudioPlayer p = getPlayer();

        switch($intent.getAction()){
            case IntentActions.PlaySongs:
                SongGroup g = (SongGroup)data;
                SongList l = group2List(g);
                _menu.setData(l , g.index);
                SongListItem item = _menu.getCurrectSong();
                if(item!=null){
                    play(item);
                }
                break;
            case IntentActions.Stop:
                p.stop();
                break;
            case IntentActions.Play:
                play();
                break;
            case IntentActions.Pause:
                p.pause();
                break;
            case IntentActions.PlayNext:
                if(_menu.hasNext()){
                    playNext();
                }
                break;
            case IntentActions.ChangPlayLoop:
                changPlayLoop((Boolean)data);
                break;
            case IntentActions.ChangPlayModel:
                changPlayModel((String)data);
                break;
            case IntentActions.Seek:
                PlayProgress pg = (PlayProgress) data;
                if(_lock_seek){
                    _seekTime = pg.position;
                }else{
                    _lock_seek = true;
                    p.seekTo(pg.position);
                }
                break;
            case IntentActions.Jump2RelationId:
                SongListItem s = _menu.jump2RelationId((int)data);
                play(s);
                break;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


//Listeners
    private ZObserver onPlayer = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            ZAudioNotification n = (ZAudioNotification) $n;
            PlayProgress p = null;
            PlayModel m = AppInstance.model.play;
            SongList list_play = AppInstance.model.song.songList.list_play;
            switch(n.name){
                case ZAudioNotification.Prepared:
                    p = new PlayProgress(n.duration , n.position);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicReady, p);
                    break;
                case ZAudioNotification.Play:
                    m.isPlaying = true;
                    list_play.setPlayByRelation(_menu.getCurrectSong().relationId);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicStart, _menu.getCurrectSong());
                    break;
                case ZAudioNotification.Pause:
                    m.isPlaying = false;
                    list_play.setPauseByRelation(_menu.getCurrectSong().relationId);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicPause);
                    break;
                case ZAudioNotification.Stop:
                    m.isPlaying = false;
                    list_play.setStopByRelation(_menu.getCurrectSong().relationId);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicStop);
                    break;
                case ZAudioNotification.Seek:
                    p = getPlayModel().progress;
                    p.setData(n.duration , n.duration);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicSeekComplete, p.clone());
                    if(_seekTime>=0){
                        getPlayer().seekTo(_seekTime);
                        _seekTime = -1;
                    }else{
                        _lock_seek = false;
                    }
                    break;
                case ZAudioNotification.Progress:
                    getPlayModel().progress.setData(n.duration , n.position);
                    p = new PlayProgress(n.duration , n.position);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicProgress, p);
                    break;
                case ZAudioNotification.Error:
                    m.isPlaying = false;
                    list_play.setStopByRelation(_menu.getCurrectSong().relationId);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicError, $n.data);
                    playNext();
                    break;
                case ZAudioNotification.Complete:
                    m.isPlaying = false;
                    list_play.setStopByRelation(_menu.getCurrectSong().relationId);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicComplete, _menu.getCurrectSong());
                    playNext();
                    break;
            }
        }
    };

    private void playNext() {
        SongListItem s = _menu.next();
        if(s!=null){
            play(s);
        }else{
            ZLocalBroadcast.sendAppIntent(IntentNotice.MusicListComplete);
        }
    }

    public void play(){
        ZAudioPlayer p = getPlayer();
        if(p.isPlaying()){
            return;
        }
        if(p.isReady){
            p.start();
        }else{
            play(_menu.getCurrectSong());
        }
    }

    private void play(SongListItem $s){
        if($s==null){
            return;
        }

        PlayModel m = getPlayModel();
        m.index = _menu.getCurrectIndex();
        m.progress.reset();
        String s = $s.song.getPath();
        ZAudioPlayer p = getPlayer();
        if(p.isPlaying()){
            p.stop();
        }
        p.play(s);
    }

    private ZAudioPlayer getPlayer(){
        return MainApplication.getInstance().player;
    }

    private SongList group2List(SongGroup $g){
        SongList l = new SongList();
        SongListModel m = model.song.songList;

        ArrayList<SongListItem> a = MySongModel.Songs2SongListItems($g.songs);
        m.signFavorite(a);
        for (int i = 0; i <a.size() ; i++) {
            SongListItem item = a.get(i);
            item.index = i;
        }
        l.items = a;
        l.id = m.list_play.id;
        l.type = m.list_play.type;
        l.title = $g.name;
        return l;
    }

    private void savePlayProgress(){
        SharedPreferences p1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor e = p1.edit();
        e.putInt(SharedPreferencesKeys.CurrectMusicIndex , _menu.getCurrectIndex());
        e.putInt(SharedPreferencesKeys.CurrectMusicPosition , getPlayer().getCurrentPosition());
    }


    private void changPlayLoop(Boolean $b) {
        boolean b = _menu.getIsLoop();
        if($b==null){
            $b = !b;
        }
        if($b==b){
            return;
        }
        _menu.setIsLoop($b);
        getPlayModel().isLoop = $b;

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(SharedPreferencesKeys.PlayLoop , $b);

        ZLocalBroadcast.sendAppIntent(IntentNotice.PlayLoopChanged , $b);

    }

    private void changPlayModel(String $model) {
        String s = _menu.getMode();
        if($model==null){
            $model = SongMenu.getNextModel(s);
        }
        if($model.equals(s)){
            return;
        }
        _menu.setMode($model);
        getPlayModel().playModel = $model;

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor e = p.edit();
        e.putString(SharedPreferencesKeys.PlayModel , $model);

        ZLocalBroadcast.sendAppIntent(IntentNotice.PlayModelChanged , $model);
    }


    @Override
    public void onDestroy() {
        getPlayer().stop();
        savePlayProgress();
        super.onDestroy();
    }

    private PlayModel getPlayModel(){
        return AppModel.getInstance().play;
    }
}
