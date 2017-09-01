package com.aman.utils;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/29 19:14
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 * SQLLite 工具类
 */

public class SQLUtils {


    public static final String CreateTable = "CREATE TABLE IF NOT EXISTS ";
    public static final String PrimaryKey = " PRIMARY KEY ";
    public static final String Autoincrement = " AUTOINCREMENT";
    public static final String NotNull = " NOT NULL";
    public static final String AlertTable = "ALERT TABLE";
    public static final String AddColumn = "ADD COLUMN";


    public static String createInt(String $key, @Nullable boolean $notNull) {
        return create($key , "INTEGER" , $notNull);
    }

    public static String createLong(String $key, @Nullable boolean $notNull) {
        return create($key , "LONG" , $notNull);
    }

    public static String createFloat(String $key, @Nullable boolean $notNull) {
        return create($key , "FLOAT" , $notNull);
    }

    public static String createVarchar(String $key, @Nullable boolean $notNull) {
        return create($key , "VARCHER" , $notNull);
    }

    public static String create(String $key, String $type, @Nullable boolean $notNull) {
        $key += " " + $type;
        if($notNull){
            $key += NotNull;
        }
        return $key;
    }

    public static String format(String $top, ArrayList<String> $a) {
        String str = "";
        int n = $a.size();
        if(n>0){
            str = $a.get(0);
            for (int i = 1; i <n ; i++) {
                str += "," + $a.get(i);
            }
        }
        str = $top + "(" + str + ");";
        return str;
    }

    public static String select(ArrayList<String> $a , String $tatbleName , String $condition){
        String s = String.format(" frome %s where %s;" , $tatbleName , $condition);
        String str = "select * " + ZUtils.join($a , ",") + s;
        return str;
    }

    public static String dropTable(String $name) {
        String s = "DROP TABLE IF EXISTS " + $name + ";";
        return s;
    }
}
