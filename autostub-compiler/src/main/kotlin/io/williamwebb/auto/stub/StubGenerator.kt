package io.williamwebb.auto.stub

import com.squareup.javapoet.*
import java.lang.reflect.Constructor
import java.lang.reflect.Executable
import java.lang.reflect.Method
import java.lang.reflect.Type
import java.util.HashSet
import javax.lang.model.element.Modifier

/**
 * Created by williamwebb on 3/6/16.
 */
class StubGenerator(internal val model: StubModel) {

  final val INSTANCES = HashSet<Type>()

  fun brewJava(): JavaFile {

    val classBuilder = createClass(model)
    classBuilder.addModifiers(*model.modifiers)

    if(INSTANCES.isNotEmpty()) {
      classBuilder.addStaticBlock(createStaticInstances(classBuilder))
    }

    return JavaFile.builder(model.packageName, classBuilder.build()).build()
  }

  private fun createClass(model: StubModel): TypeSpec.Builder {
    println("Brewing: " + model.packageName + "." + model.name)

    val builder = TypeSpec.classBuilder(model.name)
    builder.addModifiers(*model.modifiers)

    model.constructors.forEach {
      builder.addMethod(createConstructor(it).build())
    }

    model.methods.forEach {
      builder.addMethod(createMethod(it).build())
    }

    model.innerClasses.forEach {
      builder.addType(createClass(it).build())
    }

    return builder
  }

  private fun createConstructor(constructor: Constructor<*>): MethodSpec.Builder {
    return createExecutable(MethodSpec.constructorBuilder(), constructor)
  }

  private fun createMethod(method: Method): MethodSpec.Builder {
    val spec = createExecutable(MethodSpec.methodBuilder(method.name), method)

    if(!(method.returnType.equals(Void.TYPE))) {
      spec.returns(method.returnType)
      spec.addStatement("return ${formatType(method.returnType)}")

      if(!INSTANCES.contains(method.returnType)) {
        INSTANCES.add(method.returnType)
      }
    }

    return spec
  }

  private fun createExecutable(builder: MethodSpec.Builder, exec: Executable): MethodSpec.Builder {
    builder.addModifiers(exec.modifiers())

    exec.annotations.forEach {
      builder.addAnnotation(ClassName.get(it.javaClass))
    }

    exec.parameters.forEach {
      builder.addParameter(it.type, it.name)
    }

    exec.exceptionTypes.forEach {
      builder.addException(it)
    }

    return builder
  }

  private fun createStaticInstances(typeSpec: TypeSpec.Builder): CodeBlock {
    val codeBlock = CodeBlock.builder()

    tryCatch(codeBlock, "Exception", {
      INSTANCES.forEach {
        val type = formatType(it)
        typeSpec.addField(it, type, Modifier.STATIC, Modifier.PRIVATE)
        codeBlock.addStatement("  $type = ${it.typeName}.class.newInstance()")
      }
    })

    return codeBlock.build()
  }

  private inline fun tryCatch(codeBlock : CodeBlock.Builder, exception: String, body: () -> Unit) {
    codeBlock.add("try {").add("\n")
    body()
    codeBlock.add("} catch($exception noop) {}").add("\n")
  }

  private fun formatType(type: Type) = "${type.typeName.replace(".","_")}_INSTANCE"
}