package com.zw.global.model;

import android.content.Context;

import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/30 11:43
 *
 * @author Aman
 * @Email 1390792438@qq.com
 * 数据模型总入库
 */

public class AppModel {

    public MySongModel song = null;
    public PlayModel play = null;
    public UIModel ui;
    public SettingsModel settings;

    public AppModel(Context $c){
        ui = new UIModel();
        settings = new SettingsModel();
        song = new MySongModel();
        play = new PlayModel($c);
    }

    public SongListItem getCurrectSongListItem(){
        SongListItem item = null;
        SongList l = song.songList.list_play;
        item = l.getItemByIndex(play.index);
        return item;
    }
}
