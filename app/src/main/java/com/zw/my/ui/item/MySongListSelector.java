package com.zw.my.ui.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.model.data.SongList;
import com.zw.my.adapter.MySongListsAdapter;
import com.zw.ui.containers.SubPageTitle;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/12/27 23:24
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MySongListSelector extends ZRelativeLayout implements ISubpage{

    private MySongListsAdapter _adapter;

    private ListView _list;
    private SubPageTitle _title;

    public MySongListSelector(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.my_songlist_selector , this);


        _title = (SubPageTitle) findViewById(R.id.title);
        _title.set_observable(this);
        _list = (ListView) findViewById(R.id.list);

        _adapter = new MySongListsAdapter(onItem);
        _list.setAdapter(_adapter);
    }

//Listeners

    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch($n.name){
                case ZNotifcationNames.Click:
                    sendNotification(ZNotifcationNames.Selected , $n.data);
                    break;
            }
        }
    };

//interface
    @Override
    public AnimationTypes get_showMovieType() {return AnimationTypes.None;}
    @Override
    public void set_showMovieType(AnimationTypes $type) {}
    @Override
    public AnimationTypes get_hideMovieType() {return AnimationTypes.None;}
    @Override
    public void set_hideMovieType(AnimationTypes $type) {}

    public void setData(ArrayList<SongList> $a){
        _adapter.setData($a);
    }

    public void close(){
        sendNotification(ZNotifcationNames.Close);
    }
}
