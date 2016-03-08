package io.williamwebb.auto.stub

import java.lang.reflect.Executable
import java.lang.reflect.Modifier
import java.util.*
import javax.lang.model.type.MirroredTypeException

// java.lang.reflect.Modifier
// javax.lang.model.element.Modifier

/**
 * Created by williamwebb on 3/6/16.
 */
private fun getModifiers(modifiers: Int): Set<javax.lang.model.element.Modifier> {
  val mods  = HashSet<javax.lang.model.element.Modifier>()

  if(Modifier.isAbstract(modifiers))     mods.add(javax.lang.model.element.Modifier.ABSTRACT)
  if(Modifier.isFinal(modifiers))        mods.add(javax.lang.model.element.Modifier.FINAL)
  if(Modifier.isPrivate(modifiers))      mods.add(javax.lang.model.element.Modifier.PRIVATE)
  if(Modifier.isProtected(modifiers))    mods.add(javax.lang.model.element.Modifier.PROTECTED)
  if(Modifier.isPublic(modifiers))       mods.add(javax.lang.model.element.Modifier.PUBLIC)
  if(Modifier.isStatic(modifiers))       mods.add(javax.lang.model.element.Modifier.STATIC)
  if(Modifier.isStrict(modifiers))       mods.add(javax.lang.model.element.Modifier.STRICTFP)
  if(Modifier.isSynchronized(modifiers)) mods.add(javax.lang.model.element.Modifier.SYNCHRONIZED)
  if(Modifier.isTransient(modifiers))    mods.add(javax.lang.model.element.Modifier.TRANSIENT)
  if(Modifier.isVolatile(modifiers))     mods.add(javax.lang.model.element.Modifier.VOLATILE)
//  if(Modifier.isInterface(modifiers))    mods.add(javax.lang.model.element.Modifier.)

  return mods
}

fun Class<*>.modifiers(): Array<javax.lang.model.element.Modifier> {
  return getModifiers(modifiers).toTypedArray()
}

fun Executable.modifiers(): Set<javax.lang.model.element.Modifier> {
  return getModifiers(modifiers)
}

fun AutoStub.Stub.className(): String {
  try {
    return (this.value as Class<*>).toString() // this should throw
  } catch (mte: MirroredTypeException) {
    return mte.typeMirror.toString()
  }
}