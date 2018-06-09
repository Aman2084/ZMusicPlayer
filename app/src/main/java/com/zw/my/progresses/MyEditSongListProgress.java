package com.zw.my.progresses;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.aman.utils.UIUtils;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.IntentActions;
import com.zw.global.model.MySongModel;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongList;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.my.SongListModel;
import com.zw.my.ui.MySongListEditor;
import com.zw.my.ui.MySongSelector;
import com.zw.utils.AppUtils;
import com.zw.utils.ZProgress;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/11/11 12:00
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MyEditSongListProgress extends ZProgress {

    private MySongListEditor _editor;
    private SongList _data;
    private MySongSelector _selecter;

    public MyEditSongListProgress() {
        super(null, null);
    }

//logic
    private void writeData(){
        SongListModel m = AppInstance.model.song.songList;
        String s = _editor.getTitle();
        if(s!="" && s!=_data.title){
            m.updateSongList_title(_data.id , s);
        }
    }

    private void deleteSongs(ArrayList<SongListItem> $a) {
        SongListModel m = AppInstance.model.song.songList;
        m.deleteSongsFromeList(_data.id , $a);
    }

    private void delete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AppInstance.mainActivity);
        String s_ok = AppUtils.id2String(R.string.global_ok);
        String s_cancel = AppUtils.id2String(R.string.global_ok);
        String s_title = AppUtils.id2String(R.string.my_deleteSure);
        String s_msg = AppUtils.id2String(R.string.my_deleteListSure);
        s_msg = String.format(s_msg , _data.title);
        UIUtils.alert(AppInstance.mainActivity , s_title , s_msg ,s_ok ,s_cancel ,
                onDeleteDialog , null);
    }

    private void showSelecter() {
        if(_selecter==null){
            _selecter = new MySongSelector(AppInstance.mainActivity , null);
            _selecter.addObserver(onSelecter);
        }
        ArrayList<Song> a = AppInstance.model.song.song.get_allSongs();
        ArrayList<SongListItem> arr = MySongModel.Songs2SongListItems(a);
        _selecter.setData(arr);
        sendIntent(IntentActions.ShowThirdSubPage , _selecter);
    }

    private void clear(){
        if(_editor!=null){
            _editor.deleteObserver(onEditor);
            _editor = null;
        }
        if(_selecter!=null){
            _selecter.deleteObserver(onSelecter);
            _selecter = null;
        }
    }

//listener

    private ZObserver onEditor = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case ZNotifcationNames.Complete:
                    writeData();
                    break;
                case ZNotifcationNames.Delete:
                    delete();
                    break;
                case ZNotifcationNames.Close:
                    writeData();
                    clear();
                    break;
                case ZNotifcationNames.Add:
                    showSelecter();
                    break;
                case ZNotifcationNames.DeleteItem:
                    deleteSongs((ArrayList<SongListItem>)$n.data);
                    break;
            }
        }
    };


    private ZObserver onSelecter = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch($n.name){
                case ZNotifcationNames.Selected:
                    ArrayList<Song> a = (ArrayList<Song>)$n.data;
                    if(a.size()>0){
                        SongListModel m = AppInstance.model.song.songList;
                        ArrayList<SongListItem> arr = (ArrayList<SongListItem>)$n.data;
                        m.addSong2List(_data.id , MySongModel.SongListItems2Songs(arr));
                        _data = m.getSongListById(_data.id);
                        _editor.setData(_data);
                    }
                    break;
            }
        }
    };

    private AlertDialog.OnClickListener onDeleteDialog = new AlertDialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface $dialog, int $which) {
            SongListModel m = AppInstance.model.song.songList;
            m.deleteSongList(_data.id);
            _editor.close();
        }
    };

//interface
    public void show(SongList $list) {
        _data = $list;
        _editor = new MySongListEditor(AppInstance.mainActivity , null);
        _editor.addObserver(onEditor);
        _editor.setData($list);
        sendIntent(IntentActions.ShowSecondSubPage , _editor);
    }
}
