package com.zw.my.progresses;

import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongGroup;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.my.SongListModel;
import com.zw.my.ui.MySongManage;
import com.zw.my.ui.item.MySongListSelector;
import com.zw.utils.ZProgress;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/11/29 2:13
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MySongManageProgres extends ZProgress {

    private ArrayList<SongListItem> _songs_selected;

    private MySongManage _ui = null;
    private MySongListSelector _selector;

    public MySongManageProgres() {
        super(null, null);
    }

    @Override
    public void destroy() {
        super.destroy();
        disposeUI();
    }

//interface
    public void show(ArrayList<Song> $a , MySongManage.DisplayMode $mode){
        if($a.size()<1){
            return;
        }
        ArrayList<SongListItem> a = MySongModel.Songs2SongListItems($a);
        AppInstance.model.song.songList.signFavorite(a);
        if(_ui==null){
            _ui = new MySongManage(AppInstance.mainActivity);
            _ui.addObserver(onUI);
        }
        _ui.setDsiplayMode($mode);
        _ui.setData(a);
        sendIntent(IntentActions.ShowThirdSubPage , _ui);
    }

    public void show(SongGroup $data , MySongManage.DisplayMode $mode) {
        show($data.songs , $mode);
        _ui.setTitle($data.name);
    }


//Listeners
    private ZObserver onUI = new ZObserver() {
    @Override
        public void onNotification(ZNotification $n) {
            SongListModel m = AppInstance.model.song.songList;

            switch($n.name){
                case ZNotifcationNames.Close:
                    disposeUI();
                    sendNotification(ZNotifcationNames.Close);
                    break;
                case ZNotifcationNames.Delete:
                    deleteSongs((ArrayList<SongListItem>) $n.data);
                    break;
                case AppNotificationNames.Favorite:
                case AppNotificationNames.UNFavorite:
                    SongListItem o = (SongListItem) $n.data;
                    m.setFavorite(o);
                    break;
                case AppNotificationNames.Add2SongList:
                    _songs_selected = (ArrayList<SongListItem>) $n.data;
                    getSelecter().setData(m.get_allSongList());
                    sendIntent(IntentActions.ShowThirdSubPage , _selector);
                    break;
            }
        }
    };

    private ZObserver onSelector = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case ZNotifcationNames.Close:
                    _ui.set_showMovieType(AnimationTypes.None);
                    _ui.clearSetected();
                    sendIntent(IntentActions.ShowThirdSubPage , _ui);
                    break;
                case ZNotifcationNames.Selected:
                    add2List(((SongList)$n.data));
                    break;
            }
        }
    };

//Logic
    private void add2List(SongList $s){
        SongListModel m = AppInstance.model.song.songList;
        ArrayList<Song> a = MySongModel.SongListItems2Songs(_songs_selected);
        m.addSong2List($s.id , a);
        _selector.close();
    }


    private void deleteSongs(ArrayList<SongListItem> $a) {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<Song>  arr = new ArrayList<>();
        for (SongListItem s:$a) {
            a.add(s.songId);
            arr.add(s.song);
        }
        MySongModel m = AppInstance.model.song;
        m.deleteSongs(a);
    }

    private void disposeUI() {
        _ui.deleteObserver(onUI);
        _ui = null;
        if(_selector!=null){
            _selector.deleteObserver(onSelector);
            _selector = null;
        }
    }

//getter and setter

    private MySongListSelector getSelecter(){
        if(_selector==null){
            _selector = new MySongListSelector(AppInstance.mainActivity , null);
            _selector.addObserver(onSelector);
        }
        return _selector;
    }

}
