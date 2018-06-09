package com.zw.ui.containers;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aman.utils.observer.IZObservable;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.zw.R;

import static com.zw.R.id.txt;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/20 17:50
 *
 * @author Aman
 * @Email 1390792438@qq.com
 *
 * 子页面上侧Title
 */

public class SubPageTitle extends RelativeLayout {

    private IZObservable _observable;

    public SubPageTitle(Context $context, AttributeSet $attrs) {
        super($context, $attrs);

        LayoutInflater.from($context).inflate(R.layout.sub_title , this);

        TypedArray a = $context.obtainStyledAttributes($attrs , R.styleable.SubPageTitle);
        String s = a.getString(R.styleable.SubPageTitle_titleText);
        TextView t = (TextView) findViewById(txt);
        t.setText(s);

        ImageButton btn = (ImageButton) findViewById(R.id.btn_close);
        btn.setOnClickListener(onClick);
    }

//Listeners
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_observable!=null){
                ZNotification n = new ZNotification(ZNotifcationNames.Close);
                _observable.sendNotification(n);
            }
        }
    };

//getter and setter
    public void set_observable(IZObservable _observable) {
        this._observable = _observable;
    }

    public void set_text(String $str){
        TextView t = (TextView)findViewById(txt);
        t.setText($str);
    }

    public String get_text(){
        TextView t = (TextView)findViewById(txt);
        String s = t.getText().toString();
        return s;
    }
}
