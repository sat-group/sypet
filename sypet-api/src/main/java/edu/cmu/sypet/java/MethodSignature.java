package edu.cmu.sypet.java;

import java.util.List;

public interface MethodSignature<T extends Type> {

  T returnType();

  String name();

  List<T> parametersTypes();
}
