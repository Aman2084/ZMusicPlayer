package com.zw.music.ui.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.aman.media.ZAudioPlayer;
import com.aman.ui.controls.ZViewPager;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.MainApplication;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayProgress;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/27 1:32
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongPager extends ZViewPager {

    private class PlayTask extends TimerTask{
        public int relationId;
        public boolean waiting = false;

        @Override
        public void run() {
            waiting = false;
            ZLocalBroadcast.sendAppIntent(IntentActions.Jump2RelationId, relationId);
        }

        @Override
        public boolean cancel() {
            waiting = false;
            return super.cancel();
        }
    }


    private MusicSongAdapter _adapter;
    private Timer _timer;
    private PlayTask _task;

    public MusicSongPager(Context $c , AttributeSet $a) {
        super($c, $a);

        _adapter = new MusicSongAdapter(onItem);
        setAdapter(_adapter);

        addOnPageChangeListener(onPageChange);
    }

//Listener

    private OnPageChangeListener onPageChange = new OnPageChangeListener() {

        private boolean _hasScroll = false;

        @Override
        public void onPageScrolled(int $position, float $positionOffset, int $positionOffsetPixels) {
            SongListItem s = _adapter.getDataByPosition($position);
            if(s!=null){
                sendNotification(ZNotifcationNames.Change , s);
            }
        }

        @Override
        public void onPageSelected(int $position) {
            if(!_hasScroll){
                return;
            }

            _hasScroll = false;

            SongListItem s = _adapter.getDataByPosition($position);
            if(s!=null){
                sendNotification(AppNotificationNames.playSongListItem , s);

                if(_timer!=null){
                    _timer.cancel();
                    _task.cancel();
                }

                _timer = new Timer();
                _task = new PlayTask();
                _task.relationId = s.relationId;
                _timer.schedule(_task , 1000);
            }
        }

        @Override
        public void onPageScrollStateChanged(int $state) {
            if($state==ViewPager.SCROLL_STATE_DRAGGING){
                _hasScroll = true;
            }else if($state==ViewPager.SCROLL_STATE_IDLE){
                _hasScroll = false;
                //TODO... 607  滑动定位...
            }
        }
    };

    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {

        }
    };

//interface
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
        //TODO... 607 setCurrentItem
    }

    public void setData(ArrayList<SongListItem> $a, int $index) {
        _adapter.setData($a , $index);
        int i = $a.size()*300 + $index;
        setCurrentItem(i);
    }


    public void setData_progress(PlayProgress $p) {
        MusicSongPagerItem item = getCurrentPagerItem();
        if(item!=null){
            item.setData_progress($p);
        }
    }

    public void setData_play(SongListItem $item) {
        ZAudioPlayer player = getPlayer();
        int index = _adapter.getIndexByRelationId($item.relationId);
        int oldIndex = getCurrentItem();
        if(index!=oldIndex){
            setCurrentItem(index);
        }
        MusicSongPagerItem item = getCurrentPagerItem();
        if(item==null){
            return;
        }

        PlayProgress p = new PlayProgress($item.song.getDuration() ,  player.getCurrentPosition());
        setData_progress(p);
        item.play();
    }

    public void pause() {
        MusicSongPagerItem item = getCurrentPagerItem();
        if(item==null){
            return;
        }
        item.pause();
    }

//getter and setter

    private ZAudioPlayer getPlayer(){
        return MainApplication.getInstance().player;
    }

    private MusicSongPagerItem getCurrentPagerItem(){
        int p = this.getCurrentItem();
        MusicSongPagerItem item = _adapter.getItemByPosition(p);
        return item;
    }

    public SongListItem getCurrentData(){
        int i = this.getCurrentItem();
        SongListItem o = _adapter.getDataByPosition(i);
        return o;
    }

}
