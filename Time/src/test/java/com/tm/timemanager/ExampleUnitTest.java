package com.tm.timemanager;

import android.util.Log;

import com.tm.timemanager.Utils.DateUtil;

import org.apache.http.impl.cookie.DateUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testDateUtil(){
        String s = DateUtil.formatTime(660000);

        String s1 = s.substring(0, 2);
        String s2 = s.substring(3, 5);
        System.out.println(s);
        System.out.println(s1);
        System.out.println(s2);
    }
}