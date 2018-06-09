package com.zw.music.ui.pager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.TextView;

import com.aman.ui.containers.ZFrameLayout;
import com.aman.utils.MathUtils;
import com.aman.utils.ZUtils;
import com.aman.utils.math.Cartesian;
import com.aman.utils.math.Polar;
import com.aman.utils.observer.ZNotifcationNames;
import com.zw.R;
import com.zw.global.AppInstance;
import com.zw.global.AppNotificationNames;
import com.zw.global.model.music.PlayProgress;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/1 1:31
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class MusicSongProgressBar extends ZFrameLayout {

    private static final double LeftRandian = Math.PI*3 / 4;
    private static final double RightRandian = Math.PI / 4;
    private static final double LongRandian = Math.PI*6 / 4;

    private enum Area{
        LeftTop , RightTop , LeftBottom , RightBottom
    }

    private enum TouchType{
        Center,Outter,Other
    }

    private TouchType _touchType = TouchType.Other;
    private Area _lastArea;

    private boolean _resized = false;

    private double _radian;
    private double _radius = 0;
    private Cartesian _center = null;

    private TextView _txt_total;
    private TextView _txt_currect;

    public MusicSongProgressBar(Context $c, AttributeSet $a) {
        super($c, $a);
        LayoutInflater.from($c).inflate(R.layout.music_songpage_progressbar , this);
        _txt_total = (TextView)findViewById(R.id.txt_total);
        _txt_currect = (TextView)findViewById(R.id.txt_current);
    }

//Tools
    private Area radian2Area(double $r){
        Area a = null;

        if($r<-Math.PI/2){
            a = Area.LeftTop;
        }else if($r<0){
            a = Area.RightTop;
        }else if($r<Math.PI/2){
            a = Area.RightBottom;
        }else if($r<Math.PI){
            a = Area.LeftBottom;
        }
        return a;
    }

    private double calcRadianAndArea(double $r) {
        $r = MathUtils.commonRadian($r);
        if(isOutCrossBorder($r)){
            if (_lastArea==Area.LeftBottom){
                $r = LeftRandian;
            }else if(_lastArea==Area.RightBottom){
                $r = RightRandian;
            }
        }else{
            _lastArea = radian2Area($r);
        }
        return $r;
    }

    /**
     * 是否越界
     * @param   $r   标准化弧度（必须在-Pi到Pi之间）
     * @return  true表示弧度越界，false表示未越界
     */
    private boolean isOutCrossBorder(double $r){
        boolean b = false;
        if(RightRandian <$r && $r<LeftRandian){
            b = true;
        }
        return b;
    }


//Listener
    @Override
    public boolean onTouchEvent(MotionEvent $e) {
        Cartesian c = new Cartesian((double)$e.getX() , (double)$e.getY());
        String s = String.valueOf($e.getX()) + "," + String.valueOf($e.getY());
        String s1 = String.valueOf($e.getRawX()) + "," + String.valueOf($e.getRawY());
        switch($e.getAction()){
            case MotionEvent.ACTION_DOWN:
                onDown(c);
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(c);
                break;
            case MotionEvent.ACTION_UP:
                onUp(c);
                break;
        }
        boolean b = _touchType!=TouchType.Other;
        return b;
    }

    private void onDown(Cartesian $c) {
        Polar p = MathUtils.Cartesian2Polar($c , _center);
        double d = Math.abs(p.radius - _radius);
        if(p.radius<(_radius/5)){
            _touchType = TouchType.Center;
        }else if(d<(_radius/5)){
            boolean isOut = isOutCrossBorder(p.radian);
            if(data!=null && !isOut){
                _touchType = TouchType.Outter;
                _radian = p.radian;
                _lastArea = radian2Area(p.radian);
            }else{
                _touchType = TouchType.Other;
            }
        }else{
            _touchType = TouchType.Other;
        }
    }

    private void onMove(Cartesian $c) {
        Polar p = MathUtils.Cartesian2Polar($c , _center);
        switch (_touchType){
            case Center:
                if(p.radius>(_radius/5)){
                    _touchType = TouchType.Other;
                }
                break;
            case Outter:
                double r = calcRadianAndArea(p.radian);
                r = MathUtils.commonRadian_2Pi(r);
                p.radian = r;
                p.systemRotation(-LeftRandian);
                r = MathUtils.commonRadian_2Pi(p.radian);

                PlayProgress g = ((PlayProgress)data).clone();
                double position = g.position;
                g.position = (int)(g.duration * (r / LongRandian));
                if(g.position!=position){
                    setData2(g);
                }
                break;
            default:
                break;
        }
    }

    private void onUp(Cartesian $c) {
        String name = null;
        Object body = null;
        Polar p = MathUtils.Cartesian2Polar($c , _center);

        switch (_touchType){
            case Center:
                if(p.radius<(_radius/5)){
                    name = ZNotifcationNames.Click;
                }
                break;
            case Outter:
                onMove($c);
                name = AppNotificationNames.Seek;
                body = this.data;
                break;
        }

        _touchType = TouchType.Other;

        if(name!=null){
            sendNotification(name , body);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        _resized = true;
        super.onSizeChanged(w, h, oldw, oldh);
    }

//Draw

    @Override
    protected void dispatchDraw(Canvas $c) {
        super.dispatchDraw($c);

        //背景
        int c = ContextCompat.getColor(AppInstance.mainActivity , R.color.grayplay);
        int w = getWidth() / 70;

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setARGB(Color.alpha(c) , Color.red(c) , Color.green(c) , Color.blue(c));
        p.setStrokeWidth(w);

        RectF r = new RectF(0 , 0 , getWidth() , getHeight());
        r.inset(w , w);
        $c.drawArc(r , 135 , 270 , false , p);

        //当前进度线
        c = ContextCompat.getColor(AppInstance.mainActivity , R.color.greenplay);
        p.setARGB(Color.alpha(c) , Color.red(c) , Color.green(c) , Color.blue(c));
        float progress = 0;
        if(data!=null){
            PlayProgress pg = (PlayProgress)data;
            progress = (float) pg.position / pg.duration;
        }
        if(progress!=0){
            c = ContextCompat.getColor(AppInstance.mainActivity , R.color.greenplay);
            p.setARGB(Color.alpha(c) , Color.red(c) , Color.green(c) , Color.blue(c));
            $c.drawArc(r , 135 , 270*progress , false , p);
        }

        //当前指针
        double a = 270 * progress + 135;
        a = Math.toRadians(a);
        _radius = r.width()/2;
        _center = new Cartesian(r.centerX() , r.centerY());
        Cartesian pos = MathUtils.Polar2Cartesian(a , _radius , _center);
        p.setStyle(Paint.Style.FILL);
        $c.drawCircle((float) pos.x , (float)pos.y , w , p);

        if(_resized){

            a = Math.toRadians(135);
            pos = MathUtils.Polar2Cartesian(a , _radius , _center);
            pos.offset(0 , w*2);
            float xpos = (float)(pos.x - _txt_currect.getWidth() / 2);
            _txt_currect.setX(xpos);
            _txt_currect.setY((float)pos.y);
            _txt_total.setY((float)pos.y);

            a = Math.toRadians(45);
            pos = MathUtils.Polar2Cartesian(a , _radius , _center);
            xpos = (float)(pos.x - _txt_total.getWidth() / 2);
            _txt_total.setX(xpos);

            _resized = false;
        }
    }

//interface
    @Override
    public void setData(Object $o) {
        if(_touchType==TouchType.Outter){
            return;
        }
        setData2((PlayProgress) $o);
    }

//getter and setter
    private void setData2(PlayProgress $g) {
        super.setData($g);
        invalidate();
        _txt_currect.setText(ZUtils.millisecond2Str($g.position));
        _txt_total.setText(ZUtils.millisecond2Str($g.duration));
    }
}
