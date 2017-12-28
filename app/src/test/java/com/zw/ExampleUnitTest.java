package com.zw;

import org.junit.Test;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() throws Exception {

        ArrayList<Integer> a = new ArrayList();
        a.add(123);
        a.add(645);

        String[] s = new String[a.size()];
        for (int i = 0; i <a.size() ; i++) {
            s[i] = a.get(i).toString();
        }

        String aa = s[0];

    }
}