package com.zw.music.ui.pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.aman.ui.containers.ZRelativeLayout;
import com.aman.utils.Debuger;
import com.aman.utils.UIUtils;
import com.aman.utils.message.ZLocalBroadcast;
import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;
import com.aman.utils.observer.ZObserver;
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

    private int _pid;

    private static int numObject = -1;
    private static int getPid(){
        numObject ++;
        return numObject;
    }


    public boolean isLive = false;
    public int position = -1;

    private CircleImageView _icon = null;

    private MusicSongProgressBar _progress;

    private ImageView _img_play;
    private ImageView _img_pause;

    private boolean inited = false;

    public MusicSongPagerItem(Context $c, AttributeSet $a) {
        super($c, $a);

        LayoutInflater.from($c).inflate(R.layout.music_songpage_item, this);
        _img_play = (ImageView)findViewById(R.id.btn_play);
        _img_pause = (ImageView)findViewById(R.id.btn_pause);
        _icon = (CircleImageView) findViewById(R.id.img);
        _progress = (MusicSongProgressBar) findViewById(R.id.progress);
        _icon.setVisibility(GONE);
        _progress.setVisibility(GONE);
        _progress.addObserver(onProgress);
        this._pid = getPid();

//        trace("new" );
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
    private void refuse_draw(){
        if(data==null){
            return;
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
        };

//        trace("draw");
    }


    private void refuse_layout(){
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
        _icon.setVisibility(VISIBLE);
        _progress.setVisibility(VISIBLE);

//        trace("layout");
    }

    private void refuse_playStatus(){
        if(data==null){
            return;
        }
        SongListItem s = (SongListItem)data;
        if(s.stause == SongListItem.Play){
            _img_play.setVisibility(GONE);
            _img_pause.setVisibility(VISIBLE);
        }else{
            _img_play.setVisibility(VISIBLE);
            _img_pause.setVisibility(GONE);
        }
    }

    private void  trace(String $s){
        String s = " " + String.valueOf(_pid) + " " + $s;
        Debuger.traceTime(s);
    }


//Listener

    @Override
    protected void onSizeChanged(int $w, int $h, int $oldw, int $oldh) {
        super.onSizeChanged($w, $h, $oldw, $oldh);
        refuse_layout();
    }

    @Override
    protected void dispatchDraw(Canvas $c) {
        super.dispatchDraw($c);
        refuse_draw();
        if(!inited){
            inited = true;
            refuse_playStatus();
        }
    }

    private ZObserver onData = new ZObserver() {
        @Override
        public void onNotification(ZNotification $n) {
            switch ($n.name){
                case ZNotifcationNames.Progress:
                    _progress.updata((PlayProgress)$n.data);
                    break;
                case ZNotifcationNames.Stop:
                case ZNotifcationNames.Pause:
                case ZNotifcationNames.Play:
                    refuse_playStatus();
                    break;
            }
        }
    };

//Interface
    @Override public void setData(Object $o){
        if($o==data){
            return;
        }
        SongListItem m = (SongListItem) data;
        if(m!=null){
            m.deleteObserver(onData);
        }

        super.setData($o);
        m = (SongListItem) data;
        m.addObserver(onData);
        if(inited){
            _progress.resetData(m.song.getDuration() , m.position);
            refuse_draw();
            refuse_playStatus();
        }
//        trace("setData");
    }

    public void reset(){
        _icon.setImageResource(R.drawable.music_page_defaulticon);
        _progress.resetData(0 , 0);
    }

}
