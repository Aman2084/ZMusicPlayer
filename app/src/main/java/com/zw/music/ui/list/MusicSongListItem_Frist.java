package com.zw.music.ui.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.utils.observer.ZNotifcationNames;
import com.zw.R;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/8 15:15
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongListItem_Frist extends ZRelativeLayout {

    public MusicSongListItem_Frist(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.music_songlist_itemfrist , this);
        this.setOnClickListener(onClick);
    }


    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            sendNotification(ZNotifcationNames.Clear);
        }
    };
}
