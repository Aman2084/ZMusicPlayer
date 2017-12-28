package com.zw.my.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.model.data.SongList;
import com.zw.my.ui.item.MySongListItem;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/12/27 23:39
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MySongListsAdapter extends BaseAdapter {

    private ZObserver _listener;
    private ArrayList<SongList> _data;


    public MySongListsAdapter(ZObserver $listener) {
        super();
        _listener = $listener;
    }

    @Override
    public int getCount() {
        int n = _data==null ? 0 : _data.size();
        return n;
    }

    public void setData(ArrayList<SongList> $a){
        _data = $a;
        notifyDataSetChanged();
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
        MySongListItem m = (MySongListItem)$convertView;
        if(m==null){
            m = new MySongListItem(AppInstance.mainActivity , _listener);
        }
        m.setData(_data.get($position));
        return m;
    }
}
