package com.zw.music.ui.pager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayProgress;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/29 1:41
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongAdapter extends PagerAdapter {


    private ArrayList<SongListItem> _data;
    private ZObserver _observer;

    private ArrayList<MusicSongPagerItem> _arr_item;


    public MusicSongAdapter(ZObserver $o){
        _observer = $o;
        _arr_item = new ArrayList<>();
    }

//Adapter
    @Override
    public int getCount() {
        if(_data==null || _data.size()<2){
            return 1;
        }

        int n = _data.size();
        return n;
    }

    @Override
    public boolean isViewFromObject(View $view, Object $object) {
        return $view==$object;
    }

    @Override
    public Object instantiateItem(ViewGroup $c, int $position) {
        MusicSongPagerItem item = null;
        for (int i = 0; i <_arr_item.size() ; i++) {
            MusicSongPagerItem o = _arr_item.get(i);
            if(!o.isLive){
                item = o;
                break;
            }
        }
        if(item==null){
            item = new MusicSongPagerItem(AppInstance.mainActivity , null);
            item.addObserver(_observer);
            _arr_item.add(item);
        }

        PlayProgress g = new PlayProgress(0 , 0);
        if(_data!=null && _data.size()>0){
            int i = $position;
            SongListItem s = _data.get(i);
            item.setData(s);
            SongListItem cs = AppInstance.model.getCurrectSongListItem();
            if(cs!=null && cs.relationId==s.relationId){
                PlayModel m = AppInstance.model.play;
                g.setData(m.progress);
            }else{
                g.duration = s.song.getDuration();
            }
            s.sendNotification(ZNotifcationNames.Progress , g);
        }else{
            item.setData(null);
        }

        item.position = $position;
        item.isLive = true;

        $c.addView(item);
        item.postInvalidate();
        return item;
    }

    @Override
    public void destroyItem(ViewGroup $c, int $position, Object $o) {
        MusicSongPagerItem item = null;
        for (int i = 0; i <_arr_item.size() ; i++) {
            MusicSongPagerItem o = _arr_item.get(i);
            if(o.position==$position){
                item = o;
                break;
            }
        }

        if(item!=null && item.isLive){
            item.isLive = false;
            $c.removeView(item);
        }
    }

//interface

    public void setData(ArrayList<SongListItem> $a){
        _data = $a;
        notifyDataSetChanged();
    }


    public void clear(){

    }

    public MusicSongPagerItem getItemByPosition(int $position) {
        MusicSongPagerItem item = null;
        for (MusicSongPagerItem o:_arr_item) {
            if(o.position==$position){
                item = o;
                break;
            }
        }
        return item;
    }

    public SongListItem getDataByPosition(int $position) {
        if(_data==null || _data.size()==0){
            return null;
        }
        $position %= _data.size();
        return _data.get($position);
    }
}
