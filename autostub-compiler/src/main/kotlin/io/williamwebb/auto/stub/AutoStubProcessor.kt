package io.williamwebb.auto.stub

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * Created by williamwebb on 3/6/16.
 */
@AutoService(Processor::class)
open class AutoStubProcessor : AbstractProcessor() {

  override fun process(set: Set<TypeElement>, env: RoundEnvironment): Boolean {
    try {
      getModels(env).forEach {
        StubGenerator(it).brewJava().writeTo(processingEnv.filer)
      }
    } catch(cnfe :ClassNotFoundException) {
      error("The artifact to be stubbed must be added in provided and apt scope.")
    }

    return true
  }

  private fun getModels(environment: RoundEnvironment): List<StubModel> {
    return environment.getElementsAnnotatedWith(AutoStub::class.java)
      .map { it.getAnnotation(AutoStub::class.java) }
      .map { it.value.toList() }
      .flatMap { it }
      .map { Class.forName(it.className()) }
      .map { createStubModel(it, it.`package`.name) }
      .toList() // not sure how it worked without this?
  }

  private fun createStubModel(clazz: Class<*>, packageName: String) =
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
      createStubModel(it, "${clazz.packageName()}.$parent")
    }.toList()
  }

  override fun getSupportedSourceVersion(): SourceVersion {
    return SourceVersion.latestSupported()
  }

  override fun getSupportedAnnotationTypes(): Set<String> {
    return setOf(AutoStub::class.java.canonicalName)
  }
}