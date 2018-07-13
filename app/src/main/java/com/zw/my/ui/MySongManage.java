package com.zw.my.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.utils.UIUtils;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.SongGroup;
import com.zw.global.model.data.SongListItem;
import com.zw.my.adapter.MyListSongsAdapter;
import com.zw.my.ui.item.MySongItem;
import com.zw.ui.containers.SubPageTitle;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/11/29 2:36
 *
 * @author Aman
 * @Email 1390792438@qq.com
 * 歌曲管理器
 */

public class MySongManage extends ZRelativeLayout implements ISubpage {

    private ManageAtapter _apater;
    private SubPageTitle _title;
    private View _bar_normal;
    private View _bar_edit;
    private View _bar_batch;
    private ListView _list;
    private CheckBox _check;

    private boolean _useBrowse = true;
    private boolean _checkByCode = false;
    private AnimationTypes _showType = AnimationTypes.Right;
    private ArrayList<SongListItem> _data;

    /**
     * 显示模式
     * Browse   浏览模式
     * Manage   单个管理模式
     * Batch    批量操作模式
     * NoBrowse 无浏览模式,并初始化显示为Manage模式
     * */
    public enum DisplayMode {
        Browse, Manage, Batch, NoBrowse
    }

    public MySongManage(Context $c) {
        super($c, null);
        LayoutInflater.from(AppInstance.mainActivity).inflate(R.layout.my_songmamage , this);

        _title = (SubPageTitle) findViewById(R.id.title);
        _title.set_observable(this);
        _bar_normal = findViewById(R.id.bar_normal);
        _bar_edit = findViewById(R.id.bar_edit);
        _bar_batch = findViewById(R.id.bar_batch);
        _list = (ListView)findViewById(R.id.list);
        _check = (CheckBox)findViewById(R.id.checkbox);

        _apater = new ManageAtapter(onItem);
        _list.setAdapter(_apater);

        int[] ids = {R.id.btn_manage,R.id.btn_play,R.id.btn_complete,
                R.id.btn_batch,R.id.btn_back,R.id.btn_delete,R.id.btn_add2list};
        UIUtils.setOnClickByIds(this , ids , onClick);
        _check.setOnCheckedChangeListener(onCheck);
    }

    private class ManageAtapter extends MyListSongsAdapter{

        private DisplayMode _mode = null;

        public ManageAtapter(ZObserver $listener) {
            super($listener);
        }

        public void setDisplayMode(DisplayMode $m){
            _mode = $m;
        }


        @Override
        public View getView(int $position, View $convertView, ViewGroup $parent) {
            MySongItem item =  (MySongItem)super.getView($position, $convertView, $parent);
            switch(_mode){
                case Manage:
                    item.showBtns(true);
                    break;
                case Batch:
                    item.showBatch(true);
                    break;
            }
            return item;
        }
    }


//Listener
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            Object data = null;
            String type = null;
            switch($v.getId()){
                case R.id.btn_back:
                case R.id.btn_manage:
                    changeDisplayMode(DisplayMode.Manage);
                    break;
                case R.id.btn_batch:
                    changeDisplayMode(DisplayMode.Batch);
                    break;
                case R.id.btn_complete:
                    if(_useBrowse){
                        changeDisplayMode(DisplayMode.Browse);
                    }else{
                        type = ZNotifcationNames.Close;
                    }
                    break;
                case R.id.btn_delete:
                    ArrayList<SongListItem> a = _apater.getSeletedItems();
                    if(a.size()>0){
                        _apater.removeSongs(a);
                        type = ZNotifcationNames.Delete;
                        data = a;
                    }
                    break;
                case R.id.btn_add2list:
                    a = _apater.getSeletedItems();
                    if(a.size()>0){
                        type = AppNotificationNames.Add2SongList;
                        data = a;
                    }
                    break;
                case R.id.btn_play:
                    play();
                    break;
            }

            if(type!=null){
                sendNotification(type , data);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton $buttonView, boolean $isChecked) {
            if(_checkByCode){
                _checkByCode = false;
                return;
            }

            if(_data==null || _data.isEmpty()){
                return;
            }

            for (SongListItem o:_data) {
                if(o.selected!=$isChecked){
                    o.selected = $isChecked;
                    o.sendNotification(ZNotifcationNames.Change);
                }
            }
        }
    };

    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            MySongItem item = (MySongItem)$n.owner;
            SongListItem o = null;
            if($n.data instanceof SongListItem){
                o = (SongListItem)$n.data;
            }
            switch ($n.name){
                case AppNotificationNames.Favorite:
                case AppNotificationNames.UNFavorite:
                    o.song.isFavorite = !o.song.isFavorite;
                    item.refuse_favorite();
                    sendNotification($n.name , $n.data);
                    break;
                case ZNotifcationNames.Delete:
                    _apater.removeSong(o);
                    ArrayList<SongListItem> a = new ArrayList<>();
                    a.add(o);
                    sendNotification(ZNotifcationNames.Delete , a);
                    break;
                case ZNotifcationNames.Click:
                    play(o);
                    break;
                case ZNotifcationNames.Selected:
                    boolean b = (Boolean)$n.data;
                    if(!b){
                        _checkByCode = true;
                        _check.setChecked(false);
                    }
                    break;
            }
        }
    };


//Logic

    private void play(){
        SongGroup g = new SongGroup();
        g.songs = MySongModel.SongListItems2Songs(_data);
        g.name = _title.get_text();
        ZLocalBroadcast.sendAppIntent(IntentActions.PrePlaySongs , g);
    }

    private void play(SongListItem $o){
        SongGroup g = new SongGroup();
        g.index = _data.indexOf($o);
        g.songs = MySongModel.SongListItems2Songs(_data);
        g.name = _title.get_text();
        ZLocalBroadcast.sendAppIntent(IntentActions.PrePlaySongs , g);
    }

    private void changeDisplayMode(DisplayMode $mode){
        _apater.setDisplayMode($mode);
        _bar_normal.setVisibility(View.GONE);
        _bar_edit.setVisibility(View.GONE);
        _bar_batch.setVisibility(View.GONE);
        switch($mode){
            case Browse:
                _bar_normal.setVisibility(View.VISIBLE);
                break;
            case Manage:
                _bar_edit.setVisibility(View.VISIBLE);
                break;
            case Batch:
                _bar_batch.setVisibility(View.VISIBLE);
                break;
        }
        for (int i = 0; i <_list.getChildCount() ; i++) {
            MySongItem item = (MySongItem)_list.getChildAt(i);
            item.showBtns(false);
            item.showBatch(false);
            switch($mode){
                case Manage:
                    item.showBtns(true);
                    break;
                case Batch:
                    item.showBatch(true);
                    break;
            }
        }
    }

//interface
    public void setDsiplayMode(DisplayMode $mode){
        if($mode==DisplayMode.NoBrowse){
            _useBrowse = false;
            changeDisplayMode(DisplayMode.Manage);
        }else{
            _useBrowse = true;
            changeDisplayMode($mode);
        }
    }

    public void setData(ArrayList<SongListItem> $o) {
        _data = $o;
        _apater.setData($o);
    }

    public void clearSetected() {
        _apater.clearSetetcted();
        int n = _list.getChildCount();
        for (int i = 0; i < n ; i++) {
            MySongItem item = (MySongItem)_list.getChildAt(i);
            item.setSelected(false);
        }
    }

    public void setTitle(String $str){
        _title.set_text($str);
    }

    @Override
    public AnimationTypes get_showMovieType() {
        return _showType;
    }
    @Override
    public AnimationTypes get_hideMovieType() {
        return AnimationTypes.Right;
    }
    @Override
    public void set_showMovieType(AnimationTypes $type) {
        _showType = $type;
    }
    @Override
    public void set_hideMovieType(AnimationTypes $type) {}
}
