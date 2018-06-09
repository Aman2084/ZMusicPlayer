package com.zw;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {



    @Test
    public void addition_isCorrect() throws Exception {

        int a1 = 0b1;
        int a2 = 0b10;
        int a3 = 0b100;

        int b = a1|a3;
        String s = String.valueOf(b);
        System.out.println(s);
        int c = a1 & b;
        System.out.println(c);
        c = a2 & b;
        System.out.println(c);
        c = a3 & b;
        System.out.println(c);

//        _timer.scheduleAtFixedRate(_task2, 0, 4000);
//        _timer.scheduleAtFixedRate(_task3, 0, 3000);

//        double a = Math.toRadians(45);
//        a = Math.tan(a);
//        System.out.printf(String.valueOf(a));
    }
}