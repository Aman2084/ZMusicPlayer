package com.zw.global.model;

import android.util.Log;

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

    private static AppModel _instance = null;

    public MySongModel song = null;
    public PlayModel play = null;
    public UIModel ui;
    public SettingsModel settings;

    public AppModel() throws Exception{
        if(_instance!=null){
            throw new Exception("Singleton");
        }
        _instance = this;
        ui = new UIModel();
        settings = new SettingsModel();
        song = new MySongModel();
        play = new PlayModel();
    }

    public SongListItem getCurrectSongListItem(){
        SongListItem item = null;
        SongList l = song.songList.list_play;
        item = l.getItemByIndex(play.index);
        return item;
    }

    public static AppModel getInstance(){
        if(_instance ==null){
            try{
                _instance = new AppModel();
            }catch (Exception $e){
                Log.e("====" , "error");
            }
        }
        return _instance;
    }
}
