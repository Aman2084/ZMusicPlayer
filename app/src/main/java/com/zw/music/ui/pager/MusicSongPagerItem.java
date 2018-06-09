package com.zw.music.ui.pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.aman.media.ZAudioPlayer;
import com.aman.ui.containers.ZRelativeLayout;
import com.aman.utils.UIUtils;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
import com.zw.MainApplication;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.IntentActions;
import com.zw.global.model.data.SongListItem;
import com.zw.global.model.music.PlayModel;
import com.zw.global.model.music.PlayProgress;
import com.zw.ui.CircleImageView;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/5/29 1:00
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongPagerItem extends ZRelativeLayout {

    private boolean _resized = false;

    public boolean isLive = false;
    public int position = -1;

    private CircleImageView _icon = null;

    private MusicSongProgressBar _progress;

    private ImageView _img_play;
    private ImageView _img_pause;


    public MusicSongPagerItem(Context $c, AttributeSet $a) {
        super($c, $a);

        LayoutInflater.from($c).inflate(R.layout.music_songpage_item, this);

        _img_play = (ImageView)findViewById(R.id.btn_play);
        _img_pause = (ImageView)findViewById(R.id.btn_pause);
        _icon = (CircleImageView) findViewById(R.id.img);
        _progress = (MusicSongProgressBar) findViewById(R.id.progress);

        _progress.addObserver(onProgress);
    }

//Listener

    private ZObserver onProgress = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            String action = null;
            Object body = null;
            switch($n.name){
                case ZNotifcationNames.Click:
                    PlayModel m = AppInstance.model.play;
                    action = m.isPlaying ? IntentActions.Pause : IntentActions.Play;
                    break;
                case AppNotificationNames.Seek:
                    action = IntentActions.Seek;
                    body = $n.data;
                    break;
            }
            if(action!=null){
                ZLocalBroadcast.sendAppIntent(action , body);
            }
        }
    };

//Logic
    private void refuse(){

        if(_resized){
            int w = this.getWidth();
            int h = this.getHeight();
            if(w==0 || h==0){
                return;
            }

            Rect r = new Rect(0 , 0 , w , h);

            int d;
            if(w>h){
                d = (int)Math.floor((w - h) / 2);
                r.inset(d , 0);
            }else{
                d = (int)Math.floor((h - w) / 2);
                r.inset(0 , d);
            }

            Rect r_inner = new Rect(r);
            d = (int)Math.floor(r.width()/8);
            r_inner.inset(d , d);
            UIUtils.setPosAndSize(_icon , r_inner);

            Rect r_out = new Rect(r);
            d = (int)Math.floor(r.width()/12);
            r_out.inset(d , d);
            UIUtils.setPosAndSize(_progress , r_out);
        }

        SongListItem s = (SongListItem)this.data;
        if(s!=null){
            Bitmap b = s.song.getBmp(AppInstance.mainActivity);
            if(b!=null){
                _icon.setImageBitmap(b);
            }else{
                _icon.setImageResource(R.drawable.music_page_defaulticon);
            }
        }else{
            _icon.setImageResource(R.drawable.music_page_defaulticon);
        }

        ZAudioPlayer p = MainApplication.getInstance().player;
        if(p.isPlaying()){
            play();
        }else{
            pause();
        }
    }

    @Override
    protected void onSizeChanged(int $w, int $h, int $oldw, int $oldh) {
        super.onSizeChanged($w, $h, $oldw, $oldh);
        _resized = true;
        refuse();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(_resized){
            refuse();
            _resized = false;
        }
    }


//Interface
    @Override public void setData(Object $o){
        if($o==data){
            return;
        }
        if(data!=null){
            SongListItem s1 = (SongListItem)data;
            SongListItem s2 = (SongListItem)$o;
            if(s1.songId.equals(s2.songId)){
                return;
            }
        }
        super.setData($o);
        refuse();
    }

    public void setData_progress(PlayProgress $p) {
        _progress.setData($p);
    }

    public void play(){
        _img_play.setVisibility(GONE);
        _img_pause.setVisibility(VISIBLE);
    }
    public void pause(){
        _img_play.setVisibility(VISIBLE);
        _img_pause.setVisibility(GONE);
    }

}
