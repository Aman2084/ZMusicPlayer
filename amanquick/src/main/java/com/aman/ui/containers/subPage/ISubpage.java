package com.aman.ui.containers.subPage;

import com.aman.utils.observer.IZObservable;

/**
 * AmanQuick 1.0
 * Created on 2017/8/20 10:41
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 *
 * 子页面功能接口，含义如下功能：
 *  show/hide动画
 */

public interface ISubpage extends IZObservable{

    AnimationTypes get_showMovieType();
    void set_showMovieType(AnimationTypes $type);

    AnimationTypes get_hideMovieType();
    void set_hideMovieType(AnimationTypes $type);


}
