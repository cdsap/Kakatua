package com.agoda.myapplication

import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

@ExperimentTarget
open class KotlinTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    @AnnotationB
    fun testAssert() {
        assert(true)
    }

    fun testWithoutAnnotationShouldNotBeIncluded() {

    }

}

