package com.aman.utils.observer;

import android.support.annotation.Nullable;

import java.util.Observable;

/**
 * AmanQuick 1.0
 * Created on 2017/8/19 16:19
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZObservable extends Observable implements IZObservable {

    private String _name;
    private Object _owner;

    public ZObservable(@Nullable Object $owner , @Nullable String $name){
        _owner = $owner;
        _name = $name;
    }

//listeners

    /**
     * 向所有Observe发送Notification
     *
     * @param $n 该Notification将发送到所有侦听当前对象的Oberver（一个实现了Oberver接口的对方）
     *           最终现身于现于Observer.update的第二个参数
     *
     * @see java.util.Observer
     */
    public void sendNotification(ZNotification $n){
        $n.target = this;
        $n.owner = _owner;
        setChanged();
        notifyObservers($n);
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

//getter and setter

    public String getName(){
        return _name;
    }

    /**
     * 向所有Observe发送一个字符串类型的广播，以代表一种程序行为的发生
     * @param $str  该字符串将发送到所有侦听当前对象的Oberver（一个实现了Oberver接口的对方）
     *              最终现身于现于Observer.update的第二个参数
     *
     * @see java.util.Observer
     */
    public void setName(String $str){
        _name = $str;
        setChanged();
        notifyObservers(_name);
    }

    public Object getOwner() {
        return _owner;
    }

    public void setOwner(Object _owner) {
        this._owner = _owner;
    }
}
