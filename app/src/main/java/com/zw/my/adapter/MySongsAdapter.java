package com.zw.my.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.model.data.Song;
import com.zw.my.ui.item.MySongItem;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/31 3:56
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MySongsAdapter extends BaseAdapter{


    protected ArrayList<Song> _data = new ArrayList<>();

    private ZObserver _listener;

    public MySongsAdapter(ZObserver $listener) {
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

    public void setData(ArrayList<Song> $a){
        _data = $a;
        notifyDataSetChanged();
    }
}
