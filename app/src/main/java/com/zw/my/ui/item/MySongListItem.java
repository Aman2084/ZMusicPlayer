package com.zw.my.ui.item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aman.ui.containers.items.ZRelativeItem;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.model.data.SongList;

/**
 * AmanQuick 1.0
 * Created on 2017/10/17 16:02
 *
 * @author Aman
 * @Email 1390792438@qq.com
 *
 * 我的音乐主页歌单Item
 */

public class MySongListItem extends ZRelativeItem {

    private static MySongListItem showBtnItem = null;

//    private ScrollShowHideHelper _scroll;

    private TextView _txt_num;
    private TextView _txt_name;
    private ImageButton _btn_delete;



    public MySongListItem(Context $c, @Nullable ZObserver $l) {
        super($c, R.layout.my_songlist_item, $l);

//        this.setBackgroundColor(0xff009cff);

        _txt_name = (TextView)findViewById(R.id.txt_name);
        _txt_num = (TextView)findViewById(R.id.txt_num);

        /**
        _btn_delete = (ImageButton)findViewById(R._id.btn_delete);
        _scroll = new ScrollShowHideHelper(_btn_delete);
        _btn_delete.setVisibility(View.VISIBLE);

        _btn_delete.setOnClickListener(onClick);
         */
        setOnClickListener(new OnClickListener() {
            public void onClick(View $v) {
                sendNotification(ZNotifcationNames.Click , data);
            }
        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                sendNotification(ZNotifcationNames.LongClick , data);
                return true;
            }
        });
    }

//EventHandler

    /***
    @Override
    public boolean onTouchEvent(MotionEvent $e) {
        SongList d = (SongList)data;
        float w = getMeasuredWidth();
        if(d==null || !d.useBtn || _scroll.playing){
            return super.onTouchEvent($e);
        }
        switch ($e.getAction()){
            case MotionEvent.ACTION_DOWN:
                setShowBtnItem(this);
                float left = w - _btn_delete.getWidth();
                _scroll.down(left , w , $e.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                if(_scroll.isDown){
                    _scroll.move($e.getX());
                }
                break;
            case MotionEvent.ACTION_UP:
                _scroll.up();
                break;
        }
        return super.onTouchEvent($e);
    }

    protected OnClickListener onClick = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            itemListener.sendNotification(ZNotifcationNames.Delete , data);
        }
    };


    public void hideBtns() {
        if(showBtnItem==this){
            showBtnItem = null;
        }
        _scroll.hideBtns();
    }
    */

    public void setData(SongList $d){
        super.setData($d);

        _txt_name.setText($d.title);
//        String str = AppUtils.id2String(R.string.global_songNum);
        String str = "%s首";
        str = String.format(str , $d.getSongNum());
        _txt_num.setText(str);
    }


    /***
    private static void setShowBtnItem(MySongListItem $item){
        if(showBtnItem==$item){
            return;
        }
        if(showBtnItem!=null){
            showBtnItem.hideBtns();
        }
        showBtnItem = $item;
    }
     */

}
