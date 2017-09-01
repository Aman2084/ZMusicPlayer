package com.aman.utils.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.aman.utils.message.ZIntent;

import java.util.Observable;
import java.util.Observer;


/**
 * AmanQuick 1.0
 * Created on 2017/8/18 21:45
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 *
 * 逻辑专用模块，包含以下几个基本功能：
 *      1. 发送 / 侦听LocalBroadcast
 *      2. Observer观察者（但不具备被观察身份）
 */

abstract public class Mediator implements Observer{

    protected LocalBroadcastManager _localBroadcastManager;
    protected Context _context;

    public Mediator(Context $c){
        _context = $c;
        _localBroadcastManager = LocalBroadcastManager.getInstance($c);
        initBoradcast($c);
    }

    private void initBoradcast(Context $c) {
        IntentFilter f = new IntentFilter();
        String[] a = getLocalIntentActions();
        for (int i = 0; i<a.length ; i++) {
            String s = a[i];
            f.addAction(s);
        }
        _localBroadcastManager.registerReceiver(localBroadcastReceiver , f);
    }

    protected BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context $context, Intent $intent) {
            receiverLocalBroadcast($context, $intent);
        }
    };

    protected void sendIntent(String $action , @Nullable Object $data){
        ZIntent o = new ZIntent($action , $data);
        _localBroadcastManager.sendBroadcast(o);
    }

    abstract protected String[] getLocalIntentActions();
    abstract protected void receiverLocalBroadcast(Context $context, Intent $intent);

    @Override
    public void update(Observable $o, Object $data) {}
}
