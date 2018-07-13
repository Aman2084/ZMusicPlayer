package com.zw.my.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.IntentActions;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayPosition;
import com.zw.my.adapter.MyListSongsAdapter;
import com.zw.my.ui.item.MySongItem;
import com.zw.ui.containers.SubPageTitle;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/11/13 22:46
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MySongListEditor extends ZRelativeLayout implements ISubpage {

    private ViewGroup _core_editTop;
    private ViewGroup _core_editBottom;
    private ViewGroup _core_commonBottom;

    private SubPageTitle _title;
    private EditText _input;
    private Button _btn_add;
    private ImageView _btn_delete;
    private ImageView _btn_ok;
    private ImageView _btn_edit;
    private ImageView _btn_deleteSongList;
    private CheckBox _check;

    private ListView _list_edit;
    private ListView _list_common;

    private SongsAdapter _ad_edit;
    private MyListSongsAdapter _ad_common;

    private SongList _data;

    public MySongListEditor(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.my_songlist_editor, this);

        _core_editTop = (ViewGroup) findViewById(R.id.core_editTop);
        _core_editBottom = (ViewGroup) findViewById(R.id.core_editBottom);
        _core_commonBottom = (ViewGroup) findViewById(R.id.core_commonBottom);

        _title = (SubPageTitle) findViewById(R.id.title);
        _input = (EditText) findViewById(R.id.input);
        _btn_add = (Button) findViewById(R.id.btn_add);
        _btn_delete = (ImageView) findViewById(R.id.btn_delete);
        _btn_ok = (ImageView) findViewById(R.id.btn_ok);
        _btn_edit = (ImageView) findViewById(R.id.btn_edit);
        _btn_deleteSongList = (ImageView) findViewById(R.id.btn_deleteSongList);
        _check = (CheckBox)findViewById(R.id.checkbox);

        _list_edit = (ListView) findViewById(R.id.list_edit);
        _list_common = (ListView) findViewById(R.id.list_common);

        _title.set_observable(this);

        _btn_add.setOnClickListener(onClick);
        _btn_delete.setOnClickListener(onClick);
        _btn_ok.setOnClickListener(onClick);
        _btn_edit.setOnClickListener(onClick);
        _btn_deleteSongList.setOnClickListener(onClick);
        _check.setOnCheckedChangeListener(onCheck);

        initList();
        initInput();
    }


    private class SongsAdapter extends MyListSongsAdapter{
        public SongsAdapter(ZObserver $listener) {
            super($listener);
        }

        @Override
        public View getView(int $position, View $convertView, ViewGroup $parent) {
            MySongItem item = (MySongItem)super.getView($position, $convertView, $parent);
            item.showBatch(true);
            return item;
        }

        public ArrayList<SongListItem> deleteSelectedSongs(){
            ArrayList<SongListItem> a = new ArrayList<>();
            for (int i = 0; i <_data.size() ; i++) {
                SongListItem item = _data.get(i);
                if(item.selected){
                    a.add(item);
                    _data.remove(i);
                    i--;
                }
            }
            if(a.size()>0){
                notifyDataSetChanged();
            }
            return a;
        }
    }

//init
    private void initList() {
        _ad_edit = new SongsAdapter(new ZObserver() {
            @Override
            public void onNotification(ZNotification $n) {
                onItem_edit($n);
            }
        });

        _ad_common = new MyListSongsAdapter(new ZObserver() {
            @Override
            public void onNotification(ZNotification $n) {
                onItem_common($n);
            }
        });

        _list_edit.setAdapter(_ad_edit);
        _list_common.setAdapter(_ad_common);
    }

    private void initInput() {
        //防止回车换行
        _input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView $v, int $actionId, KeyEvent $e) {
                boolean b = $e!=null && $e.getKeyCode()!=KeyEvent.KEYCODE_ENTER;
                return b;
            }
        });
    }

//Listeners

    private OnClickListener onClick = new OnClickListener() {

        @Override
        public void onClick(View $v) {
            String type = null;
            Object data = null;

            switch ($v.getId()){
                case R.id.btn_add:
                    type = ZNotifcationNames.Add;
                    break;
                case R.id.btn_delete:
                    ArrayList<SongListItem> a = _ad_edit.deleteSelectedSongs();
                    set_checked(false);
                    data = a;
                    _ad_common.notifyDataSetChanged();
                    type = a.size()>0 ? ZNotifcationNames.DeleteItem : null;
                    break;
                case R.id.btn_ok:
                    set_isEdit(false);
                    refuseTitle();
                    type = ZNotifcationNames.Complete;
                    break;
                case R.id.btn_deleteSongList:
                    type = ZNotifcationNames.Delete;
                    break;
                case R.id.btn_edit:
                    set_isEdit(true);
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
            set_checked($isChecked);
        }
    };

    private void onItem_common(ZNotification $n) {
        SongListItem s = (SongListItem)$n.data;
        PlayPosition p = new PlayPosition(_data.id , s.relationId);
        ZLocalBroadcast.sendAppIntent(IntentActions.PrePlaySongList , p);
    }

    private void onItem_edit(ZNotification $n) {
        switch ($n.name){
            case ZNotifcationNames.Selected:
                if((boolean)$n.data==false){
                    _check.setChecked(false);
                }
                break;
        }
    }

//logic
    /**
     * 同步歌单名
     * */
    private void refuseTitle(){
        String s = getTitle();
        if(s!=null && s!=_data.title){
            _title.set_text(s);
        }
    }


//interface
    public void setData(SongList $l) {
        _data = $l;
        ArrayList<SongListItem> a = MySongModel.CloneListSong(_data.items);

        _ad_common.setData(a);
        _ad_edit.setData(a);
        _title.set_text(_data.title);
        _input.setText(_data.title);
    }

    public void close() {
        sendNotification(ZNotifcationNames.Close);
    }

//getter and setter
    private void set_isEdit(boolean $b){
        int common = View.GONE;
        int edit = View.GONE;
        if($b){
            edit = View.VISIBLE;
        }else{
            common = View.VISIBLE;
        }
        _core_commonBottom.setVisibility(common);
        _list_common.setVisibility(common);
        _core_editTop.setVisibility(edit);
        _core_editBottom.setVisibility(edit);
        _list_edit.setVisibility(edit);
    }

    public String getTitle(){
        String s = _input.getText().toString();
        return s;
    }

    private void set_checked(boolean $b){
        int n = _list_edit.getChildCount();
        for (int i = 0; i <n ; i++) {
            MySongItem item = (MySongItem)_list_edit.getChildAt(i);
            if(item!=null){
                item.setSelected($b);
            }
        }

        if(_data!=null){
            for (SongListItem s:_data.items) {
                s.selected = $b;
            }
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
