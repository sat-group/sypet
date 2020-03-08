package edu.cmu.petrinet.sypet;

public interface SyPetriNet {

  boolean containsPlace(Type type);

  boolean containsTransition(MethodSignature signature);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
