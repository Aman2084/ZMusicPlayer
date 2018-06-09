package com.zw.main;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.ui.containers.ZFragment;
import com.aman.utils.UIUtils;
import com.aman.utils.observer.ZNotifcationNames;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongList;
import com.zw.ui.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * 底部音乐烂
 */
public class MainMusicFragment extends ZFragment {

    private CircleImageView _icon;
    private ImageView _btn_next;
    private ImageView _btn_play;
    private TextView _txt_song;
    private TextView _txt_singer;

    private boolean _isPlay = true;

    public MainMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_music, container, false);
        _icon = (CircleImageView)v.findViewById(R.id.icon);
        _btn_next = (ImageView)v.findViewById(R.id.btn_next);
        _btn_play = (ImageView)v.findViewById(R.id.btn_play);
        _txt_song = (TextView)v.findViewById(R.id.txt_song);
        _txt_singer = (TextView)v.findViewById(R.id.txt_singer);

        int[] a = {R.id.icon , R.id.btn_next , R.id.btn_play};
        UIUtils.setOnClickByIds((ViewGroup) v , a , onClick);
        v.setOnClickListener(onClick);

        return v;
    }

//Listener
    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View $v) {
            String s = null;
            switch ($v.getId()){
                case R.id.btn_play:
                    s = _isPlay ? ZNotifcationNames.Pause : ZNotifcationNames.Play;
                    break;
                case R.id.btn_next:
                    s = ZNotifcationNames.Next;
                    break;
                default:
                    s = ZNotifcationNames.Show;
                    break;
            }

            if(s!=null){
                sendNotification(s);
            }
        }
    };

//interface
    public void refuse_song(){
        SongList l = AppInstance.model.song.songList.list_play;
        if(l.getSongNum()>0){
            int index = AppInstance.model.play.index;
            Song s = l.getSongByIndex(index);
            if(s==null){
                index ++;
            }

            Bitmap b = s.getBmp(AppInstance.mainActivity);
            if(b!=null){
                _icon.setImageBitmap(b);
            }else{
                _icon.setImageResource(R.drawable.main_music_defaulticon);
            }

            _txt_song.setText(s.getDisplayName());
            _txt_singer.setText(s.getDisplaySinger());
        }else{
            _txt_song.setText("");
            _txt_singer.setText("");
        }
    }


    public void setPlaying(boolean $b){
        if(_isPlay==$b){
            return;
        }
        _isPlay = $b;
        int img = _isPlay ? R.drawable.main_music_pausebtn : R.drawable.main_music_playbtn;
        _btn_play.setImageResource(img);
    }
}
