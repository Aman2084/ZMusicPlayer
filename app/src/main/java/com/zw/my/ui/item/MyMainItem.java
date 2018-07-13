package com.zw.my.ui.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.utils.observer.ZNotifcationNames;
import com.zw.R;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MyMainItem extends ZRelativeLayout {

    public MyMainItem(Context $c, AttributeSet $a){
        super($c , $a);

        LayoutInflater.from($c).inflate(R.layout.my_main_item ,this , true);

        TypedArray a = $c.obtainStyledAttributes($a ,R.styleable.MyMainItem);
        int id = a.getResourceId(R.styleable.MyMainItem_imgSrc , -1);
        if(id!=-1){
            ImageView img = (ImageView) findViewById(R.id.img);
            img.setImageResource(id);
        }

        String str = a.getString(R.styleable.MyMainItem_itemName);
        TextView t = (TextView)findViewById(R.id.txt_name);
        t.setText(str);


        ImageView btn = (ImageButton)this.findViewById(R.id.btn);
        btn.setOnClickListener(onClick);
        this.setOnClickListener(onClick);
    }

    public void setNum(int $n){
        String str = $n + "首";
        TextView t = (TextView)findViewById(R.id.txt_num);
        t.setText(str);
    }

    private OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            boolean b = $v.getId()==R.id.btn;
            String s = b ? ZNotifcationNames.Play : ZNotifcationNames.Click;
            sendNotification(s);
        }
    };



}
