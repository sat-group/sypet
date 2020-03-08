package edu.cmu.sypet.petrinet.simple;

public interface BackendPetriNet<T, MS> {

  boolean containsPlace(T type);

  boolean containsTransition(MS signature);

  void addPlace(T type);

  void addTransition(MS signature);
}
