package com.zw.my.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.model.data.SongListItem;
import com.zw.my.ui.item.MySongItem;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/11/24 2:32
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MyListSongsAdapter extends BaseAdapter {

    protected ArrayList<SongListItem> _data = new ArrayList<>();

    private ZObserver _listener;

    public MyListSongsAdapter(ZObserver $listener) {
        super();
        _listener = $listener;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int $position) {
        return _data.get($position);
    }

    @Override
    public long getItemId(int $position) {
        return $position;
    }

    @Override
    public View getView(int $position, View $convertView, ViewGroup $parent) {
        MySongItem m = (MySongItem)$convertView;
        if(m==null){
            m = new MySongItem(AppInstance.mainActivity, _listener);
        }
        m.setData(_data.get($position));
        return m;
    }

    public void setData(ArrayList<SongListItem> $a){
        _data = $a;
        notifyDataSetChanged();
    }

    public void removeSong(SongListItem $s) {
        _data.remove($s);
        setData(_data);
    }

    public void removeSongs(ArrayList<SongListItem> $a) {
        _data.removeAll($a);
        setData(_data);
    }


    public ArrayList<SongListItem> getSeletedItems(){
        ArrayList<SongListItem> a = new ArrayList<>();
        for (int i = 0; i <_data.size() ; i++) {
            SongListItem s = _data.get(i);
            if(s.selected){
                a.add(s);
            }
        }
        return a;
    }

    public void clearSetetcted() {
        if(_data==null){
            return;
        }
        for (SongListItem s:_data) {
            s.selected = false;
        }
    }
}
