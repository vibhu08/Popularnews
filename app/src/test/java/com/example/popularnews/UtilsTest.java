package com.example.popularnews;

import org.junit.Test;

import java.text.DateFormat;

import okhttp3.internal.Util;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void dateFormat() {

        String input="2019-03-23T17:08:00Z";
        String expected="Sat, 23 Mar 2019";
        String output= Utils.DateFormat(input);
        assertEquals(expected,output);
    }

}