package com.agoda.myapplication;


import android.support.test.rule.ActivityTestRule;

import org.junit.Test;

@ExperimentTarget
public class JavaTest {
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testssss(){
        assert(true);
    }
}
