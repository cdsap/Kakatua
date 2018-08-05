package com.agoda.generator.annotations

import com.agoda.generator.visitor.VisitorAppendExperiments
import com.agoda.generator.visitor.VisitorDefault
import com.agoda.generator.visitor.VisitorReplaceId
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement

class AnnotationProvider {


    fun get(it: AnnotationMirror, values: Array<String>): AnnotationSpec {
        val element = it.annotationType.asElement() as TypeElement
        val builder = AnnotationSpec.builder(element.asClassName())
        val member = CodeBlock.builder()
        val visitor = when (it.annotationType.asElement().simpleName.toString()) {
            AnnotationB::class.simpleName -> VisitorReplaceId(member)
            AnnotationC::class.simpleName -> VisitorAppendExperiments(member,values)
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
}