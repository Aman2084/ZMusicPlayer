package com.aman.exception;

/**
 * ZMusicPlayer 1.0
 * Created on 2018/6/8 16:12
 *
 * @author Aman
 * @Email 1390792438@qq.com
 */

public class SingletonException extends Exception {

    public SingletonException() {
        super("Singleton is exist!");
    }
}
