package com.aman.utils.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/31 6:20
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public abstract class ZObserver implements Observer {

    @Override
    public void update(Observable $o, Object $arg) {
        if($arg instanceof ZNotification){
            onNotification((ZNotification) $arg);
        }
    }

    public abstract void onNotification(ZNotification $n);
}
