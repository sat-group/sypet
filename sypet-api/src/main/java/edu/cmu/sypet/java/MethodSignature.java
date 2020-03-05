package edu.cmu.sypet.java;

import java.util.List;

public interface MethodSignature {

  Type returnType();

  MethodName name();

  List<Type> parametersTypes();
}
