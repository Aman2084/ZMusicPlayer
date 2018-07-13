package com.zw.main;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.aman.ui.containers.subPage.SubPageManager;
import com.aman.utils.message.ZIntent;
import com.aman.utils.observer.Mediator;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.MainActivity;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.IntentNotice;
import com.zw.global.model.data.SongList;
import com.zw.global.model.music.PlayPosition;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/20 12:36
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MainMediator extends Mediator {

    private SubPageManager _svm_second;
    private SubPageManager _svm_third;

    private MainNavigationFragment _fragment_navigation;
    private MainContainerFragment _fragment_main;
    private MainMusicFragment _fragment_music;


    public MainMediator(Context $c){
        super($c);
        FragmentManager m = ((MainActivity) $c).getFragmentManager();
        _fragment_music = (MainMusicFragment) m.findFragmentById(R.id.mainMusic);
        _fragment_main = (MainContainerFragment) m.findFragmentById(R.id.mainContainer);
        _fragment_navigation = (MainNavigationFragment)m.findFragmentById(R.id.mainNavigation);

        _svm_second = new SubPageManager(AppInstance.layer_second);
        _svm_third = new SubPageManager(AppInstance.layer_third);

        init();
    }

    @Override
    public void destroy() {
        super.destroy();
        _svm_second.clear();
        _svm_third.clear();
        _fragment_main.deleteObserver(onMain);
        AppInstance.MainUI_my.deleteObserver(onMy);
        _fragment_music.deleteObserver(onMusic);
    }

//init

    private void init() {
        _fragment_main.init();
        AppInstance.MainUI_my.addObserver(onMy);
        _fragment_main.addObserver(onMain);
        _fragment_music.addObserver(onMusic);

        boolean isPlay = AppInstance.model.play.isPlaying;
        _fragment_music.setPlaying(isPlay);
        _fragment_music.refuse_song();
    }

//Listeners

    private ZObserver onMain = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case ZNotifcationNames.Selected:
                    int i = (int)$n.data;
                    _fragment_navigation.setSelectedIndex(i);
                    break;
            }
        }
    };

    private ZObserver onMusic = new ZObserver() {
        @Override
            public void onNotification(ZNotification $n) {
            String s = null;
            switch ($n.name){
                case ZNotifcationNames.Pause:
                    s = IntentActions.Pause;
                    break;
                case ZNotifcationNames.Play:
                    s = IntentActions.Play;
                    break;
                case ZNotifcationNames.Next:
                    s = IntentActions.PlayNext;
                    break;
                case ZNotifcationNames.Show:
                    s = IntentActions.ShowPlayPage;
                    break;
            }

            if(s!=null){
                sendIntent(s);
            }

        }
    };

    private ZObserver onMy = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
        switch ($n.name){
            case AppNotificationNames.EditSongList:
                sendIntent(IntentActions.EditSongList , $n.data);
                break;
            case AppNotificationNames.PlaySongList:
                SongList l = (SongList)$n.data;
                if(l.items.size()>0){
                    int relation = l.items.get(0).relationId;
                    PlayPosition p = new PlayPosition(l.id , relation , 0);
                    sendIntent(IntentActions.PrePlaySongList , p);
                }
                break;
        }
        }
    };


//LocalBroadcast
    @Override
    protected String[] getActions_application() {
        String[] a = {
            IntentActions.ShowSecondSubPage
            ,IntentActions.ShowThirdSubPage
            ,IntentActions.ShowPlayPage
            ,IntentActions.Import_SetUI
            ,IntentNotice.SongList_Creat
            ,IntentNotice.SongList_Delete
            ,IntentNotice.SongList_UpData
            ,IntentNotice.MusicStart
            ,IntentNotice.MusicPause
            ,IntentNotice.MusicComplete
            ,IntentNotice.MusicListComplete
            ,IntentNotice.MusicStop
            ,IntentNotice.MusicError


        };
        return a;
    }

    @Override
    protected void receiveIntent(Context $context, Intent $intent) {
        Object o = ((ZIntent)$intent).data;
        switch ($intent.getAction()){
            case IntentActions.ShowSecondSubPage:
                _svm_second.show((View)o);
                break;
            case IntentActions.ShowThirdSubPage:
                _svm_third.show((View)o);
                break;
            case IntentNotice.SongList_Creat:
            case IntentNotice.SongList_Delete:
            case IntentNotice.SongList_UpData:
                AppInstance.MainUI_my.refuse();
                break;

            case IntentNotice.MusicStart:
                _fragment_music.setPlaying(true);
                _fragment_music.refuse_song();
                break;
            case IntentNotice.MusicStop:
            case IntentNotice.MusicPause:
            case IntentNotice.MusicListComplete:
                _fragment_music.setPlaying(false);
                break;
            case IntentNotice.MusicComplete:
            case IntentNotice.MusicError:

                break;
            case IntentActions.Import_SetUI:
                refuseByImport();
                break;
        }
    }

    private void refuseByImport() {
        _fragment_music.refuse_song();
    }
}
