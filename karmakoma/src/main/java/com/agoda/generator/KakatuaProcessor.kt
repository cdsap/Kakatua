package com.agoda.generator

import com.agoda.generator.annotations.A
import com.agoda.generator.annotations.ExperimentedTestedClass
import com.agoda.generator.annotations.ExperimentTarget
import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*

@AutoService(Processor::class)
class KakatuaProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(ExperimentTarget::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(
            annotations: MutableSet<out TypeElement>?,
            roundEnv: RoundEnvironment?
    ): Boolean {
        val typeElements = roundEnv?.let {
            it.getElementsAnnotatedWith(ExperimentTarget::class.java)
                    .filter {
                        !it.modifiers.contains(Modifier.FINAL)
                    }
                    .map {
                        val a = it.getAnnotation(ExperimentTarget::class.java)
                        ExperimentedTestedClass(it, emptyArray())
                    }

        }
        Kakatua(processingEnv).generate(typeElements!!)
        return true
    }
}

