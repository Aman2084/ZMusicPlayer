package com.zw.time;

import android.content.Context;
import android.content.Intent;

import com.aman.utils.observer.Mediator;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/18 22:38
 * <p>
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class TimeMediator extends Mediator {

    public TimeMediator(Context $c) {
        super($c);
    }

    @Override
    protected String[] getActions_application() {
        return new String[0];
    }

    @Override
    protected void receiveIntent(Context $context, Intent $intent) {
    }
}
