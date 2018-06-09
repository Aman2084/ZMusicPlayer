package com.aman.ui.containers.subPage;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.aman.utils.observer.ZNotifcationNames;
import com.aman.utils.observer.ZNotification;

import java.util.Observable;
import java.util.Observer;

/**
 * AmanQuick 1.0
 * Created on 2017/8/20 11:59
 *
 * @author Aman
 * @Email 1390792438@qq.com
 *
 * 子页面管理器：在一个容器中管理子页面的显示和隐藏
 */

public class SubPageManager implements Observer{

    private boolean _playing;

    private  ViewGroup _container;
    private View _currectView;

    public SubPageManager(ViewGroup $c){
        _container = $c;
    }

//动画
    private void startShowAnimation() {
        if(_playing){
            return;
        }
        ISubpage p = (ISubpage) _currectView;
        TranslateAnimation t = null;

        switch (p.get_showMovieType()){
            case Left:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , -1.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f);
                break;
            case Right:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 1.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f);
                break;
            case Top:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , -1.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f);
                break;
            case Bottom:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 1.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f);
                break;
        }
        if(t !=null){
            _playing = true;
            t.setDuration(150);
            t.setAnimationListener(onShow);
            _currectView.startAnimation(t);
        }
    }

    private void startHideAnimation() {
        if(_playing){
            return;
        }
        ISubpage p = (ISubpage) _currectView;
        TranslateAnimation t = null;

        switch (p.get_hideMovieType()){
            case Left:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , -1.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f);
                break;
            case Right:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 1.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f);
                break;
            case Top:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , -1.0f);
                break;
            case Bottom:
                t = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 0.0f,
                        Animation.RELATIVE_TO_PARENT , 1.0f);
                break;
        }
        if(t !=null){
            _playing = true;
            t.setDuration(150);
            t.setAnimationListener(onHide);
            _currectView.startAnimation(t);
        }
    }

    private Animation.AnimationListener onShow = new Animation.AnimationListener(){
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            _playing = false;
            animation.setAnimationListener(null);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

    private Animation.AnimationListener onHide = new Animation.AnimationListener(){
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            _playing = false;
            animation.setAnimationListener(null);
            clear();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

//ZNotification

    @Override
    public void update(Observable $o, Object $arg) {
        if($arg instanceof ZNotification){
            ZNotification n = (ZNotification) $arg;
            if(n.name== ZNotifcationNames.Close){
                hide();
            }
        }
    }


//listeners
    /**
     * 显示展示对象
     * @param $v    要展示的子显示对象，
     *              如果为null则clear
     */
    public void show(@Nullable View $v){
        if(_currectView!=null && $v==_currectView){
            return;
        }
        clear();
        if($v==null){
            return;
        }
        _currectView = $v;
        _container.addView(_currectView);

        if(_currectView instanceof ISubpage){
            ((ISubpage) _currectView).addObserver(this);
            startShowAnimation();
        }
    }

    public void hide(){
        if(_currectView==null){
            return;
        }
        else if(_playing){
            return;
        }
        if(_currectView instanceof ISubpage){
            startHideAnimation();
        }else{
            clear();
        }
    }

    public void  clear(){
        if(_currectView!=null){
            if(_playing){
                _currectView.clearAnimation();
            }
            if(_currectView instanceof ISubpage){
                ((ISubpage) _currectView).deleteObserver(this);
            }
            _container.removeView(_currectView);
            _currectView = null;
        }
    }

}
