package com.zw.my.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.model.data.SongListItem;
import com.zw.my.adapter.MyListSongsAdapter;
import com.zw.my.adapter.MySongsAdapter;
import com.zw.my.ui.item.MySongItem;
import com.zw.ui.containers.SubPageTitle;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/31 3:15
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MySongSelector extends ZRelativeLayout implements ISubpage{

    private Button _btn;
    private CheckBox _check;
    private ListView _list;

    SongsAdapter _adapter;
    ArrayList<SongListItem> _listData = null;

    public MySongSelector(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.my_selectsongs , this);
        SubPageTitle t = (SubPageTitle)findViewById(R.id.title);
        t.set_observable(this);
        _list = (ListView)findViewById(R.id.list);
        _check = (CheckBox)findViewById(R.id.checkbox);
        _btn = (Button)findViewById(R.id.btn_do);
        _check.setOnCheckedChangeListener(onCheck);
        _btn.setOnClickListener(onClick);

        _adapter = new SongsAdapter(onItem);
        _list.setAdapter(_adapter);
    }

//logic

    private void refuse(){
        int num = get_numSong();
        if(num==0){
            _btn.setText(getContext().getString(R.string.global_cancel));
            _btn.setBackgroundResource(R.drawable.global_btnbg_gray);
        }else{
            String s = getContext().getString(R.string.my_importSong , Integer.toString(num));
            _btn.setText(s);
            _btn.setBackgroundResource(R.drawable.global_btnbg_orange);
        }
        _btn.refreshDrawableState();

        int n = _listData==null ? 0 : _listData.size();
        boolean b = num==n;
        _check.setOnCheckedChangeListener(null);
//        _check.setSelected(b);
        _check.setChecked(b);
        _check.setOnCheckedChangeListener(onCheck);
    }

//Listener

    private CompoundButton.OnCheckedChangeListener onCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton $buttonView, boolean $isChecked) {
            set_checked($isChecked);
            refuse();
        }
    };

    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            ArrayList<SongListItem> a = get_selectedSongs();
            String s = a.size()>0 ? ZNotifcationNames.Selected : ZNotifcationNames.Cancel;
            sendNotification(s , a);
            sendNotification(ZNotifcationNames.Close);
        }
    };

    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            if($n.name==ZNotifcationNames.Selected){
                refuse();
            }
        }
    };

//interface

    public void setData(ArrayList<SongListItem> $a){
        _listData = $a;
        set_checked(false);
        _adapter.setData($a);
        refuse();
    }

//getter and setter
    private void set_checked(boolean $b){
        int n = _list.getChildCount();
        for (int i = 0; i <n ; i++) {
            MySongItem item = (MySongItem)_list.getChildAt(i);
            if(item!=null){
                item.setSelected($b);
            }
        }

        if(_listData!=null){
            for (SongListItem s:_listData) {
                s.selected = $b;
            }
        }
    }

    private int get_numSong(){
        int num = 0;
        if(_listData!=null && _listData.size()>0){
            for (SongListItem s:_listData) {
                if(s.selected){
                    num ++;
                }
            }
        }
        return num;
    }

    private ArrayList<SongListItem> get_selectedSongs(){
        ArrayList<SongListItem> a = new ArrayList<>();
        for (SongListItem s:_listData) {
            if(s.selected){
                a.add(s);
            }
        }
        return a;
    }

    @Override
    public AnimationTypes get_showMovieType() {return AnimationTypes.Right;}
    @Override
    public void set_showMovieType(AnimationTypes $type) {}
    @Override
    public AnimationTypes get_hideMovieType() {return AnimationTypes.Right;}
    @Override
    public void set_hideMovieType(AnimationTypes $type) {}

    private class SongsAdapter extends MyListSongsAdapter {
        public SongsAdapter(ZObserver $listener) {
            super($listener);
        }

        public View getView(int $position, View $convertView, ViewGroup $parent) {
            MySongItem m = (MySongItem)super.getView($position , $convertView , $parent);
            m.showBatch(true);
            return m;
        }

        @Override
        public void setData(ArrayList<SongListItem> $a) {
            for (SongListItem s : $a) {
                s.selected = false;
            }
            super.setData($a);
        }
    }

    private class SongAdapter extends MySongsAdapter{

        public SongAdapter(ZObserver $listener) {
            super($listener);
        }

        @Override
        public View getView(int $position, View $convertView, ViewGroup $parent) {
            MySongItem s = (MySongItem)super.getView($position, $convertView, $parent);
            if(s!=null){
                s.showBatch(true);
            }
            return s;
        }
    }
}