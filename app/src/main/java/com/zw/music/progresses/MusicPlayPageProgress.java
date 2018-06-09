package com.zw.music.progresses;

import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.model.AppModel;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayProgress;
import com.zw.global.model.my.SongListModel;
import com.zw.music.ui.MusicPlayPage;
import com.zw.utils.ZProgress;

/**
 * 播放页
 * ZMusicPlayer 1.0
 * Created on 2018/5/22 17:24
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicPlayPageProgress extends ZProgress {

    private MusicPlayPage _playPage;

    public MusicPlayPageProgress() {
        super(null, null);
    }

    private ZObserver onPage = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            String s = null;
            Object data = null;
            AppModel m = AppInstance.model;
            PlayModel p = m.play;

            switch($n.name){
                case AppNotificationNames.ChangSongFavorite:
                    SongListModel l = m.song.songList;
                    SongListItem item = l.list_play.getItemByIndex(p.index);
                    l.setFavorite(item);
                    break;
                case AppNotificationNames.ChangPlayModel:
                    s = IntentActions.ChangPlayModel;
                    break;
                case AppNotificationNames.ChangPlayLoop:
                    s = IntentActions.ChangPlayLoop;
                    break;
                case ZNotifcationNames.Close:
                    clear();
                    break;
            }
            if(s!=null){
                sendIntent(s , data);
            }
        }
    };

//Private Tool
    private void clear(){
        if(_playPage!=null){
            _playPage.deleteObserver(onPage);
            _playPage = null;
        }
    }

//Interface

    public void setData_progress(PlayProgress $p){
        _playPage.setData_progress($p);
    }


    public void show(){
        _playPage = new MusicPlayPage(AppInstance.mainActivity , null);
        _playPage.addObserver(onPage);
        sendIntent(IntentActions.ShowThirdSubPage , _playPage);
    }
    public boolean isShowing(){
        return _playPage!=null;
    }

    public void refuse_song() {
        _playPage.refuse_song();
    }

    public void refuse_model() {
        _playPage.refuse_playMode();
    }

    public void refuse_loop() {
        _playPage.refuse_playLoop();
    }

    public void setData_play(SongListItem item) {
        _playPage.setData_play(item);
    }

    public void pause() {
        _playPage.pause();
    }
}
