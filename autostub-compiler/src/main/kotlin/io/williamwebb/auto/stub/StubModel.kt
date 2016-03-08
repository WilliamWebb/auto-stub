package io.williamwebb.auto.stub

import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.*
import javax.lang.model.element.Modifier

/**
 * Created by williamwebb on 3/6/16.
 */
data class StubModel(
    val name: String,
    val packageName: String,
    val modifiers: Array<Modifier>,
    val constructors: Array<Constructor<*>>,
    val methods: Array<Method>,
    val innerClasses: List<StubModel> = ArrayList())