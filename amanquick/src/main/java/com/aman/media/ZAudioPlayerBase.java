package com.aman.media;

import android.media.MediaPlayer;

import com.aman.utils.observer.IZObservable;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObservable;

import java.util.Observer;

/**
 * 音频播放器基础类，实现IZObservable 接口
 *
 * AmanQuick 1.0
 * Created on 2018/4/24 22:34
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZAudioPlayerBase extends MediaPlayer implements IZObservable {

    protected ZObservable observable = new ZObservable(this ,null);


    @Override
    public void addObserver(Observer $o) {
        observable.addObserver($o);
    }

    @Override
    public void deleteObserver(Observer $o) {
        observable.deleteObserver($o);
    }

    @Override
    public void deleteObservers() {
        observable.deleteObservers();
    }

    @Override
    public void sendNotification(ZNotification $n) {
        if($n.target==null){
            $n.target = observable;
        }
        if($n.owner==null){
            $n.owner = this;
        }
        observable.sendNotification($n);
    }

    @Override
    public void sendNotification(String $name) {
        ZNotification n = new ZAudioNotification($name);
        sendNotification(n);
    }

    @Override
    public void sendNotification(String $name, Object $data) {
        ZNotification n = new ZAudioNotification($name , $data);
        sendNotification(n);
    }

    @Override
    public void sendNotification(String $name, Object $data, String $action) {
        ZNotification n = new ZAudioNotification($name , $data , $action);
        sendNotification(n);
    }

    public void sendNotification(String $name , int $duration , int $position) {
        ZNotification n = new ZAudioNotification($name , $duration , $position);
        sendNotification(n);
    }


    @Override
    public void setName(String $str) {
        observable.setName($str);
    }

}
