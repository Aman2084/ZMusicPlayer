package com.aman.ui.containers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.aman.utils.observer.IZObservable;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObservable;

import java.util.Observer;

/**
 * AmanQuick 1.0
 * Created on 2017/8/20 8:58
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZRelativeLayout extends RelativeLayout implements IZObservable {

    public Object data;

    protected ZObservable observable = new ZObservable(this ,null);

    public ZRelativeLayout(Context $c, AttributeSet $a){
        super($c , $a);
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

    public void setData(Object $o){ data = $o; }
}
