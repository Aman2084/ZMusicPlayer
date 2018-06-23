package com.zw.my.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aman.ui.containers.ZLinearLayout;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongList;
import com.zw.my.ui.item.MyMainItem;
import com.zw.my.ui.item.MySongListItem;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/8 10:35
 *
 * @author Aman
 * @Email 1390792438@qq.com
 * "我的音乐"一级界面
 */

public class MyMainContent extends ZLinearLayout {

    private final RelativeLayout _btn_addList;
    private MyMainItem _item_all;
    private MyMainItem _item_fav;

    private ListView _list;
    private MyMainAdater _adater;

    public MyMainContent(Context $c ,@Nullable AttributeSet $attrs){
        super($c , $attrs);

        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater.from($c).inflate(R.layout.my_main , this , true);

        _item_all = (MyMainItem) findViewById(R.id.item_all);
        _item_fav = (MyMainItem) findViewById(R.id.item_fav);
        _btn_addList = (RelativeLayout) findViewById(R.id.btn_add);
        _list = (ListView)findViewById(R.id.list);

        _item_all.setOnClickListener(onMainItem);
        _item_fav.setOnClickListener(onMainItem);
        _btn_addList.setOnClickListener(onMainItem);

        AbsListView.LayoutParams p = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT , AbsListView.LayoutParams.WRAP_CONTENT);
        ArrayList<View> a = new ArrayList<>();
        while (this.getChildCount()>1){
            View v = this.getChildAt(0);
            this.removeView(v);
            v.setLayoutParams(p);
            a.add(v);
        }
        _adater = new MyMainAdater(a, new ZObserver() {
            @Override
            public void onNotification(ZNotification $n) {
                onSongListItem($n);
            }
        });
        _list.setAdapter(_adater);
    }



    //Event Handler
    private OnClickListener onMainItem = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = null;
            if(v==_item_all){
                str = IntentActions.ShowMyAllMusic;
            }else if(v==_item_fav){
                str = IntentActions.ShowMyFavorites;
            }else if(v==_btn_addList){
                str = IntentActions.NewSongList;
            }
            if(str!=null){
                ZLocalBroadcast.sendAppIntent(str);
            }
        }
    };


    private void onSongListItem(ZNotification $n) {
        SongList l = null;
        if($n.data!=null && $n.data instanceof SongList){
            l = (SongList)$n.data;
        }
        switch ($n.name){
            case ZNotifcationNames.Click:
                sendNotification(AppNotificationNames.EditSongList, l);
                break;
            case ZNotifcationNames.LongClick:
                sendNotification(AppNotificationNames.PlaySongList, l);
                break;
        }
    }

//init
    public void refuse() {
        ArrayList<SongList> a = AppInstance.model.song.songList.get_allSongList();
        _adater.setData(a);
        int n = _list.getChildCount();
        for (int i = 0; i <n ; i++) {
            View v = _list.getChildAt(i);
//            v instanceof  ;
//            if(!())

        }
    }
}


class MyMainAdater extends BaseAdapter {

    private ZObserver _itemListener;
    private LinearLayout _core;
    private ArrayList<View> _arr_views;
    private ArrayList<SongList> _arr_data = null;

    public MyMainAdater(ArrayList<View> $a , ZObserver $listener) {
        super();
        _arr_views = $a;
        _itemListener = $listener;
    }

    @Override
    public int getCount() {
        int n = _arr_views.size();
        if(_arr_data!=null){
            n += _arr_data.size();
        }
        return n;
    }

    @Override
    public Object getItem(int $position) {
        Object o = null;
        int i = $position - _arr_views.size();
        if(i>-1 && _arr_data!=null &&_arr_data.size()>i){
            o = _arr_data.get(i);
        }
        return o;
    }

    @Override
    public long getItemId(int $position) {
        return $position;
    }

    @Override
    public View getView(int $position, View $convertView, ViewGroup $parent) {
        View v = null;
        int n = _arr_views.size();
        if($position<n){
            v = _arr_views.get($position);
        }else if(_arr_data!=null){
            int i = $position - n;
            if(i>-1 && i<_arr_data.size()){
                MySongListItem item = null;
                if($convertView!=null && $convertView instanceof MySongListItem){
                    v = $convertView;
                }
                if(v == null){
                    item = new MySongListItem(AppInstance.mainActivity , _itemListener);
                    item.setData(_arr_data.get(i));
                    v = item;
                }
            }
        }
        if(v==null){
            Log.e("MyMainContent===" , "出现  null");
        }
        return v;
    }

    public void setData(ArrayList<SongList> $a){
        _arr_data = $a;
        notifyDataSetChanged();
    }
}
