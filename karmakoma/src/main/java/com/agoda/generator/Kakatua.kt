package com.agoda.generator

import com.agoda.generator.annotations.*
import com.agoda.generator.entities.Meta
import com.squareup.kotlinpoet.*
import org.junit.Test
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.util.Elements

class Kakatua(private val environment: ProcessingEnvironment) {

    private val annotationProvider = AnnotationProvider()

    fun init(roundEnv: RoundEnvironment?) {
        val typeElements = roundEnv?.let {
            it.getElementsAnnotatedWith(ExperimentTarget::class.java)
                    .filter {
                        !it.modifiers.contains(Modifier.FINAL)
                    }
                    .map {
                        val a = it.getAnnotation(ExperimentTarget::class.java)
                        ExperimentedTestedClass(it, a.values)
                    }

        }
        generate(typeElements!!)
    }

    private fun generate(experimentedClass: List<ExperimentedTestedClass>) {
        if (experimentedClass.isEmpty()) {
            return
        }
        val meta = Meta(environment.options)
        experimentedClass.forEach {

            val packageName = packageName(environment.elementUtils, it.typeElement)
            val generatedClass = generateTestClass(it)
            val javaFile = FileSpec.builder(packageName, generatedClass.name ?: "")
                    .addType(generatedClass).build()

            javaFile.writeTo(File(meta.path, "${javaFile.name}.kt"))
        }

    }

    private fun generateTestClass(experimentedClass: ExperimentedTestedClass): TypeSpec {
        val className = experimentedClass.type.toString().split(".").last()
        val builder = TypeSpec.classBuilder("Experimented_$className")
                .superclass(experimentedClass.type.asTypeName())
                .addModifiers(KModifier.PUBLIC)
        generateTestMethods(builder, experimentedClass, experimentedClass.values)
        return builder.build()
    }

    private fun generateTestMethods(
            classBuilder: TypeSpec.Builder,
            experimentedClass: ExperimentedTestedClass,
            values: Array<Experiments>
    ) {

        experimentedClass.typeElement.enclosedElements
                .filter {
                    it.kind == ElementKind.METHOD &&
                            it.getAnnotationsByType(Test::class.java).isNotEmpty()
                }
                .forEach {
                    classBuilder.apply {
                        val element = it
                        addFunction(
                                FunSpec.builder("TEST_" + it.simpleName.toString())
                                        .addCode("$it\n")
                                        .also { generateTestAnnotations(it, element, values) }
                                        .build()
                        )
                    }
                }
    }

    private fun generateTestAnnotations(builder: FunSpec.Builder, element: Element, values: Array<Experiments>) {
        var contents = false
        element.annotationMirrors.forEach {
            if (it.annotationType.asElement().simpleName.toString() == BExperiments::class.simpleName) {
                contents = true
            }

        }

        builder.addAnnotations(
                element.annotationMirrors.flatMap {
                    mutableListOf(annotationProvider.get(
                            it, values))
                }
        )

        if (!contents) {
            builder.addAnnotation(
                    AnnotationSpec.builder(BExperiments::class.java)
                            .addMember(annotationProvider.get(values)).build()
            )
        }

    }

    private fun packageName(elementUtils: Elements, typeElement: Element): String {
        val pkg = elementUtils.getPackageOf(typeElement)
        return pkg.qualifiedName.toString()
    }
}