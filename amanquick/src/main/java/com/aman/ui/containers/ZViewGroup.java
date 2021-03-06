package com.aman.ui.containers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.aman.utils.observer.IZObservable;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObservable;

import java.util.Observer;

/**
 * AmanQuick 1.0
 * Created on 2017/8/20 8:53
 * <p>
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class ZViewGroup extends ViewGroup implements IZObservable {

    public Object data;

    protected ZObservable observable = new ZObservable(this ,null);

    public ZViewGroup(Context $c, AttributeSet $a){
        super($c , $a);
    }

    protected void onLayout(boolean changed,
                                     int l, int t, int r, int b){
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
    public void sendNotification(String $name) {observable.sendNotification($name);}

    @Override
    public void sendNotification(String $name, Object $data) {observable.sendNotification($name , $data);}

    @Override
    public void sendNotification(String $name, Object $data, String $action) {observable.sendNotification($name , $data , $action);}

    @Override
    public void setName(String $str) {
        observable.setName($str);
    }

    public void setData(Object $o){ data = $o; }

    public void destroy() {
        deleteObservers();
    }
}
