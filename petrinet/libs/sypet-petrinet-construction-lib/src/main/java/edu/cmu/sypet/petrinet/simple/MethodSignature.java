package edu.cmu.sypet.petrinet.simple;

import java.util.Collection;

public interface MethodSignature<T> {

  Collection<T> parametersTypes();

  String name();

  T returnType();
}
