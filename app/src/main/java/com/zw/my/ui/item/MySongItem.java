package com.zw.my.ui.item;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aman.ui.containers.items.ZRelativeItem;
import com.aman.utils.UIUtils;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZObserver;
import com.zw.R;
import com.zw.global.AppNotificationNames;
import com.zw.global.model.data.Song;
import com.zw.global.model.data.SongListItem;

/**
 * AmanQuick 1.0
 * Created on 2017/9/22 21:03
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class MySongItem extends ZRelativeItem {

//    private static MySongItem showBtnItem;

    private LinearLayout _core_btns;
    private ImageButton _btn_fav;
    private ImageButton _btn_delete;
    private CheckBox _checkbox;

    private TextView _txt_name;
    private TextView _txt_singer;

//    private ScrollShowHideHelper _scroll;

    /**是否启用删除和收藏菜单
    public boolean useBtns = false;
     */
    private boolean _isShowBatch = false;
    private boolean _isShowBtns = false;
    private boolean _showFavorite = false;

    public MySongItem(Context $c, @Nullable ZObserver $l) {
        super($c, R.layout.my_song_item, $l);

        _txt_name = (TextView)findViewById(R.id.txt_name);
        _txt_singer = (TextView)findViewById(R.id.txt_singer);

        _core_btns = (LinearLayout) findViewById(R.id.core_btn);
        _btn_fav = (ImageButton)findViewById(R.id.btn_fav);
        _btn_delete = (ImageButton)findViewById(R.id.btn_delete);
        _checkbox = (CheckBox) findViewById(R.id.checkbox);
//        _scroll = new ScrollShowHideHelper(_core_btns);


        int[] ids = {R.id.btn_fav , R.id.btn_delete};
        UIUtils.setOnClickByIds(this , ids , onBtns);

        setOnClickListener(new OnClickListener() {
            public void onClick(View $v) {
                sendNotification(ZNotifcationNames.Click , data);
            }
        });
        _checkbox.setOnCheckedChangeListener(onCheck);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(_showFavorite){
            int pos = _core_btns.getMeasuredWidth() - _btn_fav.getMeasuredWidth();
            _btn_fav.setX(pos);
        }
    }


//Listeners
    private CompoundButton.OnCheckedChangeListener onCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton $buttonView, boolean $isChecked) {
        if(data!=null && data instanceof SongListItem){
            ((SongListItem) data).selected = $isChecked;
            sendNotification(ZNotifcationNames.Selected , $isChecked);
        }
        }
    };


    private OnClickListener onBtns = new OnClickListener() {
        @Override
        public void onClick(View $v) {
            SongListItem item = (SongListItem)data;
            String type = null;
            switch($v.getId()){
                case R.id.btn_fav:
                    if(item!=null){
                        type = item.isFavorite ? AppNotificationNames.UNFavorite : AppNotificationNames.Favorite;
                    }
                    break;
                case R.id.btn_delete:
                    sendNotification(ZNotifcationNames.Delete , data);
                    break;
            }
            if(type!=null){
                sendNotification(type , data);
            }
        }
    };

//显示/隐藏按钮
    /*@Override
    public boolean onTouchEvent(MotionEvent $e) {
        float w = getMeasuredWidth();
        if(_isShowBatch || !useBtns || _scroll.playing){
            return super.onTouchEvent($e);
        }
        switch ($e.getAction()){
            case MotionEvent.ACTION_DOWN:
                setShowBtnItem(this);
                float left = w - _core_btns.getWidth();
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
    */

//interface
    @Override
    public void setData(Object $o) {
        super.setData($o);
        Song s = null;
        if($o instanceof Song){
            s = (Song)$o;
        }else if($o instanceof SongListItem){
            SongListItem item = (SongListItem)$o;
            s = item.song;
            _checkbox.setSelected(item.selected);
            refuse_favorite();
        }

        if(s!=null){
            _txt_name.setText(s.getDisplayName());
            _txt_singer.setText(s.getDisplaySinger());
        }
    }



    /**是否显示多选选项*/
    public void showBatch(boolean $b){
        if(_isShowBatch==$b){
            return;
        }
        _isShowBatch = $b;
        if(_isShowBatch){
            _checkbox.setVisibility(View.VISIBLE);
//            hideBtns();
        }else{
            _checkbox.setVisibility(View.INVISIBLE);
        }
    }

    public void showBtns(boolean $b){
        _isShowBtns = $b;
        if($b){
            _core_btns.setVisibility(View.VISIBLE);
        }else{
            _core_btns.setVisibility(View.GONE);
        }
    }

    public void showFavorite() {
        _showFavorite = true;
        _core_btns.setVisibility(View.VISIBLE);
        _btn_delete.setVisibility(View.GONE);
    }

    public void refuse_favorite() {
        SongListItem o = (SongListItem)data;
        if(o==null){
            return;
        }

        int imgId = o.isFavorite ? R.drawable.my_global_fav : R.drawable.my_global_unfav;
        _btn_fav.setImageResource(imgId);
    }

    /*
    public void hideBtns() {

        if(showBtnItem==this){
            showBtnItem = null;
        }
//        _scroll.hideBtns();

    }
    */

//getter and setter

    public void setSelected(boolean $b){
        _checkbox.setOnCheckedChangeListener(null);
        _checkbox.setSelected($b);
        _checkbox.setChecked($b);
        _checkbox.setOnCheckedChangeListener(onCheck);
    }

    /*
    private static void setShowBtnItem(MySongItem $item){
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