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
 * @Email 1390792438@qq.com
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
        for (int i = 0; i < _arr.size(); i++) {

            Song s = _arr.get(i);

            MediaMetadataRetriever m = new MediaMetadataRetriever();
            try{
                m.setDataSource(s.getPath());
            }catch (IllegalArgumentException $e){
                _arr.remove(i);
                i--;
                continue;
            }

            String str = null;
            //歌曲名（null）
            str = s.getName();
            if(str==null){
                s.setName(m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            }
            //专辑名（null）
            str = s.getAlbum();
            if(str==null){
                s.setAlbum(m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            }
            //歌手名（null）
            str = s.getSinger();
            if(str==null){
                s.setSinger(m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            }
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
