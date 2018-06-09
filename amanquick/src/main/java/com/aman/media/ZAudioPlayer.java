package com.aman.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AmanQuick 1.0
 * Created on 2018/4/24 22:39
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZAudioPlayer extends ZAudioPlayerBase {


    private class ProgressTask extends TimerTask {
        @Override
        public void run() {
            _handler.postDelayed(onProgress , 0);
        }
    }

    public boolean isReady = false;

    private Timer _timer;
    private ProgressTask _task;
    private Handler _handler;

    public ZAudioPlayer(){
        _handler = new Handler();
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.setLooping(false);
        this.setOnCompletionListener(onComplete);
        this.setOnErrorListener(onError);
        this.setOnPreparedListener(onPrepared);
        this.setOnSeekCompleteListener(onSeekListener);
    }

//功能方法
    private void timerStart() {
        if(_timer!=null){
            return;
        }
        _timer = new Timer();
        _task = new ProgressTask();
        _timer.scheduleAtFixedRate(_task , 0 , 1000);
    }

    private void timerStop(){
        if(_task!=null){
            _task.cancel();
            _task = null;
        }
        if(_timer!=null){
            _timer.cancel();
            _timer = null;
        }
    }


//事件响应
    private OnCompletionListener onComplete = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer $m) {
            sendNotification(ZAudioNotification.Complete);
        }
    };
    private OnErrorListener onError = new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer $m, int $what, int $extra) {
            HashMap m = new HashMap();
            m.put("what" , $what);
            m.put("extra" , $extra);
            sendNotification(ZAudioNotification.Error , m);
            return false;
        }
    };
    private OnPreparedListener onPrepared = new OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer $m) {
            isReady = true;
            sendNotification(ZAudioNotification.Prepared , $m.getDuration() , 0);
        }
    };
    private OnSeekCompleteListener onSeekListener = new OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer $m) {
            sendNotification(ZAudioNotification.Seek , getDuration() , getCurrentPosition());
        }
    };

    private Runnable onProgress = new Runnable() {
        @Override
        public void run() {
            sendNotification(ZAudioNotification.Progress , getDuration() , getCurrentPosition());
        }
    };

//接口方法

    public void play(String $path) {
        if(prepare($path)){
            start();
        }
    }

    @Override
    public void start(){
        try{
            super.start();
        }catch(Exception $e){
            sendNotification(ZAudioNotification.Error , $e);
            return;
        }
        timerStart();
        sendNotification(ZAudioNotification.Play);
    }

    @Override
    public void stop(){
        try{
            super.stop();
        }catch(Exception $e){
            sendNotification(ZAudioNotification.Error , $e);
            return;
        }
        timerStop();
        isReady = false;
        sendNotification(ZAudioNotification.Stop);
    }

    @Override
    public void pause(){
        try{
            super.pause();
        }catch(IllegalStateException $e){
            sendNotification(ZAudioNotification.Error , $e);
            return;
        }
        timerStop();
        sendNotification(ZAudioNotification.Pause);
    }

    public boolean prepare(String $path){
        if(isPlaying()){
            stop();
        }
        this.reset();
        try{
            this.setDataSource($path);
        }catch(Exception $e){
            sendNotification(ZAudioNotification.Error , $e);
            return false;
        }
        try{
            super.prepare();
        }catch(Exception $e){
            sendNotification(ZAudioNotification.Error , $e);
            return false;
        }
        return true;
    }

    @Override
    public void reset() {
        super.reset();
        isReady = false;
    }
}
