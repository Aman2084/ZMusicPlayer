package com.zw.music;

import android.content.Context;
import android.content.Intent;

import com.aman.utils.message.ZIntent;
import com.aman.utils.observer.Mediator;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.IntentNotice;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayProgress;
import com.zw.music.progresses.MusicPlayPageProgress;

/**
 * ZmusicPlayer 1.0
 * Created on 2017/8/18 22:39
 * <p>
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicMediator extends Mediator {

    private MusicPlayPageProgress _playPage;

    public MusicMediator(Context $c) {
        super($c);
    }

//Intent
    @Override
    protected String[] getActions_application() {
        String[] a = new String[]{
            IntentActions.ShowPlayPage
            ,IntentNotice.PlayLoopChanged
            ,IntentNotice.PlayModelChanged
            ,IntentNotice.MusicStart
            ,IntentNotice.MusicPause
            ,IntentNotice.MusicProgress
        };
        return a;
    }

    @Override
    protected void receiveIntent(Context $context, Intent $intent) {
        ZIntent zt = null;
        if($intent instanceof ZIntent){
            zt = (ZIntent)$intent;
        }

        boolean b = _playPage!=null && _playPage.isShowing();

        switch ($intent.getAction()){
            case IntentActions.ShowPlayPage:
                getPlayPage().show();
                break;
            case IntentNotice.PlayModelChanged:
                if(b){
                    _playPage.refuse_model();
                }
                break;
            case IntentNotice.PlayLoopChanged:
                if(b){
                    _playPage.refuse_loop();
                }
                break;
            case IntentNotice.MusicProgress:
                if(b){
                    _playPage.setData_progress((PlayProgress)zt.data);
                }
                break;
            case IntentNotice.MusicStart:
                if(b){
                    SongListItem item = AppInstance.model.getCurrectSongListItem();
                    _playPage.setData_play(item);
                }
                break;
            case IntentNotice.MusicPause:
                if(b){
                    _playPage.pause();
                }
                break;
        }
    }


//Listener
    private ZObserver onPlayPage = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
        switch ($n.name){

        }
        }
    };

//Logic
    private MusicPlayPageProgress getPlayPage(){
        if(_playPage==null){
            _playPage = new MusicPlayPageProgress();
            _playPage.addObserver(onPlayPage);
        }
        return _playPage;
    }

}
