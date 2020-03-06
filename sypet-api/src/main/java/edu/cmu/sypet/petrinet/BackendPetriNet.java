package edu.cmu.sypet.petrinet;

public interface BackendPetriNet<Place, Transition> {

  void addPlace(Place place);

  boolean containsPlace(Place type);

  boolean containsTransition(Transition transition);

  void addTransition(Transition transition);
}
