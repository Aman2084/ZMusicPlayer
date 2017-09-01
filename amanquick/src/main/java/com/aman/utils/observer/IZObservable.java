package com.aman.utils.observer;

import java.util.Observer;

/**
 * AmanQuick 1.0
 * Created on 2017/8/20 8:35
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public interface IZObservable {

    void addObserver(Observer $o);
    void deleteObserver(Observer o);
    void deleteObservers();


    void sendNotification(ZNotification $n);
    void sendNotification(String $name);
    void sendNotification(String $name , Object $data);
    void sendNotification(String $name , Object $data , String $action);
    void setName(String $str);
}
