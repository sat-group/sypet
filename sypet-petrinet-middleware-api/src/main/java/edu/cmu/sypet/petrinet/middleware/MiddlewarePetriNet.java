package edu.cmu.sypet.petrinet.middleware;

public interface MiddlewarePetriNet<Place, Transition> {

  boolean containsPlace(Place place);

  boolean containsTransition(Transition transition);
}
