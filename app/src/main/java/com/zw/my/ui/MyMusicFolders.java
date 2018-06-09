package com.zw.my.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.aman.consts.ZKeys;
import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObservable;
import com.zw.R;
import com.zw.global.model.data.Song;
import com.zw.my.adapter.MyFoldersAdapter;
import com.zw.my.ui.item.MyMusicFolderItem;
import com.zw.ui.containers.SubPageTitle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/25 13:28
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MyMusicFolders extends ZRelativeLayout implements ISubpage , Observer {

    private MyFoldersAdapter _adapter;

    private Button _btn;
    private CheckBox _check;
    private ListView _list;

    private HashMap<String , ArrayList<Song>> _data = null;
    private ArrayList<HashMap> _listData;

    public MyMusicFolders(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.my_musicfloders , this);
        SubPageTitle t = (SubPageTitle)findViewById(R.id.title);
        t.set_observable(this);
        _list = (ListView)findViewById(R.id.list);
        _check = (CheckBox)findViewById(R.id.checkbox);
        _btn = (Button)findViewById(R.id.btn_do);
        _check.setOnCheckedChangeListener(onCheck);
        _btn.setOnClickListener(onClick);
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
    }

    private void complete() {
        int num = get_numSong();
        if(num==0){
            sendNotification(ZNotifcationNames.Close);
            return;
        }
        ArrayList<Song> arr = new ArrayList<>();
        for (HashMap h:_listData) {
            if((boolean)h.get(ZKeys.Selected)){
                ArrayList<Song> a = _data.get(h.get(ZKeys.Path));
                arr.addAll(a);
            }
        }
        sendNotification(ZNotifcationNames.Complete , arr);
        sendNotification(ZNotifcationNames.Close);
    }

//Listener
    @Override
    public void update(Observable $o, Object $arg) {
        ZObservable o = (ZObservable)$o;
        if(o==null){
            return;
        }
        boolean b = o.getOwner() instanceof MyMusicFolderItem;
        if(!b){
            return;
        }
        ZNotification n = (ZNotification) $arg;
        if(n.name== ZNotifcationNames.Selected){
            b = (Boolean) n.data;
            if(_check.isChecked() && !b){
                _check.setOnCheckedChangeListener(null);
                _check.setChecked(false);
                _check.setOnCheckedChangeListener(onCheck);
            }
        }
        refuse();
    }


    private CompoundButton.OnCheckedChangeListener onCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton $buttonView, boolean $isChecked) {
            if(_listData!=null){
                for (HashMap h:_listData) {
                    h.put(ZKeys.Selected , $isChecked);
                }
            }
            int n = _list.getChildCount();
            for (int i = 0; i <n ; i++) {
                MyMusicFolderItem item = (MyMusicFolderItem)_list.getChildAt(i);
                if(item!=null){
                    item.set_selected($isChecked);
                }
            }
            refuse();
        }
    };

    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            if($v==_btn){
                complete();
            }
        }
    };

//getter and setter

    /**
     * 设置数据
     *      整理数据结构
     *      设置ListView
     * @param $data 源数据
     *
     */
    public void setData(ArrayList<Song> $data) {
        //整理数据
        HashMap<String , ArrayList<Song>> m = new HashMap<>();
        ArrayList<String> a = new ArrayList<>();

        String key;
        for (Song s:$data) {
            key = s.getFolderPath();
            ArrayList<Song> l = m.get(key);
            if(l==null){
                l = new ArrayList<>();
                m.put(key , l);
                a.add(key);
            }
            l.add(s);
        }
        Collections.sort(a);

        _listData = new ArrayList<>(); //List数据
        for (int i = 0; i <a.size() ; i++) {
            key = a.get(i);
            ArrayList<Song> l = m.get(key);
            String[] ss = key.split("/");
            HashMap h = new HashMap();
            h.put(ZKeys.Path ,key);
            h.put("name" , ss[ss.length-1]);
            h.put(ZKeys.Num , l.size());
            h.put(ZKeys.Selected , false);
            _listData.add(h);
        }
        _data = m;

        _adapter = new MyFoldersAdapter(_listData , this);
        _list.setAdapter(_adapter);
        refuse();
    }

    private int get_numSong(){
        int num = 0;
        if(_listData!=null && _listData.size()>0){
            for (HashMap h:_listData) {
                if((boolean)h.get(ZKeys.Selected)){
                    num += (Integer) h.get(ZKeys.Num);
                }
            }
        }
        return num;
    }

    @Override
    public AnimationTypes get_showMovieType() {return AnimationTypes.Right;}
    @Override
    public void set_showMovieType(AnimationTypes $type) {}
    @Override
    public AnimationTypes get_hideMovieType() {return AnimationTypes.Right;}
    @Override
    public void set_hideMovieType(AnimationTypes $type) {}
}