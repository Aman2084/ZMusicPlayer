package com.aman.widget;

import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aman.utils.message.ZLocalBroadcast;

import static com.aman.utils.message.ZLocalBroadcast.applicationContext;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/25 11:10
 *
 * @author Aman
 * @Email 1390792438@qq.com
 * 桌面部件： 增加对中央ZLocalBroadcast的监听
 */

abstract public class ZWidgetProvider extends AppWidgetProvider {

    public ZWidgetProvider() {
        super();
        String[] a = getActions_application();
        if( a!=null && a.length>0){
            ZLocalBroadcast.registerAppReceiver(a , receiver);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        String[] a = getActions_application();
        if( a!=null && a.length>0){
            ZLocalBroadcast.registerAppReceiver(a , receiver);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        ZLocalBroadcast.unregisterAppReceiver(receiver);
    }

    protected boolean sendAppIntent(String $action , Object $data){
        return ZLocalBroadcast.sendLoacalIntent(applicationContext , $action , $data);
    }

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context $context, Intent $intent) {
            receiveIntent($context, $intent);
        }
    };
    protected String[] getActions_application(){return null;}
    abstract protected void receiveIntent(Context $context, Intent $intent);

}
