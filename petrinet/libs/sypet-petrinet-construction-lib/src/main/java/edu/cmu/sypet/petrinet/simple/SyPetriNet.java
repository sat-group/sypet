package edu.cmu.sypet.petrinet.simple;

public interface SyPetriNet<T extends Type, MS extends MethodSignature<T>> {

  boolean containsPlace(T type);

  boolean containsTransition(MS signature);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
