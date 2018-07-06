package com.aman.utils.devices;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * AmanQuick 1.0
 * Created on 2017/9/14 18:42
 *
 * @author Aman
 * @Email: 1390792438@qq.com
 *
 * CPU效率检测，每妙钟返回CPU占用率
 */

public class CPUThread extends Thread {
    private final Handler _handler;

    public CPUThread(Handler $h){
        super();
        _handler = $h;
    }

    @Override
    public void run() {
        super.run();
        while(true){
            int r = getCPU();
            Message m = new Message();
            m.arg1 = r;
            _handler.sendMessage(m);

            try{
                sleep(1000);
            }catch (InterruptedException $e){}
        }
    }


    public int getCPU(){
        Process p = null;
        try{
            p = Runtime.getRuntime().exec("top -n 1");
        }catch (IOException $e){
            return -1;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder tv = new StringBuilder();
        int rate = 0;
        String Result;
        try{
            ;
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                    tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");
                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }

        }catch (IOException $e){
            return -1;
        }

        return rate;
    }

}
