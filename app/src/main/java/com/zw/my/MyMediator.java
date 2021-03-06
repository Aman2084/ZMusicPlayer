package com.zw.my;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.aman.utils.message.ZIntent;
import com.aman.utils.observer.Mediator;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObservable;
import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongGroup;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayPosition;
import com.zw.global.model.my.SongListModel;
import com.zw.my.progresses.MyCreatSongListProgress;
import com.zw.my.progresses.MyEditSongListProgress;
import com.zw.my.progresses.MyScanProgress;
import com.zw.my.progresses.MySongManageProgres;
import com.zw.my.ui.MyAllMusic;
import com.zw.my.ui.MyFavorite;
import com.zw.my.ui.MyMainContent;
import com.zw.my.ui.MyMusicFolders;
import com.zw.my.ui.MySongManage;

import java.util.ArrayList;
import java.util.Observable;

/**
 * AmanQuick 1.0
 * Created on 2017/8/18 21:03
 * <p>
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MyMediator extends Mediator {

    private MyAllMusic _ui_allMusic = null;
    private MyMusicFolders _ui_musicFolders = null;
    private MyFavorite _ui_favorite;
    private MyScanProgress _scan = null;

    private MyCreatSongListProgress _creatSongList = null;
    private MyEditSongListProgress _editSongList = null;
    private MySongManageProgres _songManage = null;

    public MyMediator(Context $c){
        super($c);
    }

    @Override
    public void destroy() {
        super.destroy();
        if(_ui_allMusic!=null){
            _ui_allMusic.deleteObserver(this);
            _ui_allMusic = null;
        }
        if(_ui_musicFolders!=null){
            _ui_musicFolders.deleteObserver(this);
            _ui_musicFolders = null;
        }
        if(_ui_favorite!=null){
            _ui_favorite.deleteObserver(onFavorite);
            _ui_favorite = null;
        }
        if(_scan!=null){
            _scan.deleteObserver(this);
            _scan.destroy();
            _scan = null;
        }
        if(_creatSongList!=null){
            _creatSongList.destroy();
            _creatSongList.deleteObserver(onCreatSongList);
            _creatSongList = null;
        }
        if(_editSongList!=null){
            _editSongList.destroy();
            _editSongList = null;
        }
        if(_songManage!=null){
            _songManage.destroy();
            _songManage.deleteObserver(onSongManage);
            _songManage = null;
        }
    }

//Notifcation

    @Override
    public void update(Observable $o, Object $data) {
        super.update($o, $data);
        ZObservable o = (ZObservable)$o;
        Object owner = o.getOwner();
        ZNotification n = (ZNotification)$data;

        if(_ui_allMusic!=null && owner==_ui_allMusic){
            onAllMusic(n);
        }else if(_ui_musicFolders!=null &&owner==_ui_musicFolders){
            onMusicFolders(n);
        }else{
            onScan($o , $data);
        }
    }

//Listener

    private void onMusicFolders(ZNotification $n) {
        MySongModel m = AppInstance.model.song;
        switch($n.name){
            case ZNotifcationNames.Complete:
                ArrayList<Song> s = (ArrayList<Song>) $n.data;
                m.song.set_allSongs(s);
                m.songList.importSongs(s);
                _ui_allMusic.refause();
                sendIntent(IntentActions.Import_SetService);
                break;
        }
    }

    private void onAllMusic(ZNotification $n){
        MySongModel m = AppInstance.model.song;
        switch($n.name){
            case AppNotificationNames.Scan:
//                checkScanPermission();
                getScan().scan();
                break;
            case AppNotificationNames.Manage:
                getSongManage().show(m.song.get_allSongs() , MySongManage.DisplayMode.NoBrowse);
                break;
            case AppNotificationNames.ShowSongGroup:
                getSongManage().show((SongGroup)$n.data , MySongManage.DisplayMode.Browse);
                break;
            case AppNotificationNames.PlaySongs:
                sendIntent(IntentActions.PrePlaySongs , $n.data);
                break;
            case IntentActions.ShowMyFavorites:
                showMyFavorites();
                break;
        }
    }

//Scan
    private void checkScanPermission() {
        //TODO.. 713
//        sendNotification(AppNotificationNames.CheckPermissionForScan);
//        MainActivity a = (MainActivity)AppInstance.mainActivity;
//        a.shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE");
    }

    private void onScan(Object $o, Object $data) {
        ZNotification n = (ZNotification)$data;
        Object data = n.data;
        if($o instanceof MyScanProgress){
            if(n.name.equals(ZNotifcationNames.Complete)){
                MyMusicFolders f = get_ui_MusicFolders();
                f.setData((ArrayList<Song>)data);
                sendIntent(IntentActions.ShowThirdSubPage ,f);
            }
        }
    }

    private ZObserver onCreatSongList = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
        switch($n.name){

        }
        }
    };

    private ZObserver onSongManage = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch($n.name){
                case ZNotifcationNames.Close:
                    if(_ui_allMusic!=null){
                        _ui_allMusic.refause();
                    }
                    break;
            }
        }
    };

    private ZObserver onFavorite = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case ZNotifcationNames.Close:
                    _ui_favorite.deleteObserver(onFavorite);
                    _ui_favorite = null;
                    break;
            }
        }
    };

//LocalBroadcasts
    @Override
    protected String[] getActions_application() {
        String[] a = {
                IntentActions.ShowMyAllMusic
                ,IntentActions.ShowMyFavorites
                ,IntentActions.NewSongList
                ,IntentActions.EditSongList
                ,IntentActions.PrePlaySongList
                ,IntentActions.PrePlaySongs
                ,IntentActions.ClearPlayList
                ,IntentActions.Import_SetUI
        };
        return a;
    }

    protected void receiveIntent(Context $context, Intent $intent){
        View v = null;
        Object body = null;
        if($intent instanceof ZIntent){
            body = ((ZIntent)$intent).data;
        }
        switch($intent.getAction()){
            case IntentActions.ShowMyAllMusic:
                v = get_ui_allMusic();
                break;
            case IntentActions.NewSongList:
                getCreatSongList().addSongList();
                break;
            case IntentActions.ShowMyFavorites:
                showMyFavorites();
                break;
            case IntentActions.EditSongList:
                SongList list = (SongList)((ZIntent)$intent).data;
                getEditSongList().show(list);
                break;
            case IntentActions.PrePlaySongList:
                PlayPosition p = (PlayPosition) body;
                playSongList(p);
                break;
            case IntentActions.PrePlaySongs:
                SongGroup g = (SongGroup)body;
                playSongGroup(g);
                break;
            case IntentActions.ClearPlayList:
                SongListModel m = AppInstance.model.song.songList;
                m.updateSongList_song(m.list_play.id , new ArrayList<Song>());
                break;
            case IntentActions.Import_SetUI:
                refuseByImport();
                break;
        }

        if(v!=null){
            sendIntent(IntentActions.ShowSecondSubPage , v);
        }
    }

/************************Tools*************************/

    private void refuseByImport() {
        MyMainContent c = AppInstance.MainUI_my;
        c.refuse();
    }


    private void showMyFavorites() {
        if(_ui_favorite!=null){
            return;
        }
        _ui_favorite = new MyFavorite(AppInstance.mainActivity);
        _ui_favorite.addObserver(onFavorite);
        sendIntent(IntentActions.ShowSecondSubPage , _ui_favorite);
    }

    private void playSongList(PlayPosition $p) {
        SongListModel m = AppInstance.model.song.songList;
        PlayModel p = AppInstance.model.play;
        SongList l = m.getSongListById($p.songListId);
        int i = l.relation2Index($p.relationId);
        m.updateSongList_song(m.list_play.id , l.getSongs());
        l = m.list_play;
        $p.relationId = l.index2Relation(i);
        l.position = $p;
        sendIntent(IntentActions.PlaySongList , l);
    }

    private void playSongGroup(SongGroup $g) {
        SongListModel m = AppInstance.model.song.songList;
        SongList l = m.list_play;
        PlayModel p = AppInstance.model.play;
        m.updateSongList_song(m.list_play.id , $g.songs);
        p.index = $g.index;

        SongListItem item = l.getItemByIndex(p.index);
        if(item==null){
            return;
        }
        l.position = new PlayPosition(l.id , item.relationId);
        sendIntent(IntentActions.PlaySongList, l);
    }


//getter and Setter
    private MyAllMusic get_ui_allMusic() {
        if(_ui_allMusic==null){
            _ui_allMusic = new MyAllMusic(_context, null);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT);
            _ui_allMusic.setLayoutParams(p);
            _ui_allMusic.addObserver(this);
        }
        return _ui_allMusic;
    }

    private MyMusicFolders get_ui_MusicFolders(){
        if(_ui_musicFolders ==null){
            _ui_musicFolders = new MyMusicFolders(_context, null);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT);
            _ui_musicFolders.setLayoutParams(p);
            _ui_musicFolders.addObserver(this);
        }
        return _ui_musicFolders;
    }

    private MyScanProgress getScan(){
        if(_scan==null){
            _scan = new MyScanProgress();
            _scan.addObserver(this);
        }
        return _scan;
    }

    private MyCreatSongListProgress getCreatSongList(){
        if(_creatSongList ==null){
            _creatSongList = new MyCreatSongListProgress();
            _creatSongList.addObserver(onCreatSongList);
        }
        return _creatSongList;
    }

    private MyEditSongListProgress getEditSongList(){
        if(_editSongList==null){
            _editSongList = new MyEditSongListProgress();
        }
        return _editSongList;
    }

    private MySongManageProgres getSongManage(){
        if(_songManage==null){
            _songManage = new MySongManageProgres();
            _songManage.addObserver(onSongManage);
        }
        return _songManage;
    }
}
