package com.zw.music.ui.pager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.model.data.SongListItem;
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
    private ArrayList<View> _views;
    private ZObserver _observer;

    private int _index = 100000;

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

        int n = 600000;
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
            int i = $position % _data.size();
            SongListItem s = _data.get(i);
            item.setData(s);
            g.duration = s.song.getDuration();
        }else{
            item.setData(null);
        }
        item.setData_progress(g);

        item.position = $position;
        item.isLive = true;

        $c.addView(item);
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

    public void setData(ArrayList<SongListItem> $a , int $index){
        _index = $index;
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

    public int getIndexByRelationId(int $relationId){
        int index = -1;
        for (int i = 0; i <_data.size() ; i++) {
            SongListItem item = _data.get(i);
            if(item.relationId==$relationId){
                index = 300 * _data.size() +i;
                break;
            }
        }
        return index;
    }

    public SongListItem getDataByPosition(int $position) {
        if(_data==null || _data.size()==0){
            return null;
        }
        $position %= _data.size();
        return _data.get($position);
    }
}
