package com.aman.ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aman.R;
import com.aman.exception.SingletonException;
import com.aman.ui.containers.ZFrameLayout;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZObserver;

/**
 * AmanQucik 1.0
 * Created on 2018/6/8 16:06
 *
 * @author Aman
 * @Email 1390792438@qq.com
 *
 * 信息提示
 */

public class Alert extends ZFrameLayout {

    public static final int OK = 0b1;
    public static final int Yes = 0b10;
    public static final int No = 0b100;
    public static final int Sure = 0b1000;
    public static final int Cancel = 0b10000;

    private static Alert _instance = null;

    private boolean _resized = false;

    private TextView _txt_title;
    private TextView _txt_masssage;

    private RelativeLayout _core;
    private FrameLayout _core_btns;

    private Button _btn_ok;
    private Button _btn_yes;
    private Button _btn_no;
    private Button _btn_sure;
    private Button _btn_cancel;

    public Alert(Context $c, AttributeSet $a) throws SingletonException {
        super($c, $a);
        if(_instance!=null){
            throw new SingletonException();
        }
        _instance = this;
        LayoutInflater.from($c).inflate(R.layout.alert , this);

        _core = (RelativeLayout)findViewById(R.id.core);
        _core_btns = (FrameLayout)findViewById(R.id.core_btns);
        _txt_title = (TextView)findViewById(R.id.txt_title);
        _txt_masssage = (TextView)findViewById(R.id.txt_masssage);

        this.setVisibility(GONE);
    }


//Tools
    private Button creatBtn(int $id, int $label) {
        Button b = new Button(getContext());
        b.setId($id);
        String s = getResources().getString($label);
        b.setText(s);
        b.setOnClickListener(onClick);
        return b;
    }


    private void layout_btns() {
        int n = _core_btns.getChildCount();
        float g = (float) (_core_btns.getWidth() / 10);
        float w = (_core_btns.getWidth() - g) / n;
        g /= (n+3);
        float pos = g*2;
        for (int i = 0; i <n ; i++) {
            Button b = (Button)_core_btns.getChildAt(i);
            b.setX(pos);
            ViewGroup.LayoutParams p = b.getLayoutParams();
            p.width = (int)w;
            b.setLayoutParams(p);
            b.requestLayout();
            pos += (w + g);
        }
        _core.requestLayout();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(_resized){
            _resized = false;
            layout_btns();
        }
    }

    //Listener

    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            String s = null;
            switch ($v.getId()){
                case OK:
                    s = ZNotifcationNames.OK;
                    break;
                case Sure:
                    s = ZNotifcationNames.Sure;
                    break;
                case Cancel:
                    s = ZNotifcationNames.Cancel;
                    break;
                case Yes:
                    s = ZNotifcationNames.Yes;
                    break;
                case No:
                    s = ZNotifcationNames.No;
                    break;
            }
            setVisibility(GONE);
            sendNotification(ZNotifcationNames.Close ,data , s);
            deleteObservers();
        }
    };

    @Override
    protected void onSizeChanged(int $w, int $h, int $oldw, int $oldh) {
        super.onSizeChanged($w, $h, $oldw, $oldh);
        if($w==0 || $h==0){
            return;
        }

        ViewGroup.LayoutParams p = _core.getLayoutParams();
        float y = ($h - p.height) / 2;
        _core.setY(y);
        _resized = true;
        layout_btns();
    }

//interface

    public static void show(String $title , String $message , int $btns , ZObserver $o){
        Alert a = getInstance();
        a.setVisibility(VISIBLE);
        a.setData($message , $title , $btns , $o);
    }

    public static void clear() {
        if(_instance!=null){
            _instance.deleteObservers();
            _instance = null;
        }
    }

    private void setData(String $message, String $title, int $btns, ZObserver $o) {
        $btns = ($btns==0) ? 1 : $btns;

        _txt_title.setText($title);
        _txt_masssage.setText($message);

        _core_btns.removeAllViews();
        if(($btns&OK)>0){
            _core_btns.addView(getBtn_ok());
        }
        if(($btns&Sure)>0){
            _core_btns.addView(getBtn_sure());
        }
        if(($btns&Cancel)>0){
            _core_btns.addView(getBtn_cancel());
        }
        if(($btns&Yes)>0){
            _core_btns.addView(getBtn_yes());
        }
        if(($btns&No)>0){
            _core_btns.addView(getBtn_no());
        }
        layout_btns();

        if($o!=null){
            this.addObserver($o);
        }
    }


//getter and setter
    public static Alert getInstance(){
        return _instance;
    }

    public static Alert getInstance(Context $c){
        if(_instance==null){
            try{
                _instance = new Alert($c , null);
            }catch (SingletonException $e){}
        }
        return _instance;
    }



    private Button getBtn_ok(){
      if(_btn_ok==null){
          _btn_ok = creatBtn(OK , R.string.ok);
      }
        return _btn_ok;
    }

    private Button getBtn_yes(){
        if(_btn_yes==null){
            _btn_yes = creatBtn(Yes , R.string.yes);
        }
        return _btn_yes;

    }
    private Button getBtn_no(){
        if(_btn_no==null){
            _btn_no = creatBtn(No , R.string.no);
        }
        return _btn_no;
    }
    private Button getBtn_sure(){
        if(_btn_sure==null){
            _btn_sure = creatBtn(Sure , R.string.sure);
        }
        return _btn_sure;
    }
    private Button getBtn_cancel(){
        if(_btn_cancel==null){
            _btn_cancel = creatBtn(Cancel , R.string.cancel);
        }
        return _btn_cancel;
    }
}
