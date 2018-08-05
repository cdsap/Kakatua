package com.agoda.myapplication.test;


import android.support.test.rule.ActivityTestRule;

import com.agoda.generator.annotations.ExperimentTarget;
import com.agoda.myapplication.MainActivity;

import org.junit.Test;

@ExperimentTarget
public class JavaTest {
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void testssss() {
        assert (true);
    }
}
