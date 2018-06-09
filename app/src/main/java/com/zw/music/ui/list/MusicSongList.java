package com.zw.music.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.ui.containers.SubPageTitle;

import java.util.ArrayList;

import static com.zw.R.id.txt_close;

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
    private ArrayList<SongListItem> _data;

    public MusicSongList(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.music_songlist , this);

        _title = (SubPageTitle)findViewById(R.id.title);
        _list = (ListView)findViewById(R.id.list);

        View bg = _title.findViewById(R.id.bg);
        _title.removeView(bg);
        _title.set_observable(this);

        _adapter = new MusicSongListAdapter(onItem);
        _list.setAdapter(_adapter);

        TextView txt_close = (TextView) findViewById(R.id.txt_close);
        txt_close.setOnClickListener(onClick);
    }

//Listener
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            if($v.getId()== txt_close){
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
            }

            if(s!=null){
                ZLocalBroadcast.sendAppIntent(s , data);
            }
        }
    };

//interface

    public void refuse() {
        ArrayList<SongListItem> a = new ArrayList();
        SongList l = AppInstance.model.song.songList.list_play;
        for (int i = 0; i <l.getSongNum() ; i++) {
            SongListItem s = l.items.get(i).clone();
            s.index = i;
            a.add(s);
        }
        _data = a;
        _adapter.setData(a);
    }

    public void setPlayingRelationId(int $relationId){
        if(_data==null || _data.size()==0){
            return;
        }
        SongList l = new SongList();
        l.items = _data;
        SongListItem o1 = l.getPlayingItem();
        SongListItem o2 = l.getItemByRelationId($relationId);
        if(o1!=null && o1!=null){
            if(o1==o2){
                o1.stause = SongListItem.Play;
                o1.sendNotification(ZNotifcationNames.Play);
                return;
            }
        }

        if(o1!=null){
            o1.stause = SongListItem.Stop;
            o1.sendNotification(ZNotifcationNames.Stop);
        }
        if(o2!=null){
            o2.stause = SongListItem.Play;
            o2.sendNotification(ZNotifcationNames.Play);
        }
    }

    public void pause() {
        if(_data==null || _data.size()==0){
            return;
        }
        SongList l = new SongList();
        l.items = _data;
        SongListItem o = l.getPlayingItem();
        if(o!=null){
            o.stause = SongListItem.Pause;
            o.sendNotification(ZNotifcationNames.Pause);
        }
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
