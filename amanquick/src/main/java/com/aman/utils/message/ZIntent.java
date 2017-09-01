package com.aman.utils.message;

import android.content.Intent;

/**
 * AmanQuick 1.0
 * Created on 2017/7/31 19:28
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ZIntent extends Intent {

    public static final String ZyIntentAction = "zyIntentAction";
    public Object data;

    public ZIntent(String $action){
        super($action);
    }

    public ZIntent(String $action , Object $data){
        super($action);
        data = $data;
    }
}
