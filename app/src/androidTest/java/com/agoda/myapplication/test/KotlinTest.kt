package com.agoda.myapplication.test

import android.support.test.rule.ActivityTestRule
import com.agoda.generator.annotations.ReplaceableLabel
import com.agoda.generator.annotations.BExperiments
import com.agoda.generator.annotations.ExperimentTarget
import com.agoda.generator.annotations.Experiments
import com.agoda.myapplication.MainActivity
import org.junit.Rule
import org.junit.Test

@ExperimentTarget([Experiments.EXPERIMENT_2, Experiments.EXPERIMENT_4, Experiments.EXPERIMENT_5])
open class KotlinTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    @ReplaceableLabel("ssksk")
    @BExperiments([Experiments.EXPERIMENT_1, Experiments.EXPERIMENT_2])
    fun testAssert() {
        assert(true)
    }

    @Test
    @ReplaceableLabel("inaki")
    fun testAssert2() {
        assert(true)
    }

    fun testWithoutAnnotationShouldNotBeIncluded() {

    }

}

