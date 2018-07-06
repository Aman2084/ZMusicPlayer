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
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.MainActivity;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.IntentNotice;
import com.zw.global.enums.SharedPreferencesKeys;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.SongGroup;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayPosition;
import com.zw.global.model.music.PlayProgress;
import com.zw.global.model.music.SongMenu;
import com.zw.global.model.my.SongListModel;
import com.zw.music.servive.MusicServiceBase_Widget;

import java.util.ArrayList;

import static com.zw.global.AppInstance.model;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/4/18 1:42
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZMusicService extends MusicServiceBase_Widget {

    private ZAudioPlayer _player;
    private SongMenu _menu;

    /**
     * 切换未完成时记录
     */
    private int _seekTime = -1;
    private boolean _lock_seek = false;

    @Override
    public void onCreate() {
        _menu = new SongMenu();

        if(!AppInstance.initialized){
            AppInstance.init(getApplicationContext());
        }

        //初始化数据
        PlayModel p = getPlayModel();
        SongListModel l = AppInstance.model.song.songList;
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
        super.onStartCommand($intent, $flags, $startId);
        getPlayer().addObserver(onPlayer);

        String s = $intent.getAction();
        if(s!=null && s.length()>0){
            receiveIntent(getBaseContext() , $intent);
        }
        updataWidget();
        return START_STICKY;
    }

    @Override
    protected String[] getActions_application() {
        String[] a = new String[]{

            IntentActions.PlaySongs2
            ,IntentActions.PlaySongList
            ,IntentActions.Stop
            ,IntentActions.Play
            ,IntentActions.Pause
            ,IntentActions.Seek
            ,IntentActions.PlayNext
            ,IntentActions.PlayPrev
            ,IntentActions.PlayOrPause
            ,IntentActions.Jump2RelationId
            ,IntentActions.ChangPlayLoop
            ,IntentActions.ChangPlayModel
            ,IntentActions.ClearPlayList
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
            case IntentActions.PlaySongs2:
                SongGroup g = (SongGroup)data;
                SongList l = group2List(g);
                _menu.setData(l , g.index);
                SongListItem item = _menu.getCurrectSong();
                if(item!=null){
                    play(item);
                }
                break;
            case IntentActions.PlaySongList:
                l = (SongList) data;
                playSongList(l);
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
                playNext_super();
                break;
            case IntentActions.PlayPrev:
                playPrev_super();
                break;
            case IntentActions.PlayOrPause:
                if(_player!=null && _player.isPlaying()){
                    p.pause();
                }else{
                    play();
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
            case IntentActions.ClearPlayList:
                clearList();
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
                    updataWidget_song();
                    break;
                case ZAudioNotification.Play:
                    m.isPlaying = true;
                    list_play.setPlayByRelation(_menu.getCurrectSong().relationId);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicStart, _menu.getCurrectSong());
                    updataNotifcation();
                    updataWidget_state();
                    break;
                case ZAudioNotification.Pause:
                    m.isPlaying = false;
                    list_play.setPauseByRelation(_menu.getCurrectSong().relationId);
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicPause);
                    updataNotifcation();
                    updataWidget_state();
                    break;
                case ZAudioNotification.Stop:
                    m.isPlaying = false;
                    if(list_play!=null && !_menu.isEmpty()){
                        list_play.setStopByRelation(_menu.getCurrectSong().relationId);
                    }
                    ZLocalBroadcast.sendAppIntent(IntentNotice.MusicStop);
                    updataNotifcation();
                    updataWidget_state();
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
                    updataWidget_progress();
                    break;
                case ZAudioNotification.Progress:
                    getPlayModel().progress.setData(n.duration , n.position);
                    p = new PlayProgress(n.duration , n.position);
                    updataWidget_progress();
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
                    updataNotifcation();
                    break;
            }
        }
    };

    @Override
    protected void onNotifcation(Intent $it) {
        switch ($it.getAction()){
            case IntentActions.Play:
                play();
                break;
            case IntentActions.Pause:
                _player.pause();
                break;
            case IntentActions.PlayPrev:
                playPrev_super();
                break;
            case IntentActions.PlayNext:
                playNext_super();
                break;
            case IntentActions.PlayOrPause:
                if(_player!=null && _player.isPlaying()){
                    _player.pause();
                }else{
                    play();
                }
                break;
            case ZNotifcationNames.Click:
                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplication().startActivity(it);
                break;
        }
    }

    //Tools

    private void playNext() {
        SongListItem s = _menu.next();
        if(s!=null){
            play(s);
        }else{
            ZLocalBroadcast.sendAppIntent(IntentNotice.MusicListComplete);
        }
    }

    private void playNext_super(){
        if(_menu.isEmpty()){
            return;
        }
        SongListItem s = _menu.next_super();
        play(s);
    }

    private void playPrev_super(){
        if(_menu.isEmpty()){
            return;
        }
        SongListItem s = _menu.prev_super();
        play(s);
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
        p.play(s);
    }

    /**
     * 从某个时间点开始播放歌曲
     * @param $s            歌曲数据源
     * @param $position     开始播放的位置(单位ms)
     */
    public void gotoAndPlay(SongListItem $s , int $position){
        if($s==null){
            return;
        }
        PlayModel m = getPlayModel();
        m.index = _menu.getCurrectIndex();
        m.progress.reset();
        String s = $s.song.getPath();
        ZAudioPlayer p = getPlayer();
        if(p.prepare(s)){
            p.seekTo($position);
            p.start();
        }
    }

    private void playSongList(SongList $l) {
        PlayPosition p = $l.position;
        int i = $l.relation2Index(p.relationId);
        _menu.setData($l , i);
        SongListItem item = _menu.getCurrectSong();
        if(item==null){
            return;
        }
        if(p.position>0){
            gotoAndPlay(item , p.position);
        }else{
            play(item);
        }
    }

    private void clearList() {
        _menu.clear();
        if(_player!=null){
            _player.stop();
        }
        PlayModel p = getPlayModel();
        p.reset();
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
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor e = p.edit();
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
        if(_player!=null){
            _player.stop();
        }
        savePlayProgress();
        super.onDestroy();
    }


//Getter and Setter

    private PlayModel getPlayModel(){
        return AppInstance.model.play;
    }
    private ZAudioPlayer getPlayer(){
        if(_player ==null){
            _player = new ZAudioPlayer();
        }
        return _player;
    }
}
