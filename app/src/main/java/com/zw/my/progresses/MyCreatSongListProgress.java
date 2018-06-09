package com.zw.my.progresses;

import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.my.SongListModel;
import com.zw.my.ui.MySongListNameDoalog;
import com.zw.my.ui.MySongSelector;
import com.zw.utils.ZProgress;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/19 2:06
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MyCreatSongListProgress extends ZProgress{

    private int _id;

    public MyCreatSongListProgress() {
        super(null, null);
    }

    private MySongSelector _selecter = null;

//logic

    //Step1 显示歌曲名Dialog
    public void addSongList() {
        MySongListNameDoalog d = new MySongListNameDoalog(onDialog);
        d.show();
    }

    //Step2 确定歌单名（1.操作数据库 + 2.建立本地数据模型 + 3.弹出歌曲选择界面）
    private ZObserver onDialog = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            MySongModel m = AppInstance.model.song;
            if($n.name.equals(ZNotifcationNames.OK)){
                SongList l = m.songList.creatSongList((String) $n.data);
                _id = l.id;
                if(_selecter==null){
                    _selecter = new MySongSelector(AppInstance.mainActivity , null);
                    _selecter.addObserver(onSelecter);
                }
                ArrayList<SongListItem> a = MySongModel.Songs2SongListItems(m.song.get_allSongs());
                _selecter.setData(a);
                sendIntent(IntentActions.ShowThirdSubPage , _selecter);
            }
        }
    };

    //Step3 响应歌曲选择
    private ZObserver onSelecter = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            if(!$n.name.equals(ZNotifcationNames.Selected)){
                return;
            }
            ArrayList<SongListItem> arr = (ArrayList<SongListItem>)$n.data;
            ArrayList<Song> a = MySongModel.SongListItems2Songs(arr);
            SongListModel m = AppInstance.model.song.songList;
            m.addSong2List(_id , a);
            sendNotification(ZNotifcationNames.Complete);
        }
    };
}
