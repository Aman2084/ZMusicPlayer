package com.zw.ui.others;

import android.os.CountDownTimer;
import android.view.View;

/**
 * ZmusicPlayer 1.0
 * Created on 2017/10/16 18:43
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 */

public class ScrollShowHideHelper {


    private View _target;

    public boolean playing = false;
    public boolean isDown = false;
    private boolean _showBtnCore = false;

    private float _left;
    private float _right;
    private float _mouseX;

    private CountDownTimer _timer;

    public ScrollShowHideHelper(View $target){
        _target = $target;
        setShowCore(false);
    }

    private void setShowCore(boolean $b){
        _showBtnCore = $b;
        if($b){
            _target.setVisibility(View.VISIBLE);
            _target.setClickable(true);
        }else{
            _target.setVisibility(View.INVISIBLE);
            _target.setClickable(false);
        }
    }


    private void show(){
        _timer = new CountDownTimer(1000 , 20) {
            @Override
            public void onTick(long millisUntilFinished) {
                float xpos = _target.getX() - 15;
                _target.setX(xpos);
                if(xpos<=_left){
                    this.cancel();
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                _target.setX(_left);
                playing = false;
            }
        };
        _timer.start();
    }

    private void hide(){
        _timer = new CountDownTimer(1000 , 20) {
            @Override
            public void onTick(long millisUntilFinished) {
                float xpos = _target.getX() + 10;
                _target.setX(xpos);
                if(xpos>=_right){
                    this.cancel();
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                _target.setX(_right);
                playing = false;
                setShowCore(false);
            }
        };
        _timer.start();
    }


//interface
    public void down(float $left , float $right ,float $mouseX){
        isDown = true;
        _left = $left;
        _right = $right;
        _mouseX = $mouseX;

        if(!_showBtnCore){
            setShowCore(true);
            _target.setX(_right);
        }
    }

    public void move(float $x) {
        float d = $x - _mouseX;
        _mouseX = $x;
        float xpos = _target.getX() + d;
        xpos = Math.min(xpos , _right);
        xpos = Math.max(xpos , _left);
        _target.setX(xpos);
    }

    public void up() {
        isDown = false;
        float coreX = _target.getX();
        if(coreX>(_right-15)){//隐藏
            setShowCore(false);
        }else if(coreX<(_left+10)){//完全显示
            _target.setX(_left);
        }else{
            playing = true;
            float xpos = _right - _target.getMeasuredWidth()/2;
            if(coreX>xpos){
                hide();
            }else{
                show();
            }
        }
    }

    public void hideBtns() {
        if(playing){
            _timer.cancel();;
            _timer.onFinish();
        }
        setShowCore(false);
    }
}
