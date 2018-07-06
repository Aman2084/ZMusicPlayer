package com.zw.ui.others;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.aman.utils.observer.ZObservable;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.model.UIModel;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/19 14:30
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */
public class CenterDialog extends Dialog {

    private int _layoutResID;
    private Context _context;

    protected ZObservable _observable;

    public CenterDialog(Context $c, int $layoutResID , ZObserver $observer) {
        super($c, R.style.dialog_dafault);
        _context = $c;
        _layoutResID = $layoutResID;
        _observable = new ZObservable(this , null);
        _observable.addObserver($observer);
    }

    @Override
    protected void onCreate(Bundle $savedInstanceState) {
        super.onCreate($savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(_layoutResID);
        // 宽度全屏
        Window d = getWindow();
        WindowManager.LayoutParams lp = d.getAttributes();
        UIModel m = AppInstance.model.ui;
        lp.width = m.width_px*4/5;       //设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        // 点击Dialog外部消失
        setCanceledOnTouchOutside(true);
    }

    public void destory(){
        if(_observable!=null){
            _observable.deleteObservers();
            _observable = null;
        }
    }

}

