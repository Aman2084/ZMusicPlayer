package com.zw.ui.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.zw.R;


/**
 * Created by Administrator on 2017/7/13.
 */

public class MyRadioButton extends RadioButton {

    private int _drawSize;


    public MyRadioButton(Context $c){
        this($c , null , 0);
    }

    public MyRadioButton(Context $c , AttributeSet $a ){
        this($c , $a , 0);
    }

    public MyRadioButton(Context $c , AttributeSet $a , int $defStyle){
        super($c , $a , $defStyle);

        Drawable left=null , right=null , top=null , bottom=null;

        TypedArray a = $c.obtainStyledAttributes($a , R.styleable.MyRadioButton);
        int n = a.getIndexCount();
        for (int i = 0; i < n ; i++) {
            int arrt = a.getIndex(i);
            switch (arrt){
                case R.styleable.MyRadioButton_drawableSize:
                    _drawSize = a.getDimensionPixelSize(R.styleable.MyRadioButton_drawableSize, 50);
                    break;
                case R.styleable.MyRadioButton_drawableTop:
                    top = a.getDrawable(arrt);
                    break;
                case R.styleable.MyRadioButton_drawableBottom:
                    bottom = a.getDrawable(arrt);
                    break;
                case R.styleable.MyRadioButton_drawableLeft:
                    left = a.getDrawable(arrt);
                    break;
                case R.styleable.MyRadioButton_drawableRight:
                    right = a.getDrawable(arrt);
                    break;
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(left , top , right , bottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable $left ,
                                                        Drawable $top ,
                                                        Drawable $right ,
                                                        Drawable $bottom){
        if($left!=null){
            $left.setBounds(0 , 0 , _drawSize , _drawSize);
        }
        if($right!=null){
            $right.setBounds(0 , 0 , _drawSize , _drawSize);
        }
        if($top!=null){
            $top.setBounds(0 , 0 , _drawSize , _drawSize);
        }
        if($bottom!=null){
            $bottom.setBounds(0 , 0 , _drawSize , _drawSize);
        }
        setCompoundDrawables($left , $top , $right , $bottom);
    }


}
