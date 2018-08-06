package com.agoda.generator.visitor

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleAnnotationValueVisitor7

class AppendExperimentsVisitor(
        private val builder: CodeBlock.Builder,
        private val currentExperiments: List<AnnotationValue>,
        private val newExperiments: Array<String>
) : SimpleAnnotationValueVisitor7<CodeBlock.Builder, String>(builder) {

    override fun defaultAction(o: Any, name: String) = builder.add(CodeBlock.of("%S", "$o"))

    override fun visitAnnotation(a: AnnotationMirror, name: String) = builder.add("%L", AnnotationSpec.get(a))

    override fun visitEnumConstant(c: VariableElement, name: String) = builder.add("%T.%L", c.asType(), c.simpleName)

    override fun visitType(t: TypeMirror, name: String) = builder.add("%T::class", t)

    override fun visitArray(values: List<AnnotationValue>, name: String): CodeBlock.Builder {
        builder.add("[%W%>%>")
        var annotations = mutableListOf<String>()
        var index = 0
        currentExperiments.forEach {
            it.value.toString().split(",")
                    .forEach {
                        if (!annotations.contains("\"$it\"")) {
                            if (index > 0) {
                                builder.add(",$it")
                            } else {
                                index++
                                builder.add(it)
                            }
                            annotations.add(it)
                        }
                    }
        }
        newExperiments.forEach {
            if (!annotations.contains("\"$it\"")) {
                if (index > 0) {
                    builder.add(",\"$it\"")
                } else {
                    index++
                    builder.add("\"$it\"")
                }
                annotations.add(it)
            }
        }
        builder.add("%W%<%<]")
        return builder
    }
}
