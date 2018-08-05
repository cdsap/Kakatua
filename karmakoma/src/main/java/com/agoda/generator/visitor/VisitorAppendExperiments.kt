package com.agoda.generator.visitor

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.SimpleAnnotationValueVisitor7

class VisitorAppendExperiments (
        private val builder: CodeBlock.Builder,
        values: Array<String>
) : SimpleAnnotationValueVisitor7<CodeBlock.Builder, String>(builder) {

    override fun defaultAction(o: Any, name: String) = builder.add(CodeBlock.of("%S", "$o"))

    override fun visitAnnotation(a: AnnotationMirror, name: String) = builder.add("%L", AnnotationSpec.get(a))

    override fun visitEnumConstant(c: VariableElement, name: String) = builder.add("%T.%L", c.asType(), c.simpleName)

    override fun visitType(t: TypeMirror, name: String) = builder.add("%T::class", t)

    override fun visitArray(values: List<AnnotationValue>, name: String): CodeBlock.Builder {
        var az = mutableListOf<AnnotationValue>()
     //   if(values.)
        //   val aux: CodeBlock.Builder
        //  aux = CodeBlock.builder().add("s")
        //  var a = Visitorx(aux)
        //  builder.add( "assa")
        builder.add("[%W%>%>")
        builder.add("\"inaki${values[0]}\",")
        values.forEachIndexed { index, value ->
            if (index > 0) builder.add(",%W")
            value.accept(this, name)
        }
        builder.add("%W%<%<]")
        return builder
    }
}
