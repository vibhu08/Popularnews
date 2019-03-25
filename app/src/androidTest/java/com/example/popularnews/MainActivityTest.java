package com.example.popularnews;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule=new ActivityTestRule<MainActivity>(MainActivity.class);

    public MainActivity mActivity=null;

    @Before
    public void setUp() throws Exception {
        mActivity=mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch()
    {
        View view=mActivity.findViewById(R.id.swipe_refresh_layout);
        assertNotNull(view);

    }
    @Test
    public void testLoadJson()
    {

        boolean expected=true;
        boolean output=mActivity.LoadJson();
        assertEquals(expected,output);
    }
    @Test
    public void testScroll() throws InterruptedException {

       Thread.sleep(2000);
       Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

    }


    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}