package com.zw.my.parts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aman.consts.ZKeys;
import com.aman.ui.containers.ZRelativeLayout;
import com.aman.utils.observer.ZNotifcationNames;
import com.zw.R;
import com.zw.global.AppInstance;

import java.util.HashMap;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/26 20:14
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MyMusicFolderItem extends ZRelativeLayout {

    private HashMap _data;

    private CheckBox _check;
    private TextView _txt_name;
    private TextView _txt_path;
    private TextView _txt_num;

    public MyMusicFolderItem(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.my_floderitem , this);
        _check = (CheckBox)findViewById(R.id.checkbox);
        _txt_name = (TextView)findViewById(R.id.txt_name);
        _txt_path = (TextView)findViewById(R.id.txt_path);
        _txt_num = (TextView)findViewById(R.id.txt_num);
        _check.setOnCheckedChangeListener(onCheck);
        this.setOnClickListener(onClick);
    }

//Listener
    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _check.toggle();
        }
    };


    private CompoundButton.OnCheckedChangeListener onCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton $buttonView, boolean $isChecked) {
            _data.put(ZKeys.Selected , $isChecked);
            sendNotification(ZNotifcationNames.Selected , $isChecked);
        }
    };

//listeners
    public void setData(HashMap $d){
        _data = $d;
        _txt_name.setText($d.get("name").toString());
        _txt_path.setText($d.get(ZKeys.Path).toString());
        String s = $d.get(ZKeys.Num) + AppInstance.mainActivity.getString(R.string.global_songUnit);
        _txt_num.setText(s);
        boolean b = (Boolean) $d.get(ZKeys.Selected);
        set_selected(b);
    }

    /**
     * 设置CheckBox 样式
     * @param $b    是否选中
     */
    public void set_selected(boolean $b){
        _check.setOnCheckedChangeListener(null);
        _check.setSelected($b);
        _check.setChecked($b);
        _check.setOnCheckedChangeListener(onCheck);
    }

    public boolean get_selected(){
        return _check.isSelected();
    }
}
