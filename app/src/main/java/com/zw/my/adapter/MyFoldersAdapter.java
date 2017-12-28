package com.zw.my.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zw.global.AppInstance;
import com.zw.my.ui.item.MyMusicFolderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observer;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/26 20:35
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MyFoldersAdapter extends BaseAdapter {

    private Observer _observer;

    private ArrayList<HashMap> _data;

    public MyFoldersAdapter(ArrayList<HashMap> $a , Observer $observer) {
        super();
        _data = $a;
        _observer = $observer;
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
        MyMusicFolderItem m = (MyMusicFolderItem)$convertView;
        if(m==null){
            m = new MyMusicFolderItem(AppInstance.mainActivity, null);
            m.addObserver(_observer);
        }
        m.setData(_data.get($position));
        return m;
    }
}
