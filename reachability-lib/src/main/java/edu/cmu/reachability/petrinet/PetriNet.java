package edu.cmu.reachability.petrinet;

import java.util.Set;

public interface PetriNet {
  Set<Transition> getTransitions();

  Set<Place> getPlaces();

  Place getPlace(final String id) throws NoSuchPlaceException;
}
