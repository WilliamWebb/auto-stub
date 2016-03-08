package com.example;

public class MyClass {

  private final String wat;

  public static MyClass builder(MyClass clazz) {
    return clazz;
  }

  public MyClass() throws Exception {
    this("");

    if(true) throw new Exception("OMG");
  }

  public MyClass(String wat) {
    this.wat = wat;
  }

  public static String wat(String wat) {
    return "wat";
  }

  private synchronized String wat() {
    return wat;
  }

  private void wat(int x) {

  }


  public static class Builder {
    public Builder(String omg) { }

    public static class Builder2 {

    }
  }
}
