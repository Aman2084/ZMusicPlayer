package com.zw.my.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.ui.others.CenterDialog;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/10/19 14:33
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MySongListNameDoalog extends CenterDialog {

    private TextView _txt_error;
    private EditText _input;
    private Button _btn_ok;
    private Button _btn_cancel;

    public MySongListNameDoalog(ZObserver $listener) {
        super(AppInstance.mainActivity, R.layout.my_songlistname_dialog, $listener);
    }

    @Override
    protected void onCreate(Bundle $savedInstanceState) {
        super.onCreate($savedInstanceState);
        _txt_error = (TextView)findViewById(R.id.txt_error);
        _input = (EditText)findViewById(R.id.input);
        _btn_ok = (Button)findViewById(R.id.btn_ok);
        _btn_cancel = (Button)findViewById(R.id.btn_cencel);
        _btn_ok.setOnClickListener(onClick);
        _btn_cancel.setOnClickListener(onClick);
        _input.addTextChangedListener(new EditListener());

    }



    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==_btn_ok){
                String s = _input.getText().toString();
                if(s==null || s.length()==0){
                    _txt_error.setVisibility(View.VISIBLE);
                    return;
                }else{
                    _observable.sendNotification(ZNotifcationNames.OK , s);
                }
            }
            hide();
        }
    };

    private class EditListener implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable $s) {
            String s = $s.toString();
            if(s.length()>0){
                _txt_error.setVisibility(View.INVISIBLE);
            }
        }
    }

}
