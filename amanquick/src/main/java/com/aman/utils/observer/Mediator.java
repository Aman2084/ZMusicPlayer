package com.aman.utils.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.aman.utils.message.ZLocalBroadcast;

import java.util.Observable;
import java.util.Observer;


/**
 * AmanQuick 1.0
 * Created on 2017/8/18 21:45
 * <p>
 * @author Aman
 * @Email 1390792438@qq.com
 *
 * 逻辑专用模块，包含以下几个基本功能：
 *      1. 发送 / 侦听LocalBroadcast
 *      2. Observer观察者（但不具备被观察身份）
 */

abstract public class Mediator implements Observer,IZObservable{

    protected ZObservable observable = new ZObservable(this ,null);
    protected Context _context;

    public Mediator(Context $c){
        _context = $c;
        initBroadcast();
    }

    /**
     * 构造函数
     * */
    public Mediator(){
        initBroadcast();
    }

    protected void initBroadcast(){
        String[] a = getActions_application();
        if( a!=null && a.length>0){
            ZLocalBroadcast.registerAppReceiver(a , receiver);
        }
    }


    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context $context, Intent $intent) {
            receiveIntent($context, $intent);
        }
    };


    protected void sendIntent(String $action){
        sendIntent($action , null);
    }

    protected void sendIntent(String $action , @Nullable Object $data){
        ZLocalBroadcast.sendAppIntent($action , $data);
    }

//    abstract protected String[] getActions_activity();
    abstract protected void receiveIntent(Context $context, Intent $intent);
    protected String[] getActions_application(){return null;}

    @Override
    public void update(Observable $o, Object $data) {}

    public void destroy(){
        observable.deleteObservers();
        ZLocalBroadcast.unregisterAppReceiver(receiver);
        _context = null;
    }


    public Context getContext(){
      return _context;
    }

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
        ZNotification n = new ZNotification($name);
        sendNotification(n);
    }

    @Override
    public void sendNotification(String $name, Object $data) {
        ZNotification n = new ZNotification($name , $data);
        sendNotification(n);
    }

    @Override
    public void sendNotification(String $name, Object $data, String $action) {
        ZNotification n = new ZNotification($name , $data , $action);
        sendNotification(n);
    }

    @Override
    public void setName(String $str) {
        observable.setName($str);
    }
}
