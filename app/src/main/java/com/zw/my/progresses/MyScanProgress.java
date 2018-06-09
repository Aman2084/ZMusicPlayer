package com.zw.my.progresses;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZObservable;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.model.data.Song;
import com.zw.utils.AppUtils;

import java.util.List;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/21 17:17
 *
 * @author Aman
 * @Email 1390792438@qq.com
 * 扫描本地音乐的过程
 */

public class MyScanProgress extends ZObservable {

    private ProgressDialog _diaplog;
    private boolean _scaning = false;

    private List<Song> _arr;

    public MyScanProgress(){
        super(null , null);
    }


    private MyScanThread _thread;

//listeners
    public void scan(){
        if(_scaning){
            return;
        }
        _scaning = true;
        _diaplog = new ProgressDialog(AppInstance.mainActivity);
        _diaplog.setTitle(R.string.my_scanTitle);
        _diaplog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        _diaplog.setMax(100);
        _diaplog.setProgress(0);
        _diaplog.show();

        _arr = AppUtils.getSongList(AppInstance.mainActivity);
        _diaplog.setMax(_arr.size());
        _thread = new MyScanThread(_arr , hander);
        _thread.start();
    }

//scan
    private Handler hander = new Handler(){
        public void handleMessage(Message $msg) {
            switch ($msg.arg1){
                case 1:
                    _diaplog.setProgress($msg.what);
                    break;
                case 0:
                    complete();
                    break;
            }
        }
    };

    private void complete() {
        _diaplog.cancel();
        _diaplog = null;
        _thread.clear();
        _thread = null;
        List<Song> a = _arr;
        _arr = null;
        _scaning = false;
        sendNotification(ZNotifcationNames.Complete , a);
    }
}
