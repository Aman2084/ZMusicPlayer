package com.zw.music.ui.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.model.data.SongListItem;

import java.util.ArrayList;

/**
 * 播放界面歌曲列表数内容提供器
 * ZMusicPlayer 1.0
 * Created on 2018/6/8 14:44
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongListAdapter extends BaseAdapter {

    private ZObserver _listener;
    private ArrayList<SongListItem> _data;

    private MusicSongListItem_Frist _item_frist;



    public MusicSongListAdapter(ZObserver $itemListener) {
        super();
        _listener = $itemListener;
    }

//Adapter
    @Override
    public int getCount() {
        int i = 0;
        if(_data!=null &&  _data.size()>0){
            i = _data.size() + 1;
        }
        return i;
    }

    @Override
    public Object getItem(int $position) {
        SongListItem s = null;
        if(getCount()>0 && $position>0){
            s = _data.get($position-1);
        }
        return s;
    }

    @Override
    public long getItemId(int $position) {
        return $position;
    }

    @Override
    public View getView(int $position, View $view, ViewGroup $parent) {
        if($position==0){
            if(_item_frist==null){
                _item_frist = new MusicSongListItem_Frist(AppInstance.mainActivity, null);
                _item_frist.addObserver(_listener);
            }
            return _item_frist;
        }
        MusicSongListItem s;
        if($view==null || !($view instanceof MusicSongListItem)){
            s = new MusicSongListItem(AppInstance.mainActivity, null);
            s.addObserver(_listener);
        }else {
            s = (MusicSongListItem)$view;
        }
        SongListItem item = (SongListItem) getItem($position);
        s.setData(item);
        return s;
    }

//interface

    public void setData(ArrayList<SongListItem> $a){
        _data = $a;
        this.notifyDataSetChanged();
    }

}
