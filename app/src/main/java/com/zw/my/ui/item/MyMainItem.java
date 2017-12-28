package com.zw.my.ui.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zw.R;

/**
 * Created by Administrator on 2017/8/8.
 */

public class MyMainItem extends RelativeLayout {

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
    }

    public void setNum(int $n){
        String str = $n + "首";
        TextView t = (TextView)findViewById(R.id.txt_num);
        t.setText(str);
    }
}
