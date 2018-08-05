package com.agoda.generator.annotations

import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror


class ExperimentedTestedClass(val typeElement: Element, val values: Array<String>) {

    val type: TypeMirror
        get() = typeElement.asType()
}