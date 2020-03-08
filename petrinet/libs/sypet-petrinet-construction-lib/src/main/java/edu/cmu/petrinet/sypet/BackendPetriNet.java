package edu.cmu.petrinet.sypet;

public interface BackendPetriNet<T, MS> {

  boolean containsPlace(T type);

  boolean containsTransition(MS signature);

  void addPlace(T type);

  void addTransition(MS signature);
}
