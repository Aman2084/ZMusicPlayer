package com.zw.music.ui.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.aman.ui.controls.ZViewPager;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongListItem;

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

    private ArrayList<SongListItem> _data;
    private int _index;

    private MusicSongAdapter _adapter;
    private Timer _timer;
    private PlayTask _task;


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
                if(_timer!=null){
                    _timer.cancel();
                    _task.cancel();
                }

                if(s.stause!=SongListItem.Play){
                    _timer = new Timer();
                    _task = new PlayTask();
                    _task.relationId = s.relationId;
                    _timer.schedule(_task , 1000);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int $state) {
            if($state==ViewPager.SCROLL_STATE_DRAGGING){
                _hasScroll = true;
            }else if($state==ViewPager.SCROLL_STATE_IDLE){
                _hasScroll = false;

                int position = getCurrentItem();
                if(position==0){
                    setCurrentItem(_data.size()-2 , false);
                }else if(position==(_data.size()-1)){
                    setCurrentItem(1 , false);
                }
            }
        }
    };

    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {

        }
    };

//Tools
    private void refuse() {
        if(_data==null || _data.size()==0){
            return;
        }
        _adapter.setData(_data);
        setCurrentItem(_index+1 , false);
    }

//interface

    public void setData(ArrayList<SongListItem> $a, int $index) {
        _data = new ArrayList<>();
        _data.addAll($a);
        SongListItem s = _data.get(0);
        _data.add(s);
        s = _data.get(_data.size()-2);
        _data.add(0 , s);
        _index = $index;
        refuse();
    }

    public void setData_index(int $index) {
        _index = $index;
        setCurrentItem(_index+1 , false);
    }

    public void clear() {
        _data = new ArrayList<>();
        _adapter.setData(_data);
    }

//getter and setter

    public SongListItem getCurrentData(){
        SongListItem o = _data.get(_index);
        return o;
    }

}
