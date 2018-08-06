package com.agoda.myapplication.test

import android.support.test.rule.ActivityTestRule
import com.agoda.generator.annotations.AnnotationB
import com.agoda.generator.annotations.AnnotationC
import com.agoda.generator.annotations.ExperimentTarget
import com.agoda.myapplication.MainActivity
import org.junit.Rule
import org.junit.Test

@ExperimentTarget(["JUAN", "ANTONIO", "inaki"])
open class KotlinTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    @AnnotationB("ssksk")
    @AnnotationC(["inaki", "villar"])
    fun testAssert() {
        assert(true)
    }

    fun testWithoutAnnotationShouldNotBeIncluded() {

    }

}

