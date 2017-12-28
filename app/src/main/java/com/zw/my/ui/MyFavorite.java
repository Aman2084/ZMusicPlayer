package com.zw.my.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.my.SongListModel;
import com.zw.my.adapter.MyListSongsAdapter;
import com.zw.my.ui.item.MySongItem;
import com.zw.ui.containers.SubPageTitle;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/12/9 10:34
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MyFavorite extends ZRelativeLayout implements ISubpage {

    private FavorteApader _apader;
    private ListView _list;
    private SubPageTitle _title;

    public MyFavorite(Context $c) {
        super($c, null);
        LayoutInflater.from($c).inflate(R.layout.my_favorite , this);

        _list = (ListView) findViewById(R.id.list);
        _title = (SubPageTitle) findViewById(R.id.title);
        _title.set_observable(this);

        SongListModel m = AppInstance.model.song.songList;
        ArrayList<SongListItem> a = m.list_fav.items;
        a = MySongModel.CloneListSong(a);
        _apader = new FavorteApader(onItem);
        _list.setAdapter(_apader);
        _apader.setData(a);
    }

//Observer
    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch($n.name){
                case AppNotificationNames.UNFavorite:
                    SongListItem s = (SongListItem)$n.data;
                    SongListModel m = AppInstance.model.song.songList;
                    s.isFavorite = false;
                    m.setFavorite(s);
                    _apader.removeSong(s);
                    break;
            }
        }
    };

    private class FavorteApader extends MyListSongsAdapter{
        public FavorteApader(ZObserver $listener) {
            super($listener);
        }

        @Override
        public View getView(int $position, View $convertView, ViewGroup $parent) {
            MySongItem item = (MySongItem)super.getView($position, $convertView, $parent);
            item.showFavorite();
            return item;
        }
    }


    @Override
    public AnimationTypes get_showMovieType() {
        return AnimationTypes.Right;
    }

    @Override
    public void set_showMovieType(AnimationTypes $type) {}

    @Override
    public AnimationTypes get_hideMovieType() {
        return AnimationTypes.Right;
    }

    @Override
    public void set_hideMovieType(AnimationTypes $type) {}
}
