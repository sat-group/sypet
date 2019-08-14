package edu.cmu.sypet.java;

import java.util.List;

public interface MethodSignature {
  String name();

  Type returnType();

  List<Type> parameterTypes();

  boolean isStatic();

  boolean isConstructor();

  Type declaringClass();
}
