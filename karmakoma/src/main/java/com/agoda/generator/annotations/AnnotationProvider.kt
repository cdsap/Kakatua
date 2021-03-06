package com.agoda.generator.annotations

import com.agoda.generator.visitor.AppendExperimentsVisitor
import com.agoda.generator.visitor.DefaultVisitor
import com.agoda.generator.visitor.ReplaceIdVisitor
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement

class AnnotationProvider {


    fun get(it: AnnotationMirror, values: Array<Experiments>): AnnotationSpec {
        val element = it.annotationType.asElement() as TypeElement
        val builder = AnnotationSpec.builder(element.asClassName())
        val member = CodeBlock.builder()
        val visitor = when (it.annotationType.asElement().simpleName.toString()) {
            ReplaceableLabel::class.simpleName -> ReplaceIdVisitor(member)
            BExperiments::class.simpleName -> AppendExperimentsVisitor(member, it.elementValues.toList()
                    .flatMap {
                        listOf(it.second)
                    }, values)
            else -> DefaultVisitor(member)
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

    fun get(values: Array<Experiments>): CodeBlock {
        val member = CodeBlock.builder()
        member.add("[%W%>%>")
        var index = 0
        for (executableElement in values) {
            if (index > 0) {
                member.add(",\n${executableElement.declaringClass.name}.$executableElement")
            } else {
                member.add("${executableElement.declaringClass.name}.$executableElement")
                index++
            }
        }
        member.add("%W%<%<]")
        return member.build()
    }

}