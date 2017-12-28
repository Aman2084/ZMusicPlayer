package com.zw.main;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.aman.ui.containers.subPage.SubPageManager;
import com.aman.utils.message.ZIntent;
import com.aman.utils.observer.Mediator;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.AppNotificationNames;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/20 12:36
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MainMediator extends Mediator {

    private SubPageManager _svm_second;
    private SubPageManager _svm_third;

    public MainMediator(Context $c){
        super($c);
        init();
    }

//init
    private void init() {
        _svm_second = new SubPageManager(AppInstance.layer_second);
        _svm_third = new SubPageManager(AppInstance.layer_third);

        AppInstance.MainUI_my.addObserver(onMy);
    }

//Listeners
    private ZObserver onMy = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case AppNotificationNames.PlaySongList:
                    sendIntent(IntentActions.PlaySongList , $n.data);
                    break;
                case AppNotificationNames.EditSongList:
                    sendIntent(IntentActions.EditSongList , $n.data);
                    break;
            }
        }
    };



//LocalBroadcast
    @Override
    protected String[] getLocalIntentActions() {
        String[] a = {
            IntentActions.ShowSecondSubPage
            ,IntentActions.ShowThirdSubPage
            ,IntentActions.SongList_Creat
            ,IntentActions.SongList_Delete
            ,IntentActions.SongList_UpData
        };
        return a;
    }

    @Override
    protected void receiverLocalBroadcast(Context $context, Intent $intent) {
        Object o = ((ZIntent)$intent).data;
        switch ($intent.getAction()){
            case IntentActions.ShowSecondSubPage:
                _svm_second.show((View)o);
                break;
            case IntentActions.ShowThirdSubPage:
                _svm_third.show((View)o);
                break;
            case IntentActions.SongList_Creat:
            case IntentActions.SongList_Delete:
            case IntentActions.SongList_UpData:
                AppInstance.MainUI_my.refuse();
                break;
        }
    }
}
