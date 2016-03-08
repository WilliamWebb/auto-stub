package io.williamwebb.auto.stub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by williamwebb on 3/5/16.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface AutoStub {
  Stub[] value();

  @interface Stub {
    Class<?> value();
  }
}
