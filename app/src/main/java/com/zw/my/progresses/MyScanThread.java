package com.zw.my.progresses;

import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;

import com.zw.global.model.data.Song;

import java.util.List;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/24 21:47
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 * 音频信息扫描线程
 * 每毫秒扫描十条信息
 */

public class MyScanThread extends Thread {

    private List<Song> _arr;
    private Handler _handler;


    public MyScanThread(List<Song> $a , Handler $h){
        super();
        _arr = $a;
        _handler = $h;
    }


    @Override
    public void run() {
        super.run();
        int n = _arr.size();
        for (int i = 0; i < n; i++) {

            Song s = _arr.get(i);

            MediaMetadataRetriever m = new MediaMetadataRetriever();
            m.setDataSource(s.path);

            //歌曲名（null）
            s.name = s.name==null ? m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) : s.name;
            //专辑名（null）
            s.album = s.album==null ? m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) : s.album;
            //歌手名（null）
            s.singer = s.singer==null ? m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) : s.singer;
            Message mg = new Message();
            mg.what = i+1;
            mg.arg1 = 1;
            _handler.sendMessage(mg);

            if(i%10==0){
                try{
                    sleep(40);
                }catch (InterruptedException $e){}
            }
        }

        Message meg = new Message();
        meg.arg1 = 0;
        _handler.sendMessage(meg);
    }

    public void clear() {
        _arr = null;
        _handler = null;
    }
}
