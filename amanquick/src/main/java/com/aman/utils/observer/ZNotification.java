package com.aman.utils.observer;

/**
 * AmanQuick 1.0
 * Created on 2017/8/19 16:10
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZNotification {

    public String name;
    public Object data = null;
    public String action = null;

    public ZNotification(String $name){
        name = $name;
    }

    public ZNotification(String $name , Object $data){
        name = $name;
        data = $data;
    }

    public ZNotification(String $name , Object $data , String $action){
        name = $name;
        data = $data;
        action = $action;
    }

}



