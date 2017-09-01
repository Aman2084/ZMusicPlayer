package com.aman.ui;

import com.aman.utils.observer.ZNotification;

/**
 * AmanQucik 1.0
 * Created on 2017/8/31 5:16
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public abstract class ZItemListener {

    public abstract void onItem(ZNotification $n);

    public void sendNotification(String $name) {
        sendNotification($name , null);
    }

    public void sendNotification(String $name, Object $data) {
        sendNotification($name , $data , null);
    }

    public void sendNotification(String $name, Object $data, String $action) {
        ZNotification n = new ZNotification($name , $data , $action);
        onItem(n);
    }
}
