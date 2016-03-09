package io.williamwebb.auto.stub

import com.google.auto.service.AutoService
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

/**
 * Created by williamwebb on 3/6/16.
 */

@AutoService(Processor::class)
open class AutoStubProcessor : AbstractProcessor() {

  private var typeUtils: Types? = null
  private var elementUtils: Elements? = null
  private var filer: Filer? = null
  private var messager: Messager? = null

  @Synchronized override fun init(processingEnv: ProcessingEnvironment) {
    super.init(processingEnv)

    typeUtils = processingEnv.typeUtils
    elementUtils = processingEnv.elementUtils
    filer = processingEnv.filer
    messager = processingEnv.messager
  }

  override fun process(set: Set<TypeElement>, env: RoundEnvironment): Boolean {
    var models = ArrayList<StubModel>()
    try {
      models.addAll(getModels(env))
    } catch(cnfe :ClassNotFoundException) {
      error("The artifact to be stubbed must be added in provided and apt scope.")
    }

    models.forEach {
      StubGenerator(it).brewJava().writeTo(filer)
    }

    return true
  }

  private fun getModels(environment: RoundEnvironment): List<StubModel> {
    return environment.getElementsAnnotatedWith(AutoStub::class.java)
      .map { it.getAnnotation(AutoStub::class.java) }
      .map { it.value.toList() }
      .flatMap { it }
      .map { Class.forName(it.className()) }
      .map {
        createStubModel(it, it.`package`.name)
      }
  }

  private fun createStubModel(clazz: Class<*>, packageName: String): StubModel =
      StubModel(
          clazz.simpleName,
          packageName,
          clazz.modifiers(),
          clazz.declaredConstructors,
          clazz.declaredMethods,
          processInnerClasses(packageName, clazz)
      )

  private fun processInnerClasses(parent: String, clazz: Class<*>): List<StubModel> {
    return clazz.declaredClasses.map {
      createStubModel(it, "${clazz.`package`.name}.$parent")
    }.toList()
  }

  override fun getSupportedSourceVersion(): SourceVersion {
    return SourceVersion.latestSupported()
  }

  override fun getSupportedAnnotationTypes(): Set<String> {
    return setOf(AutoStub::class.java.canonicalName)
  }
}