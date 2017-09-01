package com.zw.my;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.aman.utils.message.ZIntent;
import com.zw.R;
import com.zw.my.parts.MyMainItem;
import com.zw.global.IntentActions;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/8 10:35
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MyMainContent extends LinearLayout {

    private final ImageButton _btn_all;
    private final ImageButton _btn_fav;
    private MyMainItem _item_all;
    private MyMainItem _item_fav;



    public MyMainContent(Context $c ,@Nullable AttributeSet attrs){
        super($c , attrs);

        setOrientation(LinearLayout.VERTICAL);

        LayoutInflater.from($c).inflate(R.layout.my_main , this , true);

        _item_all = (MyMainItem) findViewById(R.id.item_all);
        _item_fav = (MyMainItem) findViewById(R.id.item_fav);
        _btn_all = (ImageButton)_item_all.findViewById(R.id.btn);
        _btn_fav = (ImageButton)_item_fav.findViewById(R.id.btn);
        _item_all.setOnClickListener(onMainItem);
        _item_fav.setOnClickListener(onMainItem);
        _btn_all.setOnClickListener(onMainItem);
        _btn_fav.setOnClickListener(onMainItem);
    }

//Event Handler
    private OnClickListener onMainItem = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = null;
            if(v==_item_all){
                str = IntentActions.ShowMyAllMusic;
            }else if(v==_item_fav){
                str = IntentActions.ShowMyFavorites;
            }else if(v==_btn_all){
                str = IntentActions.PlayMyAllMusic;
            }else if(v==_btn_fav){
                str = IntentActions.PlayMyFavorites;
            }
            if(str!=null){
                LocalBroadcastManager m = LocalBroadcastManager.getInstance(getContext());
                ZIntent intent = new ZIntent(str);
                m.sendBroadcast(intent);
            }
        }
    };

}
