package com.aman.utils;

import android.os.Build;

import java.io.File;
import java.io.FileFilter;

/**
 * ZMusicPlayer 1.0
 * Created on 2017/8/24 16:10
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 *
 * 与设备相关的工具函数
 */

public class DeviceUtils {

    public static final int DEVICEINFO_UNKNOWN = -1;


    /**
     * 获取CPU核心数量
     * @return -1（DEVICEINFO_UNKNOWN）表示无法检测到CPU核心数
     */
    public static int getNumberOfCPUCores() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return 1;
        }
        int cores;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            cores = DEVICEINFO_UNKNOWN;
        } catch (NullPointerException e) {
            cores = DEVICEINFO_UNKNOWN;
        }
        return cores;
    }

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            //regex is slow, so checking char by char.
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };
}
