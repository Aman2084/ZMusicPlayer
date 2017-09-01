package com.zw.music;

import android.content.Context;
import android.content.Intent;

import com.aman.utils.observer.Mediator;

/**
 * ZmusicPlayer 1.0
 * Created on 2017/8/18 22:39
 * <p>
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MusicMediator extends Mediator {
    public MusicMediator(Context $c) {
        super($c);
    }

    @Override
    protected String[] getLocalIntentActions() {
        return new String[0];
    }

    @Override
    protected void receiverLocalBroadcast(Context $context, Intent $intent) {

    }
}
