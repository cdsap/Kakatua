package com.agoda.generator

import com.agoda.generator.annotations.AnnotationB
import com.agoda.generator.annotations.AnnotationC
import com.agoda.generator.annotations.ExperimentedTestedClass
import com.agoda.generator.entities.Meta
import com.agoda.generator.visitor.VisitorAppendExperiments
import com.agoda.generator.visitor.VisitorDefault
import com.agoda.generator.visitor.VisitorReplaceId
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

class Kakatua(private val environment: ProcessingEnvironment) {

    fun generate(typeElements: List<ExperimentedTestedClass>) {
        if (typeElements.isEmpty()) {
            return
        }
        val meta = Meta(environment.options)
        typeElements.forEach {

            val packageName = packageName(environment.elementUtils, it.typeElement)
            val generatedClass = generateClass(it)
            val javaFile = FileSpec.builder(packageName, generatedClass.name ?: "")
                    .addType(generatedClass).build()


            javaFile.writeTo(File(meta.path, "${javaFile.name}.kt"))
        }

    }

    private fun generateClass(experimentedClass: ExperimentedTestedClass): TypeSpec {

        val className = experimentedClass.type.toString().split(".").last()
        val builder = TypeSpec.classBuilder("Experimented_$className")
                .superclass(experimentedClass.type.asTypeName())
                .addModifiers(KModifier.PUBLIC)
        generateExperimentedMethods(builder, experimentedClass, experimentedClass.values)
        return builder.build()
    }

    private fun generateExperimentedMethods(
            classBuilder: TypeSpec.Builder,
            experimentedClass: ExperimentedTestedClass,
            values: Array<String>
    ) {

        experimentedClass.typeElement.enclosedElements
                .filter {
                    it.kind == ElementKind.METHOD //&&
                    //  it.getAnnotationsByType(Test::class.java).isNotEmpty()
                }
                .forEach {
                    classBuilder.apply {
                        addFunction(
                                FunSpec.builder("TEST_" + it.simpleName.toString())
                                        .addCode("$it")
                                        .addAnnotations(
                                                it.annotationMirrors.flatMap {
                                                    val annotationSpec = getAnn(it, values)
                                                    mutableListOf(annotationSpec)
                                                }
                                        )
                                        .build()
                        )
                    }
                }
    }

    private fun getAnn(it: AnnotationMirror, values: Array<String>): AnnotationSpec {
        val element = it.annotationType.asElement() as TypeElement
        val builder = AnnotationSpec.builder(element.asClassName())
        val member = CodeBlock.builder()
        val visitor = when (it.annotationType.asElement().simpleName.toString()) {
            AnnotationB::class.simpleName -> VisitorReplaceId(member)
            AnnotationC::class.simpleName -> VisitorAppendExperiments(member)
            else -> VisitorDefault(member)
        }

        for (executableElement in it.elementValues.keys) {
            val name = executableElement.simpleName.toString()
            if (name != "value") {
                CodeBlock.builder().add("%L = ", name)
            }
            val value = it.elementValues[executableElement]!!
            value.accept(visitor, name)
            builder.addMember(member.build())
        }

        return builder.build()
    }

    private fun packageName(elementUtils: Elements, typeElement: Element): String {

        val pkg = elementUtils.getPackageOf(typeElement)
        return pkg.qualifiedName.toString()
    }
}