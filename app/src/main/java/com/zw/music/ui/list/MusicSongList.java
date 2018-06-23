package com.zw.music.ui.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.ui.containers.subPage.AnimationTypes;
import com.aman.ui.containers.subPage.ISubpage;
import com.aman.ui.controls.Alert;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongListItem;
import com.zw.ui.containers.SubPageTitle;
import com.zw.utils.AppUtils;

import java.util.ArrayList;

import static com.zw.utils.AppUtils.id2String;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/8 1:26
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongList extends ZRelativeLayout implements ISubpage{

    private MusicSongListAdapter _adapter;

    private SubPageTitle _title;
    private ListView _list;
    private ArrayList<SongListItem> _data = new ArrayList<>();

    public MusicSongList(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.music_songlist , this);

        _title = (SubPageTitle)findViewById(R.id.title);
        _list = (ListView)findViewById(R.id.list);

        View bg = _title.findViewById(R.id.bg);
        _title.removeView(bg);
        _title.set_observable(this);

        int color = ContextCompat.getColor($c , R.color.white);
        _title.set_textColor(color);

        _adapter = new MusicSongListAdapter(onItem);
        _list.setAdapter(_adapter);

        TextView txt_close = (TextView) findViewById(R.id.txt_close);
        txt_close.setOnClickListener(onClick);
    }

//Listener
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
        if($v.getId()== R.id.txt_close){
            sendNotification(ZNotifcationNames.Close);
        }
        }
    };

    private ZObserver onItem = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            SongListItem item = null;
            if($n.data instanceof SongListItem){
                item = (SongListItem) $n.data;
            }

            String s = null;
            Object data = null;

            switch ($n.name){
                case ZNotifcationNames.Play:
                    s = IntentActions.Jump2RelationId;
                    data = item.relationId;
                    break;
                case ZNotifcationNames.Resume:
                    s = IntentActions.Play;
                    break;
                case ZNotifcationNames.Pause:
                    s = IntentActions.Pause;
                    break;
                case ZNotifcationNames.Clear:
                    clearSongList();
                    break;
            }

            if(s!=null){
                ZLocalBroadcast.sendAppIntent(s , data);
            }
        }
    };

//Tools
    private void clearSongList() {
        String s1 = AppUtils.id2String(R.string.music_list_clear);
        String s2 = AppUtils.id2String(R.string.music_list_ifclear);

        Alert.show(s1, s2, Alert.Yes | Alert.No, new ZObserver() {
            @Override
            public void onNotification(ZNotification $n) {
                if($n.action.equals(ZNotifcationNames.Yes)){
                    ZLocalBroadcast.sendAppIntent(IntentActions.ClearPlayList);
                    sendNotification(ZNotifcationNames.Clear);
                }
            }
        });
    }


//interface
    public void setData(ArrayList<SongListItem> $a, int $index) {
        _data = $a;
        _adapter.setData($a);
        updata($index);
    }

    public void updata(int $index){
        String s = id2String(R.string.music_list_title);
        if(_data.size()>0){
            s += "(%s/%s)";
            s = String.format(s , $index+1 , _data.size());
        }
        _title.set_text(s);
    }

    public void clear() {
        _data = new ArrayList<>();
        _adapter.setData(_data);
    }

//SubPage
    @Override
    public AnimationTypes get_showMovieType() {
        return AnimationTypes.Bottom;
    }
    @Override
    public void set_showMovieType(AnimationTypes $type) {}
    @Override
    public void set_hideMovieType(AnimationTypes $type) {}
    @Override
    public AnimationTypes get_hideMovieType() {
        return AnimationTypes.Bottom;
    }

}
