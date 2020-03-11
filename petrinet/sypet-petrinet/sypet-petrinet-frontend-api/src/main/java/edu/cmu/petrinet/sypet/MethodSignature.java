package edu.cmu.petrinet.sypet;

import java.util.Collection;

public interface MethodSignature extends Transition {
  Collection<Type> parametersTypes();

  Type returnType();
}
