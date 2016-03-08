package com.example;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by williamwebb on 3/6/16.
 */
public class StubsTest {

  @Test
  public void test() throws Exception {
    new MyClass();
    MyClass.builder(null);
    assertEquals("",MyClass.wat(""));
    new MyClass.Builder("omg");
  }

}