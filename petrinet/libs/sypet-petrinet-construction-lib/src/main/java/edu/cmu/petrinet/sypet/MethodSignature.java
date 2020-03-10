package edu.cmu.petrinet.sypet;

import java.util.Collection;

public interface MethodSignature<T, U> extends Identifiable<U> {
  Collection<Type<T>> parametersTypes();

  Type<T> returnType();
}
