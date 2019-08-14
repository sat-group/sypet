package edu.cmu.reachability.petrinet;

import java.util.Set;

public interface PetriNet {

  Set<Transition> getTransitions();


  boolean containsTransition(String toString);

  Transition createTransition(String toString);

  Set<Place> getPlaces();

  Place getPlace(final String id) throws NoSuchPlaceException;

  boolean containsPlace(String s);

  void createPlace(String placeID);

  void createFlow(String subclass, String methodName, int w);

  void createFlow(Transition newTransition, Place p, int w);

  Flow getFlow(String id1, String id2) throws NoSuchFlowException;
}
