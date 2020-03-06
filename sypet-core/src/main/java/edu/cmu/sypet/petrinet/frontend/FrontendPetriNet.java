package edu.cmu.sypet.petrinet.frontend;

public interface FrontendPetriNet<Place, Transition> {

  boolean containsPlace(Place place);

  boolean containsTransition(Transition transition);
}
