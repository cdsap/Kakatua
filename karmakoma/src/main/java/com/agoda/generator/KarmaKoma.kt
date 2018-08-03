package com.agoda.generator

import com.agoda.myapplication.ExperimentTarget
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import org.junit.Test
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

@AutoService(Processor::class)
class KarmaKoma : AbstractProcessor() {

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
                    .map {
                        ExperimentedTestedClass(it)
                    }
        }
        generateExperimentedTestedClass(typeElements!!)
        return true
    }

    private fun generateExperimentedTestedClass(experimentedClasses: List<ExperimentedTestedClass>) {
        if (experimentedClasses.isEmpty()) {
            return
        }
        experimentedClasses.forEach {

            val packageName = packageName(processingEnv.elementUtils, it.typeElement)
            val generatedClass = generateClass(it)
            val javaFile = FileSpec.builder(packageName, generatedClass.name ?: "")
                    .addType(generatedClass).build()
            val options = processingEnv.options
            val generatedPath = options["kapt.kotlin.generated"]
            val path = generatedPath
                    ?.replace("(.*)tmp(/kapt/debug/)kotlinGenerated".toRegex(), "$1generated/source$2")

            javaFile.writeTo(File(path, "${javaFile.name}.kt"))
        }
    }

    private fun packageName(elementUtils: Elements, typeElement: Element): String {

        val pkg = elementUtils.getPackageOf(typeElement)
        return pkg.qualifiedName.toString()
    }

    fun generateClass(experimentedClass: ExperimentedTestedClass): TypeSpec {

        val className = experimentedClass.type.toString().split(".").last()
        val builder = TypeSpec.classBuilder("Experimented_$className")
                .superclass(experimentedClass.type.asTypeName())
                .addModifiers(KModifier.PUBLIC)
        generateExperimentedMethods(builder, experimentedClass)
        return builder.build()
    }

    private fun generateExperimentedMethods(
            classBuilder: TypeSpec.Builder,
            experimentedClass: ExperimentedTestedClass
    ) {

        experimentedClass.typeElement.enclosedElements.filter {
            it.kind == ElementKind.METHOD &&
                    it.getAnnotationsByType(Test::class.java).isNotEmpty()
        }.forEach {

            classBuilder.apply {
                val annotations = mutableListOf<AnnotationSpec>()
                it.annotationMirrors.forEach {
                    annotations.add(AnnotationSpec.get(it))
                }
                addFunction(FunSpec.builder("TEST_" + it.simpleName.toString())
                        .addCode("$it")
                        .addAnnotations(annotations)
                        .build())
            }
        }
    }
}

class ExperimentedTestedClass(val typeElement: Element) {

    val type: TypeMirror
        get() = typeElement.asType()
}
