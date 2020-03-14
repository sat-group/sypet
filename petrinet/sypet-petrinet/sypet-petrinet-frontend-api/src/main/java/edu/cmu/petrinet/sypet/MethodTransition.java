package edu.cmu.petrinet.sypet;

import java.util.Collection;

public interface MethodTransition extends Transition {
  Collection<Type> parametersTypes();

  Type returnType();
}
