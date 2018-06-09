package com.zw.music.ui.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.utils.ZUtils;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.model.data.SongListItem;


/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/8 15:13
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongListItem extends ZRelativeLayout {

    private enum Status{Stop , Play , Pause}

    private Status _status = Status.Stop;

    private SongListItem _data;

    private TextView _txt_index;
    private TextView _txt_name;
    private TextView _txt_singer;
    private ImageView _img_play;
    private ImageView _img_pause;


    public MusicSongListItem(Context $c, AttributeSet $a) {
        super($c, $a);
        this.setGravity(CENTER_VERTICAL);
        LayoutInflater.from($c).inflate(R.layout.music_songlist_item , this);

        _txt_index = (TextView)findViewById(R.id.txt_index);
        _txt_name = (TextView)findViewById(R.id.txt_name);
        _txt_singer = (TextView)findViewById(R.id.txt_singer);
        _img_play = (ImageView)findViewById(R.id.img_play);
        _img_pause = (ImageView)findViewById(R.id.img_pause);

        this.setOnClickListener(onClick);
    }

//Tools
    private void setStatus(Status $s){
        if($s==_status){
            return;
        }
        _status = $s;
        int color = 0;
        switch (_status){
            case Stop:
                color = R.color.white;
                _img_pause.setVisibility(GONE);
                _img_play.setVisibility(GONE);
                _txt_index.setVisibility(VISIBLE);
                break;
            case Pause:
                color = R.color.gold;
                _img_pause.setVisibility(GONE);
                _img_play.setVisibility(VISIBLE);
                _txt_index.setVisibility(GONE);
                break;
            case Play:
                color = R.color.gold;
                _img_pause.setVisibility(VISIBLE);
                _img_play.setVisibility(GONE);
                _txt_index.setVisibility(GONE);
                break;
        }

        color = ContextCompat.getColor(getContext() , color);
        _txt_singer.setTextColor(color);
        _txt_name.setTextColor(color);
    }

//Listener
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            String s = null;
            switch (_status){
                case Stop:
                    s = ZNotifcationNames.Play;
                    break;
                case Pause:
                    s = ZNotifcationNames.Resume;
                    break;
                case Play:
                    s = ZNotifcationNames.Pause;
                    break;
            }
            sendNotification(s , _data);
        }
    };

    private ZObserver onSong = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case ZNotifcationNames.Play:
                    setStatus(Status.Play);
                    break;
                case ZNotifcationNames.Pause:
                    setStatus(Status.Pause);
                    break;
                case ZNotifcationNames.Stop:
                    setStatus(Status.Stop);
                    break;
            }
        }
    };

//interface

    public void setData(SongListItem $s){
        if(_data!=null){
            _data.deleteObserver(onSong);
        }
        _data = $s;
        if(_data==null){
            return;
        }
        _data.addObserver(onSong);

        _txt_index.setText(ZUtils.int2Str((_data.index+1) , 2));
        _txt_name.setText(_data.song.getDisplayName());
        _txt_singer.setText(_data.song.getDisplaySinger());
        switch(_data.stause){
            case SongListItem.Play:
                setStatus(Status.Play);
                break;
            case SongListItem.Pause:
                setStatus(Status.Pause);
                break;
            case SongListItem.Stop:
                setStatus(Status.Stop);
                break;
        }
    }
}

