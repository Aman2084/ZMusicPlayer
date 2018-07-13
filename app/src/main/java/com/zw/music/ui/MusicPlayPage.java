package com.zw.music.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.ui.containers.subPage.SubPageManager;
import com.aman.utils.UIUtils;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.my.SongListModel;
import com.zw.global.model.music.SongMenu;
import com.zw.music.ui.list.MusicSongList;
import com.zw.music.ui.pager.MusicSongPager;
import com.zw.ui.containers.SubPageTitle;
import com.zw.utils.AppUtils;

import java.util.ArrayList;

import static com.zw.global.AppInstance.model;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/22 10:55
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicPlayPage extends ZRelativeLayout implements ISubpage {

    private String _songId;

    private SubPageManager _manager;

    private SubPageTitle _title;
    private MusicSongPager _pager;

    private ImageButton _btn_fav;
    private ImageButton _btn_model;
    private ImageButton _btn_loop;

    private TextView _txt_title;
    private TextView _txt_singer;

    private FrameLayout _core_list;
    private MusicSongList _list;
    private ArrayList<SongListItem> _data = new ArrayList<>();

    public MusicPlayPage(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.music_playpage , this);

        _title = (SubPageTitle)this.findViewById(R.id.title);
        _title.set_observable(this);

        _txt_title = (TextView) this.findViewById(R.id.txt_title);
        _txt_singer = (TextView) this.findViewById(R.id.txt_singer);

        _btn_fav = (ImageButton) findViewById(R.id.btn_fav);
        _btn_model = (ImageButton) findViewById(R.id.btn_mode);
        _btn_loop = (ImageButton) findViewById(R.id.btn_loop);

        _pager = (MusicSongPager) findViewById(R.id.pager);
        _core_list = (FrameLayout)findViewById(R.id.listCore);
        _list = new MusicSongList($c , null);

        _manager = new SubPageManager(_core_list);

        int[] a = {R.id.btn_fav , R.id.btn_mode , R.id.btn_loop , R.id.btn_list};
        UIUtils.setOnClickByIds(this , a , onClick);
        _pager.addObserver(onPager);
        _list.addObserver(onList);
        _core_list.setVisibility(GONE);

        refuse_playMode();
        refuse_playLoop();
    }

    @Override
    public void destroy() {
        super.destroy();
        _pager.deleteObservers();
        _list.deleteObservers();
        for (SongListItem item:_data) {
            item.deleteObservers();
        }
    }

//init


//Tools

    private void changeFavorite() {
        SongListItem item = _pager.getCurrentData();
        if(item!=null){
            item.song.isFavorite = !item.song.isFavorite;
            int id = item.song.isFavorite ? R.drawable.my_global_fav : R.drawable.my_global_unfav;
            _btn_fav.setBackgroundResource(id);

            SongListModel m = model.song.songList;
            m.setFavorite(item);
        }
    }

//Listener
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            String s = null;
            switch($v.getId()){
                case R.id.btn_fav:
                    changeFavorite();
                    break;
                case R.id.btn_mode:
                    s = AppNotificationNames.ChangPlayModel;
                    break;
                case R.id.btn_loop:
                    s = AppNotificationNames.ChangPlayLoop;
                    break;
                case R.id.btn_list:
                    showList();
                    break;
            }
            if(s!=null){
                sendNotification(s);
            }
        }
    };

    private ZObserver onPager = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch($n.name){
                case ZNotifcationNames.Change:
                    SongListItem s = (SongListItem) $n.data;
                    refuse(s);
                    break;
            }
        }
    };

    private ZObserver onList = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case ZNotifcationNames.Close:
                    _core_list.setVisibility(GONE);
                    break;
                case ZNotifcationNames.Clear:
                    clear();
                    break;
            }
        }
    };

//UI
    private void showList() {
        _core_list.setVisibility(VISIBLE);
        _manager.show(_list);
        int index = AppInstance.model.play.index;
        _list.setData(_data , index);
    }

    private void clear(){
        String s = AppUtils.id2String(R.string.global_unknown);
        _txt_title.setText(s);
        _txt_singer.setText(s);
        _pager.setVisibility(INVISIBLE);
        _list.clear();
    }

//interface
    public void  refuse(SongListItem $item){
        if($item.songId.equals(_songId)){
            return;
        }
        SongListModel m = AppInstance.model.song.songList;
        _songId = $item.songId;
        $item.song.isFavorite = m.isFavorite(_songId);
        int id = $item.song.isFavorite ? R.drawable.my_global_fav : R.drawable.my_global_unfav;
        _btn_fav.setBackgroundResource(id);
        _txt_title.setText($item.song.getDisplayName());
        _txt_singer.setText($item.song.getDisplaySinger());
    }

    public ArrayList<SongListItem> refuse_song(){
        SongList l = model.song.songList.list_play;
        ArrayList<SongListItem> a = null;
        int n = l.getSongNum();
        if(n>0){
            a = new ArrayList<>();
            PlayModel p = model.play;
            SongListItem item = l.getItemByIndex(p.index);
            refuse(item);

            for (int i = 0; i <n ; i++) {
                SongListItem s = l.getItemByIndex(i);
                s = s.clone();
                s.index = i;
                s.selected = i==p.index;
                a.add(s);
            }
            _data = a;
            _pager.setData(a , p.index);
            _pager.setVisibility(VISIBLE);
        }
        return a;
    }

    public void setData_index(int $inedx) {
        _pager.setData_index($inedx);
        if(_core_list.getVisibility()!=GONE){
            _list.updata($inedx);
        }
    }

    public void refuse_playMode(){
        PlayModel m = model.play;
        int id = 0;

        switch (m.playModel){
            case SongMenu.Order:
                id = R.drawable.music_btn_order;
                break;
            case SongMenu.Disorder:
                id = R.drawable.music_btn_disorder;
                break;
            case SongMenu.Single:
                id = R.drawable.music_btn_single;
                break;
        }
        _btn_model.setBackgroundResource(id);
    }

    public void refuse_playLoop(){
        PlayModel m = model.play;
        int id = m.isLoop ? R.drawable.music_btn_loop : R.drawable.music_btn_unloop;
        _btn_loop.setBackgroundResource(id);
    }

//getter and setter
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
