package edu.cmu.petrinet.simple;

public interface SimplePetriNet<Place, Transition> {

  void addPlace(Place place);

  void addTransition(Transition transition);

  boolean containsPlace(Place place);

  boolean containsTransition(Transition transition);
}
