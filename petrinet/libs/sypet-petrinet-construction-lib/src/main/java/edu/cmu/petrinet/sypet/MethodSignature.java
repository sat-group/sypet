package edu.cmu.petrinet.sypet;

import java.util.Collection;

public interface MethodSignature {

  Collection<Type> parametersTypes();

  String name();

  Type returnType();
}
