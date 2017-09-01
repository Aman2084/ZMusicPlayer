package com.zw.my;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.aman.utils.observer.Mediator;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObservable;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.NotificationNames;
import com.zw.global.model.data.Song;
import com.zw.my.progresses.MyScanProgress;

import java.util.ArrayList;
import java.util.Observable;

/**
 * AmanQuick 1.0
 * Created on 2017/8/18 21:03
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MyMediator extends Mediator {

    private MyAllMusic _ui_allMusic = null;
    private MyMusicFolders _ui_musicFolders = null;
    private MyScanProgress _scan = null;


    public MyMediator(Context $c){
        super($c);
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
            onProgress($o , $data);
        }
    }

    private void onMusicFolders(ZNotification $n) {
        switch($n.name){
            case ZNotifcationNames.Complete:
                ArrayList<Song> s = (ArrayList<Song>) $n.data;
                AppInstance.songSQL.resetAllSong(s);
                AppInstance.model.set_allSongs(s);
                sendIntent(IntentActions.ImportSongComplete , s);
                _ui_allMusic.refause();
                break;
        }
    }

    private void onAllMusic(ZNotification $n){
        switch($n.name){
            case NotificationNames.Scan:
                getScan().scan();
                break;
            case NotificationNames.PlaySongList:
                sendIntent(IntentActions.PlaySongList , $n.data);
                break;
            case NotificationNames.ShowSongList:
                //TODO... 显示歌曲清单
                break;
        }
    }

    private void onProgress(Object $o, Object $data) {
        ZNotification n = (ZNotification)$data;
        Object data = n.data;
        if($o instanceof MyScanProgress){
            if(n.name== NotificationNames.Complete){
                MyMusicFolders f = get_ui_MusicFolders();
                f.setData((ArrayList<Song>)data);
                sendIntent(IntentActions.ShowThirdSubPage ,f);
            }
        }
    }

//LocalBroadcasts
    @Override
    protected String[] getLocalIntentActions() {
        String[] a = {
                IntentActions.ShowMyAllMusic,
                IntentActions.ShowMyFavorites
        };
        return a;
    }

    protected void receiverLocalBroadcast(Context $context, Intent $intent){
        View v = null;
        switch($intent.getAction()){
            case IntentActions.ShowMyAllMusic:
                v = get_ui_allMusic();
                break;
        }

        if(v!=null){
            sendIntent(IntentActions.ShowSecondSubPage , v);
        }
    }


//getter andSetter
    private MyAllMusic get_ui_allMusic() {
        if(_ui_allMusic==null){
            _ui_allMusic = new MyAllMusic(_context , null);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT , RelativeLayout.LayoutParams.MATCH_PARENT);
            _ui_allMusic.setLayoutParams(p);
            _ui_allMusic.addObserver(this);
        }
        return _ui_allMusic;
    }

    private MyMusicFolders get_ui_MusicFolders(){
        if(_ui_musicFolders ==null){
            _ui_musicFolders = new MyMusicFolders(_context , null);
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

}
